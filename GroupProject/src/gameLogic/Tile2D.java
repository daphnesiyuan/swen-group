package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author griffiryan
 *
 * Tile2D is a generalization of any location in the playing world. It can hold avatars and items.
 * A Tile2D operates as a graph structure, with access to its neighbouring tiles.
 */
public class Tile2D implements Serializable{


	private static final long serialVersionUID = 111202619281809955L;

	private int xPos;
	private int yPos;
	private Room room;

	private List <Item> itemsOnTile;

	// One character per Tile at any given time
	private Avatar avatarOnTile;


	public Tile2D(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;

		this.itemsOnTile = new ArrayList<Item>();
		this.avatarOnTile = null;
	}



	/**
	 *  items on tile will be returned with the lowest weights first
	 * @param item - the Item object to be added to the tile
	 */
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


	/**
	 * GRAPH STRUCTURE
	 */


	public Tile2D getTileRight(){
		if(this.xPos <= 0) return null;
		Tile2D newTile = null;
		newTile = room.getTiles()[yPos][xPos-1];
		if(newTile==null) return this;
		else return newTile;
	}

	public Tile2D getTileLeft(){
		if(this.xPos >= room.getTiles().length-1) return null;
		Tile2D newTile = null;
		newTile = room.getTiles()[yPos][xPos+1];
		if(newTile==null) return this;
		else return newTile;
	}

	public Tile2D getTileUp(){
		if(this.yPos <= 0) return null;
		Tile2D newTile = null;
		newTile = room.getTiles()[yPos-1][xPos];
		if(newTile==null) return this;
		else return newTile;
	}

	public Tile2D getTileDown(){
		if(this.yPos >= room.getTiles().length-1) return null;
		Tile2D newTile = null;
		newTile = room.getTiles()[yPos+1][xPos];
		if(newTile==null) return this;
		else return newTile;
	}

	public void removeItem(Item item){
		itemsOnTile.remove(item);
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
		if (avatarOnTile == null) return;
		if(avatarOnTile.equals(player)){
			avatarOnTile = null;
		}
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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xPos;
		result = prime * result + yPos;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile2D other = (Tile2D) obj;
		if (xPos != other.xPos)
			return false;
		if (yPos != other.yPos)
			return false;
		return true;
	}


}
