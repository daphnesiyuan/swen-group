package networking;

import gameLogic.Avatar;
import gameLogic.Room;

import java.io.IOException;

/**
 *Chat Client that deals with the main aspects of the Chat program when it comes to the client
 * @author veugeljame
 *
 */
public class GameClient extends ChatClient {

	// Client side of the game
	private Object roomLock = new Object();
	private Room clientRoom = null;

	private boolean roomModified = false;
	private Object roomModifiedLock = new Object();

	/**
	 * Creates a new GameClient to connect to a server
	 * @param playerName Name of the client
	 */
	public GameClient(String playerName){
		super(playerName);
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

		synchronized(roomLock){
			return clientRoom;
		}
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

		synchronized(roomLock){
			return clientRoom.getAvatar(getName());
		}
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendMoveToServer(Move interaction) throws IOException{

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
			setChatModified(true);
			return true;
		}

		// Record client sided message
		addChatMessage(chat);
		setChatModified(true);

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
		synchronized(roomLock){
			clientRoom = room.updatedRoom;

			// Record when we last updated
			setRoomModified(true);
		}
	}

	@Override
	public void successfullyConnected(String playerName) {


	}

	/**
	 * Checks if the current client has had anything modified since the last refresh. Determines if the listener of this client needs to update or not.
	 * @return True if something has changed in the chat
	 */
	public synchronized boolean roomIsModified() {
		synchronized (roomModifiedLock){
			return roomModified;
		}
	}

	/**
	 * Sets the current state of the clients modifications status to what's given.
	 * @param modified
	 */
	public synchronized void setRoomModified(boolean modified) {
		synchronized (roomModifiedLock){
			this.roomModified = modified;
		}
	}
}
