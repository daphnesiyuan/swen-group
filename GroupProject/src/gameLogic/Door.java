package gameLogic;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

public class Door extends Tile2D{


	private int toRoomIndex;

	private Color color;

	public Door(int xPos, int yPos) {
		super(xPos, yPos);
	}


	public void setColor(Color color){
		this.color = color;
	}

	public Color getColor(){
		return color;
	}
}
