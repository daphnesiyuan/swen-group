package networking;

import java.awt.Color;
import java.util.Random;

/**
 * Chat message sent through the network
 * @author veugeljame
 *
 */
public class ChatMessage extends NetworkData{

	/**
	 *
	 */
	private static final long serialVersionUID = 3899197915705338681L;
	public final String message;
	public final String sendersName;
	public final Color color;

	public ChatMessage(String name, String message, Color color, boolean acknowledged){
		this.sendersName = name;
		this.message = message;
		this.color = color;
		this.acknowledged = acknowledged;
	}

	public ChatMessage(String name, String message, Color color){
		this.sendersName = name;
		this.message = message;
		this.color = color;
	}

	public ChatMessage(String message, Color color){
		this.sendersName = "";
		this.message = message;
		this.color = color;
	}

	public ChatMessage(String message, Color color, boolean acknowledged){
		this.sendersName = "";
		this.message = message;
		this.color = color;
		this.acknowledged = acknowledged;
	}

	/**
	 * Returns a clone of the given message, but as an acknowledged message
	 * @param message the Chat message to clone
	 * @return a clone of the givven message with acknowledged == true
	 */
	public static ChatMessage getAcknowledged(ChatMessage message){
		return new ChatMessage(message.sendersName, message.message, message.color, true);
	}

	public String toString(){
		// Time > James: MESSAGE!
		// Time James: Confirmed on the server
		// Time Welcome to the server!
		return getTime() + " " + ( !acknowledged ? ">" : "" ) +
				( !sendersName.equals("") ? sendersName + ": " : "" ) +
				message;
	}

	@Override
	public boolean equals(Object object){
		if( object == null ) return false;
		if( !(object instanceof ChatMessage) ) return false;

		ChatMessage other = (ChatMessage)object;
		if( !sendersName.equals(other.sendersName) ) return false;
		if( !message.equals(other.message) ) return false;

		return dataSent.equals(other.dataSent);
	}
}
