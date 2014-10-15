package gameLogic;
/**
*
* @author griffiryan
*
*	GreenKey is a specialization of the Key class.
*/
public class GreenKey extends Key {

	public GreenKey(Tile2D tile) {
		super(tile);

	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ "Green Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + " Green Key object, in inventory";
	}
}
