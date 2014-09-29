package networking;

import gameLogic.gameState.Game;
import gameLogic.location.Room;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author veugeljame
 *
 */
public class GameServer extends Server {

	// Complete chat history sent through the network
	private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
	private Color chatMessageColor = Color.black;

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

					updateAllClients();

					Thread.sleep(30);
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

		for (int i = 0; i < clients.size(); i++) {

			// Get each of our clients, and the room they are in
			ClientThread client = clients.get(i);
			Room room = gameServer.getRoom(client.player.getName());

			// Send the new room to the player
			client.sendData(new RoomUpdate(room));
		}
	}

	@Override
	public void retrieveObject(NetworkObject data) {

		// Determine what to do with each of the different types of objects sent from clients
		if( data.getData() instanceof ChatMessage ){

			// ChatMessage sent from a client
			processChatMessage((ChatMessage)data.getData(), data);
		}
		else if( data.getData() instanceof Move ){

			// A move performed by a client
			processMove((Move)data.getData(), data);
		}

	}

	/**
	 *
	 * @param chatMessage the message sent FROM a client
	 * @param data original NetworkObject sent through the network
	 */
	public void processChatMessage(ChatMessage chatMessage, NetworkObject data){

		// Process a command if we wrote one and display the message
		if( !processCommand(chatMessage.message, data) ){

			// Send the data back to the client
			ClientThread sender = getClientFromIP(data.getIPAddress());
			if( sender != null ){
				sender.sendData(data);
			}

			// Don't do anything else
			return;
		}

		// Save the message
		chatHistory.add(chatMessage);

		// Display for the server in console
		System.out.println(data);

		// Send it to all our clients
		sendToAllClients(data);
	}

	/**
	 *
	 * @param move Move wanting to be performed by a client
	 * @param data NetworkObject sent through the network
	 */
	public void processMove(Move move, NetworkObject data){

		synchronized(serverLock){
			gameServer.moveCharacter(move);
		}
	}

	/**
	 * Processes the message in the string provided and returns a boolean if it
	 * were processed or not
	 *
	 * @param command
	 *            Command to process
	 * @param data
	 *            packet that was sent to the server
	 * @return true or false if the command was processed
	 */
	private synchronized boolean processCommand(String command,NetworkObject data) {

		Scanner scan = new Scanner(command);
		return new CommandParser().parseCommand(scan, data);
	}

	/**
	 * Sends the chat history to the client that send the network object
	 *
	 * @param clientIP
	 *            IP of who wants the history sent to them
	 */

	/**
	 * Sends the chat history to the client that send the network object
	 * Size indicates how many of the most recent messages to claim
	 * @param clientIP Who we should send the history to
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	private synchronized void sendHistoryToClient(String clientIP, int size) {

		// Make sure we don't get a size greater than the list
		size = Math.min(chatHistory.size(),size);

		// TODO Synchronise chatHistory with a lock
		ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
		for (int i = (chatHistory.size()-1) - size; i < chatHistory.size(); i++) {
			history.add(chatHistory.get(i));
		}

		// Send a new ArrayList of the chat messages to the client
		sendToClient(clientIP, new ChatHistory(history,true));
	}

	/**
	 * The method that is run once the server enters a message in console
	 *
	 * @param message
	 */
	private synchronized void processServerMessage(String message) {

		// Process to everyone
		retrieveObject(new NetworkObject(IPAddress, new ChatMessage("~Admin", message, chatMessageColor, true)));
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

						processServerMessage(text);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class CommandParser {

		public boolean parseCommand(Scanner scan, NetworkObject data) {

			if (!scan.hasNext()) {
				return true;
			}

			String command = scan.next();

			// Set something
			if (command.equals("/name")) {
				return parseName(scan, data);
			} else if (command.equals("/get")) {
				return parseGet(scan, data);
			} else if (command.equals("/ping")) {
				return parsePing(scan, data);
			} else if (command.equals("/close")){
				return parseClose(scan,data);
			} else if (command.equals("/admins")){
				return parseAdmins(scan,data);
			}else if( command.equals("/chatcolor")){
				return parseChatColor(scan,data);
			}else if( command.equals("/help")){
				return parseHelp(scan,data);
			}

			// Unknown command
			return true;
		}

		private boolean parseClose(Scanner scan, NetworkObject data) {

			// Only admins can close the server
			if( !isAdmin(data.getIPAddress()) ){
				return true;
			}

			stopServer();
			return false;
		}

		private boolean parseName(Scanner scan, NetworkObject data) {

			// Check for name
			if (!scan.hasNext()) {
				return true;
			}

			// Get next command that SHOULD be a name
			ClientThread client = getClientFromIP(data.getIPAddress());

			// Check for failed client check
			if (client == null) {
				return true;
			}

			// Check for next name
			if (!scan.hasNext()) {
				return true;
			}

			// Get the name they want to assign their name to
			String newName = scan.nextLine();

			// set "name" name worked
			retrieveObject(new NetworkObject(IPAddress, new ChatMessage(client.getPlayerName()
					+ " has changed their name to " + newName,chatMessageColor, true)));

			// Update in game logic
			if( !gameServer.setPlayerName(client.getPlayerName(), newName) ){
				client.sendData(new ChatMessage("~Admin", "Failed to change your name in GameLogic from " + client.getPlayerName() + " to " + newName, chatMessageColor));
			}

			// Update servers name
			client.setPlayerName(newName);

			return true;
		}

		private boolean parseGet(Scanner scan, NetworkObject data) {

			// Check for name
			if (!scan.hasNext()) {
				return true;
			}

			// What do we want to get?
			String token = scan.next();

			// Check for failed client check
			if (token.equals("history")) {

				return parseHistory(scan,data);
			}

			return true;
		}

		private boolean parseHistory(Scanner scan, NetworkObject data){
			int size = chatHistory.size();

			// size of history if we were supplied one
			if( scan.hasNextInt() ){
				size = scan.nextInt();
			}

			// Never go out of bounds
			size = Math.min(chatHistory.size(),size);


			// Send history back to the client
			sendHistoryToClient(data.getIPAddress(), size);

			return false;
		}

		private boolean parsePing(Scanner scan, NetworkObject data) {

			// Send history back to the client
			long delay = ping(scan, data);

			// Display the ping if it's a valid ping
			if( delay != -1 ){
				System.out.println(((ChatMessage)data.getData()).sendersName + " pinged the server at " + delay + "ms");
			}

			return false;
		}

		private boolean parseAdmins(Scanner scan, NetworkObject data) {

			// Send history back to the client
			String adminList = "List of Admins:\n";
			for(String admin : getAdmins()){
				adminList = adminList + admin + "\n";
			}

			sendToClient(data.getIPAddress(), new ChatMessage("~Admin",adminList, chatMessageColor, true));

			return false;
		}

		private boolean parseHelp(Scanner scan, NetworkObject data) {

			// Send history back to the client
			String commandList = "\nList of Possible Commands:\n"
					+ "/ping -> Checks how fast your connection currently is\n"
					+ "/get history -> Sends back the entire chat history\n"
					+ "/get history 'number' -> Sends back the chat history up the the number of chats\n"
					+ "/admins -> lists the IP's of the admins\n"
					+ "/clear -> clears all messages off the screen\n"
					+ "/chatcolor\n"
					+ "/name 'string' -> changes your name\n\n"
					+ "- ADMIN COMMANDS -\n"
					+ "/close -> closes the server";


			sendToClient(data.getIPAddress(), new ChatMessage("~Admin",commandList, chatMessageColor, true));

			return true;
		}

		private Color parseColor(Scanner scan, NetworkObject data){
			if( scan.hasNext("/chatcolor") ){
				scan.next();

				if( scan.hasNextInt() ){
					int r,g,b;

					// Red
					if( !scan.hasNextInt() ){
						return null;
					}

					r = scan.nextInt();

					// Green
					if( !scan.hasNextInt() ){
						return null;
					}

					g = scan.nextInt();

					// Blue
					if( !scan.hasNextInt() ){
						return null;
					}

					b = scan.nextInt();

					return new Color(r,g,b);
				}
				else if( scan.hasNext() ){

					// Using a color name
					return Color.getColor(scan.next());
				}
			}
			return null;
		}

		private boolean parseChatColor(Scanner scan, NetworkObject data) {
			Color newColor = parseColor(scan,data);
			if( newColor != null ){
				sendToAllClients(new ChatMessage("~Admin", getClientFromIP(data.getIPAddress()) + " has changed the color of their chat messages", chatMessageColor, true));
				return true;
			}

			return false;
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}

	@Override
	public void newClientConnection(ClientThread cl) {

		// Tell everyone the new client has joined the server
		sendToAllClients(new ChatMessage("~Admin",cl.getPlayerName() + " has Connected.", chatMessageColor, true),cl);

		// Display welcome message for the new client
		cl.sendData(new ChatMessage("","Welcome Message::" + "\nType /help for commands", chatMessageColor, true));

		// Tell console this client connected
		System.out.println(cl.getPlayerName() + " has Connected.");
	}

	@Override
	public void clientRejoins(ClientThread cl) {

		// Tell everyone the new client has joined the server
		sendToAllClients(new ChatMessage("~Admin",cl.getPlayerName() + " has Reconnected.", chatMessageColor, true),cl);

		// Tell console this client connected
		System.out.println(cl.getPlayerName() + " has Reconnected.");
	}
}
