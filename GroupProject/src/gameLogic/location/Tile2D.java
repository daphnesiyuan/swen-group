package gameLogic.location;

import gameLogic.physical.Item;

import java.util.ArrayList;
import java.util.List;


public class Tile2D {

	int xPos;
	int yPos;
	String type;
	Room room;

	List<Item>itemsOnTile;


	public Tile2D(int x, int y, String t) {
		xPos = x;
		yPos = y;
		type = t;

		itemsOnTile = new ArrayList<Item>();
	}


	public void addItem(Item item){
		itemsOnTile.add(item);
	}

	public int getXPos(){
		return xPos;
	}
	public int getYPos(){
		return yPos;
	}

	public String getType(){
		return type;
	}




	public void setRoom(Room r) {
		room = r;

	}

	public Room getRoom(){
		return room;
	}


	public boolean canMoveTo() {
		// TODO Auto-generated method stub
		return false;
	}


	//TODO HASHCODE AND EQUALS

}
