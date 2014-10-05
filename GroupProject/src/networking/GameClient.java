package networking;

import gameLogic.Avatar;
import gameLogic.Room;

import java.io.IOException;
import java.util.Calendar;

import javax.swing.JComponent;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class GameClient extends ChatClient {

	// Client side of the game
	private Room clientRoom = null;

	public GameClient(String playerName, JComponent clientImage) {
		super(playerName, clientImage);
	}

	/**
	 * Returns the client side version of the room that the player is currently in
	 * @return Room containing the player and al lthe rooms contents
	 */
	public synchronized Room getRoom(){
		// Check if we are connected first
		if( !isConnected() ){
			System.out.println("Not currently Connected to a server");
			return null;
		}

		return clientRoom;
	}

	/**
	 * Returns the Avatar accociated with the current player
	 * @return
	 */
	public Avatar getAvatar(){

		// Check if we are connected first
		if( !isConnected() ){
			System.out.println("Not currently Connected to a server");
			return null;
		}

		return clientRoom.getAvatar(getName());
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendMoveToServer(Move interaction) throws IOException{

		if( interaction instanceof Move ){
			System.out.println("Queueing on Client: " + interaction + " " + Calendar.getInstance().getTime());
		}

		// Send data to the server
		return super.sendData(interaction);
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessageToServer(String message) throws IOException{

		// Create a new chatMessage
		ChatMessage chat = new ChatMessage(player.getName(), message, getChatMessageColor());
		return sendChatMessageToServer(chat);
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendChatMessageToServer(ChatMessage chat) throws IOException{

		// Client side commands
		if( chat.message.equals("/clear") ){
			clearChatHistory();
			return true;
		}

		// Record client sided message
		addChatMessage(chat);

		// Send data to the server
		return super.sendData(chat);
	}

	@Override
	public synchronized void retrieveObject(NetworkObject data) {
		super.retrieveObject(data);

		// Check what type of data we were sent
		if( data.getData() instanceof RoomUpdate ){

			// We were sent a move
			retrievedUpdatedRoom((RoomUpdate)data.getData());
		}
	}

	/**
	 * Process the supplied chat message sent from the server to this client.
	 * @param sendersIPAddress IP of the senders IP Address
	 * @param chatMessage Message that was sent from the given IP
	 */
	public synchronized void retrievedUpdatedRoom(RoomUpdate room){
		clientRoom = room.updatedRoom;

		System.out.println("Client Recieved new Room: " + Calendar.getInstance().getTime());

		// Room has changed to redraw it
		repaintImage();
	}

	@Override
	public void successfullyConnected(String playerName) {


	}
}
