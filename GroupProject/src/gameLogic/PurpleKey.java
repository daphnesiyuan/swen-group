package gameLogic;

public class PurpleKey extends Key {

	public PurpleKey(Tile2D tile) {
		super(tile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		String desc = "";
		return desc+ "Purple" + " Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
	}

}
