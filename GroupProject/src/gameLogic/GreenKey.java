package gameLogic;

public class GreenKey extends Key {

	public GreenKey(Tile2D tile) {
		super(tile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		String desc = "";
		return desc+ "Green" + " Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
	}
}
