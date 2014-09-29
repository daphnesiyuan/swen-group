package networking;

import gameLogic.Room;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class GameClient extends Client {

	// Client side of the game
	private Object roomLock = new Object();
	private Room clientRoom = null;

	private boolean modified = false;
	private Object modifiedLock = new Object();

	private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();


	// Which player the gameclient is controlling
	private Player player;
	private Color chatMessageColor = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

	public GameClient(String playerName){
		player = new Player(playerName);
	}

	/**
	 * Returns the client side version of the room that the player is currently in
	 * @return Room containing the player and al lthe rooms contents
	 */
	public synchronized Room getRoom(){
		synchronized(roomLock){
			return clientRoom;
		}
	}

	/**
	 * Changes the name of the client to be displayed through-out the game
	 * @param name Changes the players name and alerts the server as well
	 */
	public void setName(String name){

		// Tell the server to update this clients name as well!
		try {
			// Try and change it on the servers
			sendChatMessageToServer("/name " + name);
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
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendMoveToServer(String interaction) throws IOException{

		// Send data to the server
		return super.sendData(new Move(player, interaction));
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessageToServer(String message) throws IOException{

		// Create a new chatMessage
		ChatMessage chat = new ChatMessage(player.getName(), message, chatMessageColor);
		return sendChatMessageToServer(chat);
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessageToServer(ChatMessage chat) throws IOException{

		// Client side commands
		if( chat.message.equals("/clear") ){
			chatHistory.clear();
			setModified(true);
			return true;
		}

		// Record client sided message
		chatHistory.add(chat);
		setModified(true);

		// Send data to the server
		return super.sendData(chat);
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {

		// Check what type of data we were sent
		if( data.getData() instanceof ChatMessage ){
			// Sent a chat Message
			retrievedChatMessage(data.getIPAddress(), (ChatMessage)data.getData());
		}
		else if( data.getData() instanceof RoomUpdate ){
			// We were sent a move
			retrievedUpdatedRoom((RoomUpdate)data.getData());
		}
	}

	/**
	 * Process the supplied chat message sent from the server to this client.
	 * @param sendersIPAddress IP of the senders IP Address
	 * @param chatMessage Message that was sent from the given IP
	 */
	public synchronized void retrievedChatMessage(String sendersIPAddress, ChatMessage chatMessage){

		// Check for commands
		if( sendersIPAddress.equals(IPAddress) ){
			Scanner scan = new Scanner(chatMessage.message);
			if( scan.hasNext("/name") ){
				scan.next();

				// Check if we REALLY are assigning our name
				if( scan.hasNext() ){
					player.setName(scan.next());
				}
			}
			// Check for a ping
			else if( scan.hasNext("/ping all") ){
				try {

					sendChatMessageToServer("/ping all");
				} catch (IOException e) {}
			}
			else if( scan.hasNext("/chatcolor") ){
				scan.next();

				int r,g,b;

				if( !scan.hasNextInt() ){
					return;
				}

				r = scan.nextInt();

				if( !scan.hasNextInt() ){
					return;
				}

				g = scan.nextInt();

				if( !scan.hasNextInt() ){
					return;
				}

				b = scan.nextInt();

				chatMessageColor = new Color(r,g,b);
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
			setModified(true);
		}


		// Record when we last updated
		setModified(true);
	}

	/**
	 * Process the supplied chat message sent from the server to this client.
	 * @param sendersIPAddress IP of the senders IP Address
	 * @param chatMessage Message that was sent from the given IP
	 */
	public synchronized void retrievedUpdatedRoom(RoomUpdate room){

		//TODO Perform an action on the current room
		synchronized(roomLock){
			clientRoom = room.updatedRoom;

			// Record when we last updated
			setModified(true);
		}
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public ArrayList<ChatMessage> getChatHistory(){
		ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
		history.addAll(chatHistory);
		return history;
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public synchronized void appendWarningMessage(String warning){

		// Save the message
		chatHistory.add(new ChatMessage("WARNING", warning, chatMessageColor, true));
		setModified(true);
	}

	@Override
	public void successfullyConnected(String playerName) {

		// Change the name of the player
		player.setName(playerName);
	}

	/**
	 * Checks if the current client has had anything modified since the last refresh. Determines if the listener of this client needs to update or not.
	 * @return True if something has changed in the chat
	 */
	public boolean isModified() {
		synchronized (modifiedLock){
			return modified;
		}
	}

	/**
	 * Sets the current state of the clients modifications status to what's given.
	 * @param modified
	 */
	public void setModified(boolean modified) {
		synchronized (modifiedLock){
			this.modified = modified;
		}
	}

	/**
	 * @return the chatMessageColor
	 */
	public Color getChatMessageColor() {
		return chatMessageColor;
	}

	/**
	 * @param chatMessageColor the chatMessageColor to set
	 */
	public void setChatMessageColor(Color chatMessageColor) {
		this.chatMessageColor = chatMessageColor;
	}



}
