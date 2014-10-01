package networking;

import gameLogic.Game;
import gameLogic.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 *Game server class of which handles all the processing of the game on a server side level.
 *If a change is made to any clients information, then it is changed on all clients levels.
 * @author veugeljame
 *
 */
public class GameServer extends ChatServer {

	// Game that all players are playing off
	private Object serverLock;
	private Game gameServer;

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
						//System.out.println("Running");
						updateAllClients();

						Thread.sleep(30);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		refreshThread.start();
	}

	/**
	 * Updates all clients with a new room according to the state of the game logic
	 */
	private void updateAllClients(){
		//System.out.println("Updating.......");

		for (int i = 0; i < clients.size(); i++) {
			//System.out.println(clients.get(i).getPlayerName() + " getting Room");

			// Get each of our clients, and the room they are in
			ClientThread client = clients.get(i);
			Room room = gameServer.getRoom(client.player.getName());
			//System.out.println("Room: " + room);

			// Send the new room to the player
			client.sendData(new RoomUpdate(room));
		}
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {
		super.retrieveObject(data);

		// Determine what to do with each of the different types of objects sent from clients
		if( data.getData() instanceof Move ){

			// A move performed by a client
			processMove((Move)data.getData(), data);
		}

	}

	/**
	 *
	 * @param move Move wanting to be performed by a client
	 * @param data NetworkObject sent through the network
	 */
	private synchronized void processMove(Move move, NetworkObject data){

		synchronized(serverLock){
			gameServer.moveAvatar(move);
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

		// Tell everyone the new client has joined the server
		sendToAllClients(new ChatMessage("~Admin",cl.getPlayerName() + " has Reconnected.", chatMessageColor, true),cl);

		// Tell console this client connected
		System.out.println(cl.getPlayerName() + " has Reconnected.");
	}

	public static void main(String[] args) {
		new GameServer();
	}
}
