package gameLogic;

public class RedKey extends Key {

	public RedKey(Tile2D tile) {
		super(tile);
	}

	@Override
	public String getDescription() {
		String desc = "";
		return desc+ "Red" + " Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
	}

}
