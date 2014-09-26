package networking;

import gameLogic.location.Room;

import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class GameClient extends Client {

	// Client side room
	private Room room = null;
	private Calendar lastUpdate = null;

	// Client Interactions
	private String clientName;
	private String chatHistory = "";



	/**
	 * Returns the client side version of the room that the player is currently in
	 * @return Room containing the player and al lthe rooms contents
	 */
	public Room getRoom(){
		return room;
	}

	/**
	 * Changes the name of the client to be displayed through-out the game
	 * @param name Changes the players name and alerts the server as well
	 */
	public void setName(String name){

		// Tell the server to update this clients name as well!
		try {
			// Try and change it on the servers
			sendData("/name " + name);
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
	public boolean sendData(String interaction) throws IOException{

		// Send a new move object through the network
		return super.sendData(new Move(interaction));
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {

		// Check for commands
		if( data.getIPAddress().equals(IPAddress) ){
			Scanner scan = new Scanner(data.getData().toString());
			if( scan.hasNext("/name") ){
				scan.next();

				// Check if we REALLY are assigning our name
				if( scan.hasNext() ){
					this.clientName = scan.next();
				}
			}
		}

		// Save the message
		appendMessage(data.toString());

		// Record when we last updated
		lastUpdate = Calendar.getInstance();
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public String getChatHistory(){
		return chatHistory;
	}

	/**
	 * Gets the chat history that has been sent to this client
	 * @return String containing chat history
	 */
	public synchronized void appendMessage(String message){

		// Save the message
		chatHistory = chatHistory + message + "\n";
	}

	/**
	 * Returns the time we last got an update from the server
	 * @return Calendar of when last update was
	 */
	public Calendar lastUpdate(){
		return lastUpdate;
	}

}
