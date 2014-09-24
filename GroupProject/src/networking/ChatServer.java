package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ChatServer extends Server {

	private String chatHistory = "";

	public ChatServer() {
		super();

		// Server listens for input directly to servers terminal Thread
		Thread serverTextBox = new Thread(new ServerTextListener());
		serverTextBox.start();
	}

	@Override
	public void retrieveObject(NetworkObject data) {

		if( processCommand((String)data.getData(), data) ){
			return;
		}

		// Save the message
		chatHistory = chatHistory + data + "\n";

		// Display for the server in console
		System.out.println(data);

		// Send it to all our clients
		sendToAllClients((String)data.getData());
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
	private synchronized void sendHistoryToClient(String clientIP) {

		sendToClient(clientIP, chatHistory);
	}

	/**
	 * The method that is run once the server enters a message in console
	 *
	 * @param message
	 */
	private synchronized void processServerMessage(String message) {

		// Process to everyone
		retrieveObject(createNetworkObject("~Admin", message));
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
				return false;
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
			}
			else if( command.equals("/help")){
				return parseHelp(scan,data);
			}

			// Unknown command
			return false;
		}

		private boolean parseClose(Scanner scan, NetworkObject data) {

			// Only admins can close the server
			if( !isAdmin(data.getIPAddress()) ){
				return false;
			}

			stopServer();
			return true;
		}

		private boolean parseName(Scanner scan, NetworkObject data) {

			// Check for name
			if (!scan.hasNext()) {
				return false;
			}

			// Get next command that SHOULD be a name
			ClientThread client = getClientFromIP(data.getIPAddress());

			// Check for failed client check
			if (client == null) {
				return false;
			}

			// Check for next name
			if (!scan.hasNext()) {
				return false;
			}

			// Get the name they want to assign their name to
			String newName = scan.next();

			// set "name" name worked
			retrieveObject(createNetworkObject(client.getName()
					+ " has changed their name to " + newName));

			client.setName(newName);

			return true;
		}

		private boolean parseGet(Scanner scan, NetworkObject data) {

			// Check for name
			if (!scan.hasNext()) {
				return false;
			}

			// What do we want to get?
			String token = scan.next();

			// Check for failed client check
			if (token.equals("history")) {

				// Send history back to the client
				sendHistoryToClient(data.getIPAddress());

				return true;
			}

			return false;
		}

		private boolean parsePing(Scanner scan, NetworkObject data) {

			// Send history back to the client
			long delay = pinged(data);

			System.out.println(data.getName() + " pinged the server at " + delay + "ms");

			return true;
		}

		private boolean parseAdmins(Scanner scan, NetworkObject data) {

			// Send history back to the client
			String adminList = "List of Admins:\n";
			for(String admin : getAdmins()){
				adminList = adminList + admin + "\n";
			}

			sendToClient(data.getIPAddress(), adminList);

			return true;
		}

		private boolean parseHelp(Scanner scan, NetworkObject data) {

			// Send history back to the client
			String adminList = "\nList of Possible Commands:\n"
					+ "/ping -> Checks how fast your connection currently is\n"
					+ "/get history -> Sends back the entire chat history\n"
					+ "/admins -> lists the IP's of the admins\n"
					+ "/name 'string' -> changes your name\n\n"
					+ "- ADMIN COMMANDS -\n"
					+ "/close -> closes the server";


			sendToClient(data.getIPAddress(), adminList);

			return true;
		}

		/**
		 * Get the clientThread in this server with the matching name if there
		 * is one
		 *
		 * @param name
		 *            Of the ClientThread we want to find
		 * @return ClientThread relating to the name or null if not found
		 */
		private ClientThread parseClient(String name) {
			return getClientFromName(name);
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
