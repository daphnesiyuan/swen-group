package gameLogic;

/**
 *
 * @author Ryan Griffin and Leon North
 * Avatars that contain a light object in their inventory can see better during the night cycle.
 */
public class Light extends Item{


	public Light(Tile2D tile) {
		this.tile = tile;
		this.startTile = tile;
		this.movable = true;
	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc + "Light object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + "Light object, in inventory";
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
