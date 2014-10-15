package gameLogic;

/**
*
* @author griffiryan
*
*	RedKey is a specialization of the Key class.
*/
public class RedKey extends Key {


	public RedKey(Tile2D tile) {
		super(tile);
	}

	@Override
	public String getDescription() {
		String desc = "";
		if(tile != null) return desc+ "Red Key object, at X,Y: ("+tile.getxPos()+","+tile.getyPos()+")";
		else return desc + " Red Key object, in inventory";
	}

}
