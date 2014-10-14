package gameLogic;

public class PurpleKey extends Key {

	public PurpleKey(Tile2D tile) {
		super(tile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ "Purple Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + " Purple Key object, in inventory";
	}

}
