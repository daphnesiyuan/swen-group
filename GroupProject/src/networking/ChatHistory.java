package networking;

import java.util.ArrayList;
import java.util.Stack;

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
	public final Stack<ChatMessage> history;

	public ChatHistory(Stack<ChatMessage> history){
		this.history = history;
	}

	public ChatHistory(Stack<ChatMessage> history, boolean acknowledged){
		this.history = history;
		this.acknowledged = acknowledged;
	}
}
