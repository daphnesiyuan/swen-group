package networking;

import gameLogic.Room;

/**
 * Object sent through the network that contains the current up-to-date room
 * Sent via the server to the clients
 * @author veugeljame
 *
 */
public class RoomUpdate extends NetworkData {
	private static final long serialVersionUID = 8175985451592995007L;

	// New room to be sent to clients
	public final Room updatedRoom;

	public RoomUpdate(Room room){
		this.updatedRoom = room;
	}

}
