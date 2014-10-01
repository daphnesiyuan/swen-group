package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Tile2D implements Serializable{


	private static final long serialVersionUID = 111202619281809955L;

	private int xPos;
	private int yPos;
	private String type;
	private Room room;

	private List <Item> itemsOnTile;

	// One character per Tile at any given time
	private Avatar avatarOnTile;


	public Tile2D(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;

		itemsOnTile = new ArrayList<Item>();
		avatarOnTile = null;
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

	public Item getTopItem(){
		if(itemsOnTile.size()==0) return null;
		return itemsOnTile.get(0);
	}

	public boolean itemOnTile(){
		if(avatarOnTile != null) return false;
		return true;
	}

	public List<Item> getItems() {
		return itemsOnTile;
	}

	public void removeAvatar(Avatar player) {
		if(avatarOnTile.equals(player)){
			avatarOnTile = null;
		}
		else System.out.println("Tile2D: removePlayer(); Error removing character from tile");
	}

	public void addAvatar(Avatar player) {
		avatarOnTile = player;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setItems(List<Item> itemsOnTile) {
		this.itemsOnTile = itemsOnTile;
	}

	public Avatar getAvatar() {
		return avatarOnTile;
	}

	public void setAvatarOnTile(Avatar avatarOnTile) {
		this.avatarOnTile = avatarOnTile;
	}
}
