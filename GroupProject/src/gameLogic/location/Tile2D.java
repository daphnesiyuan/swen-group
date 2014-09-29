package gameLogic.location;

import gameLogic.entity.GameCharacter;
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

	List <Item> itemsOnTile;

	List <GameCharacter> charactersOnTile;


	public Tile2D(int x, int y, String t) {
		xPos = x;
		yPos = y;
		type = t;

		itemsOnTile = new ArrayList<Item>();

		charactersOnTile = new ArrayList<GameCharacter>();
	}

	public GameCharacter getCharacter(){
		//TODO -> hard coded
		// There are no characters on this tile
		if(charactersOnTile.size()==0) return null;
		return charactersOnTile.get(0);

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


	public Tile2D getTileLeft(){
		Tile2D newTile = null;
		newTile = room.tiles[xPos-1][yPos];
		if(newTile==null) return this;
		else return newTile;
	}

	public Tile2D getTileRight(){
		Tile2D newTile = null;
		newTile = room.tiles[xPos+1][yPos];
		if(newTile==null) return this;
		else return newTile;
	}

	public Tile2D getTileUp(){
		Tile2D newTile = null;
		newTile = room.tiles[xPos][yPos-1];
		if(newTile==null) return this;
		else return newTile;
	}

	public Tile2D getTileDown(){
		Tile2D newTile = null;
		newTile = room.tiles[xPos][yPos+1];
		if(newTile==null) return this;
		else return newTile;
	}

	/**
	 * @return a list of all items on a tile, Items with lower weights are closer to the start of the list
	 */

	public List<Item> getItems(){
		return itemsOnTile;
	}
	/**
	 * @return the Item with the lowest weight (on top) of the tile.
	 */
	public Item getTopItem(){
		if(itemsOnTile.size()==0) return null;
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

	// Can a Character move on top of this tile
	public boolean canMoveTo(){
		return true;

	}

	public void removePlayer(GameCharacter player) {
		boolean removed = false;

		for(GameCharacter gc : charactersOnTile){
			if(gc.equals(player)){
				charactersOnTile.remove(gc);
				removed = true;
			}
		}
		if(removed==false) System.out.println("Tile2D: removePlayer(); Error removing character from tile");
	}

	public void addPlayer(GameCharacter player) {
		charactersOnTile.add(player);

	}


}
