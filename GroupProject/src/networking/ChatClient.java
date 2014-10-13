package networking;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JComponent;

import networking.Client.InputWaiter;
import networking.Server.ClientThread;

public class ChatClient extends Client {

	// Clients sides version of the current chat history
	private Stack<ChatMessage> chatHistory = new Stack<ChatMessage>();

	// Color of the clients messages
	private Color chatMessageColor = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

	// Where to draw
	private JComponent clientImage;

	/**
	 * Creates a new GameClient to connect to a server
	 * @param playerName Name of the client
	 */
	public ChatClient(String playerName, JComponent clientImage){
		player = new Player(playerName);
		this.clientImage = clientImage;

		// Draws consistantly to display the new messages
		Thread drawThread = new Thread(){
			@Override
			public void run(){

				// Draw every 30ms
				while( true ){

					// Tell the component to repaint
					repaintImage();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};
		drawThread.start();
	}

	/**
	 * Assigns the component to call when anything ischanged
	 * @param component
	 */
	public void setPaintComponent(JComponent component){
		this.clientImage = component;
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {

		if( data.getData() instanceof ChatHistory ){
			chatHistory.addAll(((ChatHistory)data.getData()).history);
			return;
		}
		else if( data.getData() instanceof ChatMessage ){
			ChatMessage chatMessage = (ChatMessage) data.getData();

			// Check for commands
			Scanner scan = new Scanner(chatMessage.message);

			// Ping command is received
			if( scan.hasNext("/ping") ){ scan.next();
				if( scan.hasNext(getName()) ){

					// Someone Pinged me
					long delay = (System.currentTimeMillis() - data.getTimeInMillis());

					try {
						sendData(new ChatMessage(chatMessage.sendersName + " pinged " + getName() + " at " + delay + "ms",chatMessage.color,true));
					} catch (IOException e) {}
				}
				else if( scan.hasNext("everyone") ){
					return;
				}
			}
			else if( scan.hasNext("/name") ){

				if( data.getIPAddress().equals(getIPAddress()) ){

					scan.next(); // /name
					if( scan.hasNext() ){

						// Trim the new name, removing white spaces
						String newName = scan.nextLine().trim();
						player.setName(newName);
					}
				}
			}

			// Check if we have just gotten an acknowledgement
			if( chatHistory.contains(chatMessage) ){

				// Acknowledge the message
				chatHistory.get(chatHistory.indexOf(chatMessage)).acknowledged = true;
			}
			else{

				// Save the message
				chatHistory.add(chatMessage);
			}
		}
	}

	/**
	 * Changes the name of the client to be displayed through-out the game
	 * @param name
	 */
	public void setName(String name){
		name.trim(); // Remove spaces

		// Only change name if we are not connectged, otherwise we need permission from the server
		if( isConnected() ){

			// Not fast enough
			// Not fast enough so now removed
			// Not fast enough
			System.out.println("You must be connected to change your name!");

			// Tell the server to update this clients name as well!

			/*try {
				// Try and change it on the servers
				sendData(new ChatMessage("/name " + name, chatMessageColor));
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		else{
			// Set name directly
			player.setName(name);
		}
	}

	/**
	 * Returns the name placed as this client
	 * @return
	 */
	public String getName() {
		return player.getName();
	}

	/**
	 * Returns the player accociated with this client
	 * @return Player object containing name and IP
	 */
	public Player getPlayer(){
		return player;
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessageToServer(String message) throws IOException{

		// Create a new chatMessage
		ChatMessage chat = new ChatMessage(player.getName(), message, getChatMessageColor());
		return sendChatMessageToServer(chat);
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessageToServer(ChatMessage chat) throws IOException{

		NetworkObject data = new NetworkObject(getIPAddress(), chat);

		// Client side commands
		if( !new CommandParser().parseCommand(new Scanner(chat.message), data) ){
			return true;
		}

		// Send data to the server
		return sendData(chat);
	}

	/**
	 * Attempts to connect to the given IPAddress and port number of the server using the Clients name
	 * @param IPAddress IPAddress of the connection to connect as
	 * @param port Default: 32768
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(String IPAddress) throws UnknownHostException, IOException{
		return connect(IPAddress, player.getName(), getPort());
	}

	/**
	 * Attempts to connect to the given Server
	 * @param IPAddress IPAddress of the connection to connect as
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(ChatServer server) throws UnknownHostException, IOException{
		return connect(server.getIPAddress(), this.getName(), server.getPort());
	}

	/**
	 * Returns a new arrayList containing all the chatMessages up to "size" back
	 * Size indicates how many of the most recent messages to claim
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	public ArrayList<ChatMessage> getChatHistory(int size) {

		size = Math.min(size, chatHistory.size());

		ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
		for (int i = chatHistory.size()-size; i < chatHistory.size(); i++) {
			history.add(chatHistory.get(i));
		}

		// Send a new ArrayList of the chat messages to the client
		return history;
	}

	/**
	 * Returns a new arrayList containing all the chatMessages up to "size" back
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	public ArrayList<ChatMessage> getChatHistory() {
		// Send a get ArrayList of the chat messages to the client
		return getChatHistory(chatHistory.size());
	}

	/**
	 * Clears all messages from the chat history
	 */
	protected void clearChatHistory() {
		chatHistory.clear();
	}

	/**
	 * Adds the given message to our current history
	 * @param chat Chat Message to add to our history
	 */
	protected void addChatMessage(ChatMessage chat) {
		chatHistory.add(chat);
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public synchronized void appendWarningMessage(String warning){

		// Save the message
		chatHistory.add(new ChatMessage("WARNING",warning, Color.black, true));
	}

	/**
	 * @return the chatMessageColor
	 */
	public Color getChatMessageColor() {
		return chatMessageColor;
	}

	public void repaintImage(){

		if( clientImage != null ){
			clientImage.repaint();
		}
	}

	/**
	 * @param chatMessageColor the chatMessageColor to set
	 */
	public void setChatMessageColor(Color chatMessageColor) {
		this.chatMessageColor = chatMessageColor;
	}

	@Override
	public void successfullyConnected(String playerName) {

		// Change the name of the player
		player.setName(playerName);
	}

	/**
	 * Parser for all the commands that the server processes from ChatMessages sent from clients
	 * @author veugeljame
	 *
	 */
	private class CommandParser {

		/**
		 * Process the command in the scanner and see if it contains a command that the server can work with.
		 * @param scan Scanner attached to a string/chat message
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		public boolean parseCommand(Scanner scan, NetworkObject data) {

			if (!scan.hasNext()) {
				return true;
			}

			String command = scan.next();

			// Set something
			if (command.equals("/name")) {
				return parseName(scan, data);
			} else if (command.equals("/clear")){
				return parseClear(scan, data);
			} else if (command.equals("/reconnect")){
				return parseReconnect(scan, data);
			} else if (command.equals("/disconnect")){
				return parseDisconnect(scan, data);
			} else if (command.equals("/ping")) {
				return parsePing(scan, data);
			} else if (command.equals("/ip")) {
				return parseIP(scan, data);
			}else if (command.equals("/chatcolor")){
				return parseChatColor(scan,data);
			}
			return true;
		}

		/**
		 * Reconnects the client to the last server successfully joined
		 * @param scan Scanner attached to a string/chat message
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parseReconnect(Scanner scan, NetworkObject data) {
			((ChatMessage)data.getData()).acknowledged = true; // Chat worked
			chatHistory.add((ChatMessage)data.getData());	// Record message

			// Attempt to reconnect
			if(	reconnect(getName()) ){
				appendWarningMessage("Connected successfully to " + connectedIP + " : " + connectedPort);
				return true;
			}
			return true;
		}

		/**
		 * Disconnects the client from the server
		 * @param scan Scanner attached to a string/chat message
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parseDisconnect(Scanner scan, NetworkObject data) {
			((ChatMessage)data.getData()).acknowledged = true; // Chat worked
			chatHistory.add((ChatMessage)data.getData());	// Record message
			disconnect(); // Disconnect from server
			return true;
		}

		/**
		 * Clears the chathistory off the clients
		 * @param scan Scanner attached to a string/chat message
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parseClear(Scanner scan, NetworkObject data) {
			chatHistory.clear();
			return true;
		}

		/**
		 * Changes the name of the client that sent the data
		 * @param scan Scanner attached to a string/chat message with the next name to be used with the client
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parseName(Scanner scan, NetworkObject data) {

			// Check if we REALLY are assigning our name
			if( scan.hasNext() && data.getIPAddress().equals(getIPAddress()) ){
				setName( scan.next() );
			}
			return true;
		}

		/**
		 * Changes the color of the clients chat messages
		 * @param scan Scanner attached to a string/chat message with R G B as 3 integer extensions
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parseChatColor(Scanner scan, NetworkObject data){

			if( scan.hasNextInt() ){
				int red,green,blue;

				if( !scan.hasNextInt() ){
					appendWarningMessage("Missing Red EG: /chatColor 0 64 128");
					return false;
				}

				red = scan.nextInt();

				if( !scan.hasNextInt() ){
					appendWarningMessage("Missing Green EG: /chatColor 0 64 128");
					return false;
				}

				green = scan.nextInt();

				if( !scan.hasNextInt() ){
					appendWarningMessage("Missing Blue EG: /chatColor 0 64 128");
					return false;
				}

				blue = scan.nextInt();

				chatMessageColor = new Color(red,green,blue);
				return true;
			}
			else if( scan.hasNext() ){

				// Using a color name
				Color color = Color.getColor(scan.next());
				if( color != null ){
					chatMessageColor = color;
					return true;
				}
			}
			return true;
		}

		/**
		 * Client is pinging, so find out what we should ping
		 * @param scan Scanner attached to a string/chat message possible extension of everyone or another clients name/IP
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parsePing(Scanner scan, NetworkObject data) {
			if( scan.hasNext("everyone") ){
				return false;
			}
			return true;
		}

		/**
		 * Displays the clients Ping on their screen
		 * @param scan Scanner attached to a string/chat message
		 * @param data Information sent with the text
		 * @return True if the the text should be displayed and sent to it's clients
		 */
		private boolean parseIP(Scanner scan, NetworkObject data) {
			chatHistory.add(new ChatMessage("~Admin", "Your IP: " + getIPAddress(), Color.black, true));	// Record message
			return false;
		}
	}
}
