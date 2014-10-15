package gameLogic;

public class YellowKey extends Key {


	private static final long serialVersionUID = -4993294790727957747L;

	public YellowKey(Tile2D tile) {
		super(tile);

	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ " Yellow Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + "Yellow Key object, in inventory";
	}
}

