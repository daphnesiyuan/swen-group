package gameLogic;
/**
*
* @author griffiryan
*
*	PurpleKey is a specialization of the Key class.
*/
public class PurpleKey extends Key {

	public PurpleKey(Tile2D tile) {
		super(tile);
	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ "Purple Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + " Purple Key object, in inventory";
	}

}
