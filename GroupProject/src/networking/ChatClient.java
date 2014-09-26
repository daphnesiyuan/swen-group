package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class ChatClient extends Client {

	private String clientName;

	private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();

	private Calendar lastUpdate = null;


	/**
	 * Changes the name of the client to be displayed through-out the game
	 * @param name
	 */
	public void setName(String name){

		// Tell the server to update this clients name as well!
		try {
			// Try and change it on the servers
			sendData(new ChatMessage("/name " + name));
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

		ChatMessage chat = new ChatMessage(clientName, message);
		chatHistory.add(chat);

		return super.sendData(chat);
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {

		ChatMessage chatMessage = (ChatMessage) data.getData();

		// Check for commands
		if( data.getIPAddress().equals(IPAddress) ){
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
		}


		// Record when we last updated
		lastUpdate = Calendar.getInstance();
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
	}

	/**
	 * Returns the time we last got an update from the server
	 * @return Calendar of when last update was
	 */
	public Calendar lastUpdate(){
		return lastUpdate;
	}

}
