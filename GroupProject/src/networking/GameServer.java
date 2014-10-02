package networking;

import gameLogic.Game;
import gameLogic.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

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
	private Object serverLock = new Object();
	private Game gameServer;

	private Object gameModifiedLock = new Object();
	private boolean gameModified = false;

	public GameServer() {
		super();

		// Create a new game
		gameServer = new Game();

		// Server listens for input directly to servers terminal Thread
		Thread serverTextBox = new Thread(new ServerTextListener());
		serverTextBox.start();

		Thread refreshThread = new Thread(){
			@Override
			public void run(){
				try {
					while(true){

						// Update clients if the game has been modified
						if( isGameModified() ){
							updateAllClients();
						}

						Thread.sleep(30);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		refreshThread.start();

	}

	public GameServer(File gameToLoad) {
		super();

		// Server listens for input directly to servers terminal Thread
		Thread serverTextBox = new Thread(new ServerTextListener());
		serverTextBox.start();

		Thread refreshThread = new Thread(){
			@Override
			public void run(){
				try {
					while(true){
						updateAllClients();

						Thread.sleep(30);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		refreshThread.start();

		// Load the game from a file a file
		loadGame(gameToLoad);
	}

	/**
	 *
	 * @param file
	 * @return
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
	private void updateAllClients(){

		System.out.println("Updating Clients: " + Calendar.getInstance().getTime());

			// Make sure we have a server to update the clients with
			if( gameServer == null ){
				return;
			}

			// Sent the room of the lcient ot all it's clients
			for (int i = 0; i < clients.size(); i++) {

				// Get each of our clients, and the room they are in
				ClientThread client = clients.get(i);
				Room room = gameServer.getRoom(client.player.getName());

				//System.out.println("Sending Room " + room + " to " + client.getPlayerName());

				// Send the new room to the player
				client.sendData(new RoomUpdate(room));
			}

				setGameModified(false);
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {
		super.retrieveObject(data);

		// Determine what to do with each of the different types of objects sent from clients
		if( data.getData() instanceof Move ){
			Move move = (Move)data.getData();

			System.out.println(move + " " + Calendar.getInstance().getTime());

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

		System.out.println("Processing move: " + move + " " + Calendar.getInstance().getTime());


		System.out.println("Moving Piece: " + Calendar.getInstance().getTime());
		if( gameServer.moveAvatar(move) ){

			System.out.println("Piece has been moved: " + Calendar.getInstance().getTime());

			setGameModified(true);
			System.out.println("Game has been modified: " + Calendar.getInstance().getTime());
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
						retrieveObject(new NetworkObject(IPAddress, new ChatMessage("~Admin", text, chatMessageColor, true)));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void newClientConnection(ClientThread cl) {

		// Tell everyone the new client has joined the server
		sendToAllClients(new ChatMessage("~Admin",cl.getPlayerName() + " has Connected.", chatMessageColor, true),cl);

		// Display welcome message for the new client
		cl.sendData(new ChatMessage("","Welcome Message::" + "\nType /help for commands", chatMessageColor, true));

		// Tell console this client connected
		System.out.println(cl.getPlayerName() + " has Connected.");

		// Set new players current room
		Room currentRoom = gameServer.addPlayer(cl.getPlayerName());
		System.out.println("currentRoom: " + currentRoom);

		// Send the soom back to the client
		if( currentRoom != null ){
			cl.sendData(new RoomUpdate(currentRoom));
			System.out.println("SEND NEW ROOM!");
		}

	}

	@Override
	public void clientRejoins(ClientThread cl) {

		// TODO THIS DOES NOT WORK
		// TODO THIS DOES NOT WORK
		// TODO THIS DOES NOT WORK

		// Tell everyone the new client has joined the server
		sendToAllClients(new ChatMessage("~Admin",cl.getPlayerName() + " has Reconnected.", chatMessageColor, true),cl);

		// Tell console this client connected
		System.out.println(cl.getPlayerName() + " has Reconnected.");

		Room currentRoom = gameServer.getRoom(cl.getPlayerName());
		System.out.println("currentRoom: " + currentRoom);

		// Send the soom back to the client
		if( currentRoom != null ){
			cl.sendData(new RoomUpdate(currentRoom));
		}
	}

	public boolean isGameModified() {

		synchronized(gameModifiedLock){
			return gameModified;
		}
	}

	public void setGameModified(boolean gameModified) {
		synchronized(gameModifiedLock){
			this.gameModified = gameModified;
		}
	}

	public static void main(String[] args) {
		new GameServer();
	}
}
