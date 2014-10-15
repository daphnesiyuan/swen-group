package gameLogic;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author griffiryan
 * Door class is an extension of Tile2D, it has identical functionality, but also contains a Room field which represents the room the avatar will travel to if the door is used.
 */
public class Door extends Tile2D{

	private Room toRoom;

	public Door(int xPos, int yPos) {
		super(xPos, yPos);
		toRoom = null;
	}

	public Room getToRoom() {
		return toRoom;
	}

	public void setToRoom(Room toRoom) {
		this.toRoom = toRoom;
	}



}
