package gameLogic;

import java.io.Serializable;
import java.util.List;

public class Door extends Tile2D{


	private Room room;

	private int toRoomIndex;

	// fields to show the location a character will appear when entering a new room via this door and its connecting door.
	private int toRoomXPos;
	private int toRoomYPos;

	private boolean locked;

	//private List<Key> unlockKeys;

	public Door(int xPos, int yPos) {
		super(xPos, yPos);
		toRoomIndex = -1;
	}

	public boolean getLocked(){
		return locked;

	}

	public void setLocked(){
		locked = !locked;
	}

//	public List<Key> getKeys(){
//		return unlockKeys;
//	}
//
//	public void setKeys(List<Key> keys){
//		unlockKeys = keys;
//	}




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
