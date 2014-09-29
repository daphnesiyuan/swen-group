package gameLogic;


public class Key extends Item{

	private int xPos;
	private int yPos;
	private int weight;

	private boolean movable;

	private String itemObjectType;

	private Tile2D location;

	private boolean inInventory;

	private String keyType;



	public Key(String type, Tile2D l, int w, String m) {
		itemObjectType = type;
		location = l;
		weight = w;

		if(m.equals("T")) movable = true;
		else movable = false;

		inInventory = false;

		keyType = type;

	}

	@Override
	public String getDescription() {
		return new String("ItemObject; "+itemObjectType+", xPos :"+xPos+", yPos: "+yPos+", weight: "+weight+", movable: "+movable);
	}

	@Override
	public Tile2D getTile() {

		return location;
	}

	@Override
	public boolean moveItemTo(Tile2D toTile) {

		// If item isnt movable
		if(!movable) return false;

		// If trying to move Item directly out of room
		if(location.getRoom()!=toTile.getRoom()) return false;

		// If moving to same tile
		if(toTile.equals(location)) return false;

		// If move location is a wall or door
		if(!(toTile instanceof Floor)) return false;

		return true;

	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public String interactWith(Avatar gc) {
		if(!inInventory){
			gc.getInventory().add(this);
			inInventory = true;
			return new String("You Picked up the key: "+ keyType);
		}
		else{
			return keyType;
		}

	}

}
