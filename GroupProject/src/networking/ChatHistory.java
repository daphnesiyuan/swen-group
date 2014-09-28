package networking;

import java.util.ArrayList;

/**
 * A chathistory that contains an arrayList of ChatMessages sent throguh the network.
 * @author veugeljame
 *
 */
public class ChatHistory extends NetworkData {

	/**
	 *
	 */
	private static final long serialVersionUID = -5022978323415087568L;
	public final ArrayList<ChatMessage> history;

	public ChatHistory(ArrayList<ChatMessage> history){
		this.history = history;
	}

	public ChatHistory(ArrayList<ChatMessage> history, boolean acknowledged){
		this.history = history;
		this.acknowledged = acknowledged;
	}
}
