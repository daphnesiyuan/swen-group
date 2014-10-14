package gameLogic;

import java.util.ArrayList;
import java.util.List;

public class Box extends Item{

	private List <Item> contains;

	public Box(Tile2D tile) {
		this.tile = tile;
		this.startTile = tile;
		this.movable = true;
		this.contains = new ArrayList<Item>();
	}

	@Override
	public String getDescription() {
		String desc = "";
		return desc + "Box object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
	}

	@Override
	public Tile2D getTile() {
		return tile;
	}

	@Override
	public boolean moveItemTo(Tile2D toTile) {
		if(toTile == null) return false;
		this.tile = toTile;
		return true;
	}

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public boolean interactWith(Avatar avatar) {
		tile.removeItem(this);
		return avatar.getInventory().add(this);
	}

	@Override
	public void returnToStartPos() {
		tile = startTile;

	}

	@Override
	public boolean pickItemUp(Avatar avatar) {
		if(avatar.getInventory().size()==4) return false; 	//no space left in inventory
		tile.removeItem(this);
		return this.interactWith(avatar);
	}

	@Override
	public int hashCode() {
		return itemID * 11;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Item other = (Item) obj;
		if (itemID != other.itemID) return false;
		return true;
	}

}
