package gameLogic;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

public class Door extends Tile2D{


	private Room room;

	private int toRoomIndex;

	// fields to show the location a character will appear when entering a new room via this door and its connecting door.
	private int toRoomXPos;
	private int toRoomYPos;

	private boolean locked;		// White door - unlocked

	private Color color;

	public Door(int xPos, int yPos) {
		super(xPos, yPos);
		toRoomIndex = -1;
	}

	public boolean getLocked(){
		return locked;

	}

	public void setLocked(boolean locked){
		locked = this.locked;
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

	public void setColor(Color color){
		this.color = color;
	}

	public Color getColor(){
		return color;
	}
}
