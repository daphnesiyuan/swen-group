package networking;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class ChatClient extends Client {

	private String clientName;

	private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();

	private Object modifiedLock = new Object();
	private boolean modified = false;
	private Color chatMessageColor = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));


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
	public boolean sendData(String message) throws IOException{

		ChatMessage chat = new ChatMessage(clientName, message,chatMessageColor);

		// Client side commands
		if( chat.message.equals("/clear") ){
			chatHistory.clear();
			setModified(true);
			return true;
		}

		// Record client sided message
		chatHistory.add(chat);
		setModified(true);

		return super.sendData(chat);
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {

		ChatMessage chatMessage = (ChatMessage) data.getData();

		// Check for commands
		Scanner scan = new Scanner(chatMessage.message);
		if( data.getIPAddress().equals(IPAddress) ){
			if( scan.hasNext("/name") ){
				scan.next();

				// Check if we REALLY are assigning our name
				if( scan.hasNext() ){
					this.clientName = scan.next();
				}
			}
			else if( scan.hasNext("/chatcolor") ){
				scan.next();

				if( scan.hasNextInt() ){
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
				else if( scan.hasNext() ){

					// Using a color name
					Color color = Color.getColor(scan.next());
					if( color != null ){
						chatMessageColor = color;
					}
				}
			}
		}
		else if( !data.getIPAddress().equals(IPAddress) ){


			// Ping command is received
			if( scan.hasNext("/ping") ){ scan.next();
				if( scan.hasNext(clientName) ){

					// Someone Pinged me
					long delay = Math.abs(Calendar.getInstance().getTimeInMillis() - data.getCalendar().getTimeInMillis());

					try {
						sendData(new ChatMessage(chatMessage.sendersName + " pinged " + clientName + " at " + delay + "ms",chatMessage.color,true));
					} catch (IOException e) {}
				}
				else if( scan.hasNext("everyone") ){
					try {
						sendData(chatMessage);
					} catch (IOException e) {}
					return;
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
	 * Returns a new arrayList containing all the chatMessages up to "size" back
	 * Size indicates how many of the most recent messages to claim
	 * @param size how many messages we should get from our history from the furthest back to the last message
	 */
	public ArrayList<ChatMessage> getChatHistory(int size) {

		// Make sure we don't get a size greater than the list
		size = Math.min(chatHistory.size(),size);

		// TODO Synchronise chatHistory with a lock
		ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
		for (int i = (chatHistory.size()-1) - size; i < chatHistory.size(); i++) {
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

		// TODO Synchronise chatHistory with a lock
		ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
		for (int i = 0; i < chatHistory.size(); i++) {
			history.add(chatHistory.get(i));
		}

		// Send a new ArrayList of the chat messages to the client
		return history;
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public synchronized void appendWarningMessage(String warning){

		// Save the message
		chatHistory.add(new ChatMessage("WARNING",warning, Color.black, true));
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

	@Override
	public void successfullyConnected(String playerName) {
		// TODO Auto-generated method stub

	}

}
