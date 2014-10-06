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

public class ChatClient extends Client {

	// Which player the gameclient is controlling
	protected Player player;

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
					//TODO GET PINGING WORKING
					//TODO GET PINGING WORKING
					/*try {
						sendData(chatMessage);
					} catch (IOException e) {}*/
					return;
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

		// Tell the server to update this clients name as well!
		try {
			// Try and change it on the servers
			sendData(new ChatMessage("/name " + name, chatMessageColor));
			player.setName(name);
		} catch (IOException e) {
			e.printStackTrace();
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

		// Client side commands
		if( chat.message.equals("/clear") ){
			clearChatHistory();
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
	public boolean connect(String IPAddress, int port) throws UnknownHostException, IOException{
		return connect(IPAddress, player.getName(), port);
	}

	/**
	 * Attempts to connect to the given Server
	 * @param IPAddress IPAddress of the connection to connect as
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(ChatServer server) throws UnknownHostException, IOException{
		return connect(server, player.getName());
	}

	/**
	 * Checks the given chat for a possible client command sent from the client.
	 * @return True if a command was detected and processed successfully
	 */
	public boolean checkClientCommands(ChatMessage chat){
		Scanner scan = new Scanner(chat.message);

		// Client side commands
		if( scan.hasNext("/clear") ){
			chatHistory.clear();
			return true;
		}
		else if( scan.hasNext("/disconnect") ){
			chat.acknowledged = true; // Chat worked
			chatHistory.add(chat);	// Record message
			disconnect(); // Disconnect from server
			return true;
		}
		else if( scan.hasNext("/reconnect") ){
			chat.acknowledged = true; // Chat worked
			chatHistory.add(chat);	// Record message

			// Attempt to reconnect
			if(	reconnect(getName()) ){
				appendWarningMessage("Connected successfully to " + connectedIP + " : " + connectedPort);
				return true;
			}
		}
		else if( scan.hasNext("/name") ){
			scan.next();

			// Check if we REALLY are assigning our name
			if( scan.hasNext() ){
				setName( scan.next() );
			}
			return true;
		}
		else if( scan.hasNext("/ping") ){
			scan.next();

			if( scan.hasNext("everyone") ){
				return false;
			}
		}
		else if( scan.hasNext("/chatcolor") ){
			scan.next();

			if( scan.hasNextInt() ){
				int r,g,b;

				if( !scan.hasNextInt() ){
					appendWarningMessage("Missing Red EG: /chatColor 0 64 128");
					return false;
				}

				r = scan.nextInt();

				if( !scan.hasNextInt() ){
					appendWarningMessage("Missing Green EG: /chatColor 0 64 128");
					return false;
				}

				g = scan.nextInt();

				if( !scan.hasNextInt() ){
					appendWarningMessage("Missing Blue EG: /chatColor 0 64 128");
					return false;
				}

				b = scan.nextInt();

				chatMessageColor = new Color(r,g,b);
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
		}

		return false;
	}

	/**
	 * Returns a new arrayList containing all the chatMessages up to "size" back
	 * Size indicates how many of the most recent messages to claim
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	public Stack<ChatMessage> getChatHistory(int size) {

		size = Math.min(size, chatHistory.size());

		Stack<ChatMessage> history = new Stack<ChatMessage>();
		for (int i = chatHistory.size()-1; i > (chatHistory.size() - size); i--) {
			history.add(chatHistory.get(i));
		}

		// Send a new ArrayList of the chat messages to the client
		return history;
	}

	/**
	 * Returns a new arrayList containing all the chatMessages up to "size" back
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	public Stack<ChatMessage> getChatHistory() {
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
}
