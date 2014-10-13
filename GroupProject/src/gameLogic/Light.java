package gameLogic;

/**
 *
 * @author Ryan Griffin and Leon North
 *
 */
public class Light extends Item{


	public Light(Tile2D tile) {
		this.tile = tile;
		this.startTile = tile;
		this.movable = true;
	}

	@Override
	public String getDescription() {
		System.out.println("in description fetcher");
		String desc = "";
		return desc + "Light object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
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
		return this.interactWith(avatar);
	}

}
