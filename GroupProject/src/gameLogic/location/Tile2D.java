package gameLogic.location;

import gameLogic.physical.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


	// items on tile will be returned with the lowest weights first
	public void addItem(Item item){
		itemsOnTile.add(item);
		Collections.sort(itemsOnTile,new Comparator<Item>() {
			@Override
			public int compare(Item item1, Item item2) {
				if(item1.getWeight()<item2.getWeight()) return -1;
				else if (item1.getWeight()>item2.getWeight()) return 1;
				else return 0;
			}
		});
	}

	public Item getItem(){
		return itemsOnTile.get(0);
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
