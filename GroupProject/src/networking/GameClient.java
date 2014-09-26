package networking;

import gameLogic.location.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class GameClient extends Client {

	// Client side of the game
	private Object roomLock = new Object();
	private Room room = null;

	private boolean modified = false;
	private Object modifiedLock = new Object();

	private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();


	// Individual Client Fields
	private String clientName;


	/**
	 * Returns the client side version of the room that the player is currently in
	 * @return Room containing the player and al lthe rooms contents
	 */
	public synchronized Room getRoom(){
		synchronized(roomLock){
			return room;
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
			sendChatMessage("/name " + name);
			this.clientName = name;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the name placed as this client
	 * @return
	 */
	public String getName() {
		return clientName;
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendPerformedAction(String action) throws IOException{

		// Create a new chatMessage
		Move chat = new Move(action);

		// Send data to the server
		return super.sendData(chat);
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessage(String message) throws IOException{

		// Create a new chatMessage
		ChatMessage chat = new ChatMessage(clientName, message);

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
		else if( data.getData() instanceof Move ){
			// We were sent a move
			retrievedMove(data.getIPAddress(), (Move)data.getData());
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
					this.clientName = scan.next();
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
	public synchronized void retrievedMove(String sendersIPAddress, Move move){

		//TODO Perform an action on the current room


		// Record when we last updated
		//setModified(true);
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public String getChatHistory(){
		String history = "";
		for (int i = 0; i < chatHistory.size(); i++) {
			ChatMessage message = chatHistory.get(i);
			history = history + message + "\n";

		}

		return history;
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public synchronized void appendWarningMessage(String warning){

		// Save the message
		chatHistory.add(new ChatMessage("WARNING",warning,true));
		setModified(true);
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

}
