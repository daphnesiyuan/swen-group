package gameLogic;

public class YellowKey extends Key {

	public YellowKey(Tile2D tile) {
		super(tile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ " Yellow Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + "Yellow Key object, in inventory";
	}
}
