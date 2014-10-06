package networking;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A chathistory that contains a stack of ChatMessages sent through the network.
 * @author veugeljame
 *
 */
public class ChatHistory extends NetworkData {

	/**
	 *
	 */
	private static final long serialVersionUID = -5022978323415087568L;
	public final Stack<ChatMessage> history;

	public ChatHistory(Stack<ChatMessage> history, boolean acknowledged){
		this.history = history;
		this.acknowledged = acknowledged;
	}
}
