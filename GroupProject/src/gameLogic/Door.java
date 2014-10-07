package gameLogic;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

public class Door extends Tile2D{



	private Room room_one;
	private Room room_two;


	private Color color;



	public Door(int xPos, int yPos) {
		super(xPos, yPos);

		room_one = null;
		room_two = null;
	}


	public void setColor(Color color){
		this.color = color;
	}

	public Color getColor(){
		return color;
	}


	public Room getRoomOne() {
		return room_one;
	}


	public void setRoomOne(Room room_one) {
		this.room_one = room_one;
	}


	public Room getRoomTwo() {
		return room_two;
	}


	public void setRoomTwo(Room room_two) {
		this.room_two = room_two;
	}



}
