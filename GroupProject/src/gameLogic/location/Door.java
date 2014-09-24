package gameLogic.location;

public class Door extends Tile2D {


	private Room room;

	private int toRoomIndex;

	// fields to show the location a character will appear when entering a new room via this door and its connecting door.
	private int toRoomXPos;
	private int toRoomYPos;

	public Door(int x, int y, String t) {
		super(x, y, t);
		toRoomIndex = -1;

	}

	public void setToRoomIndex(int ri) {
		toRoomIndex = ri;

	}

	public void setToRoomX(int rx) {
		toRoomXPos = rx;

	}

	public void setToRoomY(int ry) {
		toRoomYPos = ry;

	}

	public int getToRoomXPos() {
		return toRoomXPos;
	}


	public int getToRoomYPos() {
		return toRoomYPos;
	}

	public int getToRoomIndex() {
		return toRoomIndex;
	}

}
