package gameLogic;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author griffiryan
 *
 * The Box class is an Extension of Item, and overrides all of its methods.
 */
public class Box extends Item{

	// The primary function of a Box object is to contain other Items.
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
		if(tile != null) return desc + "Box object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + "Box object, in inventory";

	}

	@Override
	public Tile2D getTile() {
		return tile;
	}

	@Override
	public boolean moveItemTo(Tile2D toTile) {
		if(toTile == null) return false;
		this.tile = toTile;
		toTile.addItem(this);
		return true;
	}

	@Override
	public int getWeight() {
		return 0;
	}

	/**
	 * The method checks whether the given avatar has enough room in their inventory, returns true iff there is space, removes it from the box and adds it to the inventory
	 */
	@Override
	public boolean interactWith(Avatar avatar) {
		if(avatar.getInventory().size()==5){
			System.out.println("Couldnt pick up "+this.getClass().getName() + ", Inventory is full!");
			return false; 	//no space left in inventory
		}
		if(tile != null) tile.removeItem(this);
		tile = null;
		return avatar.getInventory().add(this);
	}

	@Override
	public void returnToStartPos() {
		tile = startTile;
		tile.addItem(this);

	}

	@Override
	public boolean pickItemUp(Avatar avatar) {
		return this.interactWith(avatar);
	}


	public List<Item> getContains() {
		return contains;
	}

	public void setContains(List<Item> contains) {
		this.contains = contains;
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
