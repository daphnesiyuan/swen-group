package networking;

import java.io.IOException;
import java.util.Calendar;

/**
 *
 * @author veugeljame
 *
 */
public class ChatClient extends Client {

	private String clientName;

	private String chatHistory = "";

	private Calendar lastUpdate = null;

	public ChatClient() {
		super();
	}

	/**
	 * Changes the name of the client to be displayed through-out the game
	 * @param name
	 */
	public void setName(String name){

		// Tell the server to update this clients name as well!
		try {
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
	public boolean sendData(Object data) throws IOException{

		return super.sendData(clientName, data);
	}

	@Override
	public void retrieveObject(NetworkObject data) {

		// Save the message
		chatHistory = chatHistory + data + "\n";

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
	 * Returns the time we last got an update from the server
	 * @return Calendar of when last update was
	 */
	public Calendar lastUpdate(){
		return lastUpdate;
	}

}
