package networking;

import gameLogic.Avatar;
import gameLogic.Room;

import java.io.IOException;
import java.net.UnknownHostException;
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

	/**
	 * Constructor for a GameClient to specify what it's characteristics are. And how it will appear when being drawn
	 * @param playerName Name of the player
	 * @param clientImage Where to draw the Room given
	 */
	public GameClient(String playerName, JComponent clientImage) {
		super(playerName, clientImage);
	}

	/**
	 * Constructor that allows a client to be created then will connect it directly to a server
	 * @param playerName What to call the player
	 * @param clientImage Where to draw the game on
	 * @param serverIP IP Of the server to join
	 * @param serverPort Port of the server to join
	 */
	public GameClient(String playerName, JComponent clientImage, String serverIP, int serverPort) throws UnknownHostException, IOException{
		this(playerName, clientImage);
		connect(serverIP, serverPort);
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

		System.out.println("Client Name: " + getName());
		for(Avatar a : clientRoom.getAvatars()){
			System.out.println(a.getPlayerName());
		}
		System.out.println("Avatar Name: " + clientRoom.getAvatar(getName()));

		return clientRoom.getAvatar(getName());
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	public boolean sendMoveToServer(Move interaction) throws IOException{

		// Send data to the server
		return super.sendData(interaction);
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
	}

	@Override
	public void successfullyConnected(String playerName) {


	}
}
