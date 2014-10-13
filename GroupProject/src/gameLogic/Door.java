package gameLogic;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

public class Door extends Tile2D{



	private Room toRoom;
	private Color color;

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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}




}
