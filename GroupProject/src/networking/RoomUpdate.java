package networking;

import gameLogic.location.Room;

public class RoomUpdate extends NetworkData {


	/**
	 *
	 */
	private static final long serialVersionUID = 8175985451592995007L;
	public final Room updatedRoom;

	public RoomUpdate(Room room){
		this.updatedRoom = room;
	}

}
