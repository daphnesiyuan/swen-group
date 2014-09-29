package gameLogic;

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

	// One character per Tile at any given time
	Avatar characterOnTile;


	public Tile2D(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;

		itemsOnTile = new ArrayList<Item>();
		characterOnTile = null;
	}

	public Avatar getCharacter(){
		return characterOnTile;
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

	public void removePlayer(Avatar player) {
		if(characterOnTile.equals(player)){
			characterOnTile = null;
		}
		else System.out.println("Tile2D: removePlayer(); Error removing character from tile");
	}

	public void addPlayer(Avatar player) {
		characterOnTile = player;

	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public List<Item> getItemsOnTile() {
		return itemsOnTile;
	}

	public void setItemsOnTile(List<Item> itemsOnTile) {
		this.itemsOnTile = itemsOnTile;
	}

	public Avatar getCharacterOnTile() {
		return characterOnTile;
	}

	public void setCharacterOnTile(Avatar characterOnTile) {
		this.characterOnTile = characterOnTile;
	}

	public void setType(String type) {
		this.type = type;
	}


}
