package gameLogic;

import java.awt.Color;

public class Key extends Item {


	public Key(Tile2D tile){
		this.tile = tile;
		this.startTile = tile;
		this.movable = true;

	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ " Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + "Key object, in inventory";
	}

	@Override
	public Tile2D getTile() {
		return this.tile;
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
		if(avatar.getInventory().size()==4) return false; 	//no space left in inventory
		tile.removeItem(this);
		tile = null;
		return avatar.getInventory().add(this);
	}

	@Override
	public void returnToStartPos() {
		tile = startTile;

	}

	@Override
	public boolean pickItemUp(Avatar avatar) {

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
