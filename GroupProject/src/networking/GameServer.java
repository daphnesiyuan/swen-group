package networking;

import gameLogic.Game;
import gameLogic.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dataStorage.XMLLoader;
import dataStorage.XMLSaver;

/**
 *Game server class of which handles all the processing of the game on a server side level.
 *If a change is made to any clients information, then it is changed on all clients levels.
 * @author veugeljame
 *
 */
public class GameServer extends ChatServer {

	// Game that all players are playing off
	private Game gameServer;

	private Object gameModifiedLock = new Object();
	private boolean gameModified = false;

	private ArrayList<AI> gameAI = new ArrayList<AI>();

	/**
	 * Creates a new Game Server that will load a map from the file given.
	 * Waits for input from the console to run commands.
	 * @param gameToLoad MapFile to load
	 */
	public GameServer() {
		super();

		// Create a new game
		gameServer = new Game();

		// Server listens for input directly to servers terminal Thread
		Thread serverTextBox = new Thread(new ServerTextListener());
		serverTextBox.start();

		Thread tickThread = new Thread(new TickThread());
		tickThread.start();
	}

	/**
	 * Creates a new Game Server that will load a map from the file given.
	 * Waits for input from the console to run commands.
	 * @param gameToLoad MapFile to load
	 */
	public GameServer(File gameToLoad) {
		super();

		// Server listens for input directly to servers terminal Thread
		Thread serverTextBox = new Thread(new ServerTextListener());
		serverTextBox.start();

		Thread tickThread = new Thread(new TickThread());
		tickThread.start();

		// Load the game from a file a file
		loadGame(gameToLoad);
	}

	/**
	 *Loads the file given creating a new gameworld
	 * @param file Map to load into the game
	 * @return True if successfully loaded
	 */
	public boolean loadGame(File file){
		// Load a file
		XMLLoader loader = new XMLLoader();

		Game game = loader.loadGame(file);
		if( game != null ){
			System.out.println("Successfully loaded game from file:\n\t" + file);

			gameServer = game;
			return true;
		}

		System.out.println("Failed to load game from file:\n\t" + file);

		// Could not load the file
		return false;
	}

	/**
	 * Saves the game to a file
	 * @return
	 */
	public boolean saveGame(){
		XMLSaver saver = new XMLSaver(gameServer);
		return saver.saveGame();
	}

	/**
	 * Updates all clients with a new room according to the state of the game logic
	 */
	private synchronized void updateAllClients(){

			// Make sure we have a server to update the clients with
			if( gameServer == null ){
				return;
			}

			// Sent the room of the client to all it's clients
			for (int i = 0; i < clients.size(); i++) {

				// Get each of our clients, and the room they are in
				ClientThread client = clients.get(i);
				Room room = gameServer.getRoom(client.getPlayerName());

				// Send the new room to the player
				client.sendData(new RoomUpdate(room));
			}

			setGameModified(false);
	}

	/**
	 * Creates a new RandomAI and adds it to the game
	 * @return True if the AI was added successfully
	 */
	public boolean createAI(){
		// Max of 10 bots
		if(gameAI.size() >= 10) return false;

		String botName = "ai" + gameAI.size();
		Room room = gameServer.addPlayer(botName);

		if( room != null ){
			AI ai = new RandomAI(room, botName);
			gameAI.add(ai);
			return gameServer.addAI(ai);
		}
		return false;
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {
		if( data.getData() instanceof ChatMessage ){
			ChatMessage cm = (ChatMessage)data.getData();

			// Check for a command ONLY used by the GameServer
			if( cm.message.startsWith("/addbot") ){

				// Only admins can close the server
				if( isAdmin(data.getIPAddress()) ){

					if( !createAI() ){
						System.out.println("Didn't add AI!");
					}

					// Confirmation
					getClientFromIP(data.getIPAddress()).sendData(data);
					return;
				}
			}
		}

		// Tell chatMessage
		super.retrieveObject(data);

		// Determine what to do with each of the different types of objects sent from clients
		if( data.getData() instanceof Move ){
			Move move = (Move)data.getData();

			// A move performed by a client
			processMove(move, data);
		}

	}

	/**
	 *
	 * @param move Move wanting to be performed by a client
	 * @param data NetworkObject sent through the network
	 */
	private synchronized void processMove(Move move, NetworkObject data){

		// Move the players Avatar
		if( gameServer.moveAvatar(move) ){
			setGameModified(true);
		}
	}

	/**
	 * A class that listens for input from the console and sends the input to
	 * processServerMessage
	 *
	 * @author veugeljame
	 *
	 */
	private class ServerTextListener implements Runnable {
		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String text;

			while (true) {
				try {
					text = (String) br.readLine();

					if (text != null) {

						// Process to everyone
						retrieveObject(new NetworkObject(getIPAddress(), new ChatMessage("~Admin", text, chatMessageColor, true)));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void newClientConnection(ClientThread cl) {
		super.newClientConnection(cl);


		// Set new players current room
		Room currentRoom = gameServer.addPlayer(cl.getPlayerName());


		System.out.println("New Client: " + cl.getPlayerName());
		System.out.println("Room: " + currentRoom);
		System.out.println("Avatar: " + currentRoom.getAvatar(cl.getPlayerName()));
		System.out.println("totalAvatars: " + gameServer.getActiveAvatars().size());

		// Send the room back to the client
		if( currentRoom != null ){
			cl.sendData(new RoomUpdate(currentRoom));
		}

	}

	@Override
	public void clientRejoins(ClientThread cl) {
		super.clientRejoins(cl);

		Room currentRoom = gameServer.getRoom(cl.getPlayerName());
		if( currentRoom != null ){
			cl.sendData(new RoomUpdate(currentRoom));
		}
	}

	/**
	 * Checks if the game has been modified so we are able to update our clients
	 * @return True if modified
	 */
	public boolean isGameModified() {

		synchronized(gameModifiedLock){
			return gameModified;
		}
	}

	/**
	 * Assigns the game to be modified or not
	 * @param gameModified What to change it to
	 */
	public void setGameModified(boolean gameModified) {
		synchronized(gameModifiedLock){
			this.gameModified = gameModified;
		}
	}

	/**
	 * Removes the client at the given location from our list of clients
	 *
	 * @param client
	 *            index to remove a client from
	 * @param reconnecting
	 */
	@Override
	public synchronized boolean removeClient(ClientThread client, boolean reconnecting) {
		if( !super.removeClient(client, reconnecting) ){
			System.out.println("Couldn't remove from Server");
			return false;
		}

		if ( !reconnecting ) {
			if( !gameServer.removePlayerFromGame(client.getPlayerName()) ){
				System.out.println("Couldn't remove from GameLogic!");
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		new GameServer();
	}

	private class TickThread implements Runnable{

		@Override
		public void run(){
			try {
				while(true){
					gameServer.tickAllAI();
					updateAllClients();

					Thread.sleep(30);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
}
