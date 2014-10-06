package networking;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Stack;

import networking.Server.ClientThread;

public class ChatServer extends Server {


	protected Color chatMessageColor = Color.black;
	protected Stack<ChatMessage> chatHistory = new Stack<ChatMessage>();

	@Override
	public void retrieveObject(NetworkObject data) {

		// Can we process a chat message?
		if( data.getData() instanceof ChatMessage ){
			ChatMessage cm = (ChatMessage)data.getData();

			// Process a command if we wrote one and display the message
			if( !processCommand(cm.message, data) ){

				// Send the data back to the client
				ClientThread sender = getClientFromIP(data.getIPAddress());
				if( sender != null ){
					sender.sendData(data);
				}

				// Don't do anything else
				return;
			}

			// Save the message
			chatHistory.add(cm);

			// Send it to all our clients
			sendToAllClients(data);
		}

	}

	/**
	 * Sends the chat history to the client that send the network object
	 * Size indicates how many of the most recent messages to claim
	 * @param clientIP Who we should send the history to
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	private synchronized void sendHistoryToClient(String clientIP, int size) {

		// Make sure we don't get a size greater than the list
		size = Math.min(size, chatHistory.size());

		Stack<ChatMessage> history = new Stack<ChatMessage>();
		for (int i = chatHistory.size()-1; i > (chatHistory.size() - size); i--) {
			history.add(chatHistory.get(i));
		}

		// Send a new ArrayList of the chat messages to the client
		sendToClient(clientIP, new ChatHistory(history,true));
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
	 * Parser for all the commands that the server processes from ChatMessages sent from clients
	 * @author veugeljame
	 *
	 */
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
			} else if( command.equals("/help")){
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
			String newName = scan.nextLine().trim();

			// set "name" name worked
			retrieveObject(new NetworkObject(IPAddress, new ChatMessage(client.getPlayerName()
					+ " has changed their name to " + newName,chatMessageColor, true)));

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
				return true;
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
					+ "/get history 'number' -> Sends back the chat history up to 'number' of most recent messages\n"
					+ "/admins -> lists the IP's of the admins\n"
					+ "/chatcolor r g b -> changes the color of your chat messages\n"
					+ "/clear -> clears all messages off the screen\n"
					+ "/disconnect -> disconnects from the server\n"
					+ "/reconnect -> reconnects to the previous server was was successfully connected\n"
					+ "/name 'string' -> changes your name\n\n"
					+ "- ADMIN COMMANDS -\n"
					+ "/close -> closes the server";


			sendToClient(data.getIPAddress(), new ChatMessage("~Admin",commandList, chatMessageColor, true));

			return true;
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
	}

	@Override
	public void clientRejoins(ClientThread cl) {

		// Tell everyone the new client has joined the server
		sendToAllClients(new ChatMessage("~Admin",cl.getPlayerName() + " has Reconnected.", chatMessageColor, true),cl);

		// Tell console this client connected
		System.out.println(cl.getPlayerName() + " has Reconnected.");
	}
}
