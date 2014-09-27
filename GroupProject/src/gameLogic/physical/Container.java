package gameLogic.physical;

import gameLogic.entity.GameCharacter;
import gameLogic.location.Floor;
import gameLogic.location.Tile2D;


public class Container extends Item{

	private int xPos;
	private int yPos;
	private int weight;

	private boolean movable;

	private String itemObjectType;

	private Tile2D location;

	private boolean inInventory;

	private String containerType;

	public Container(String type, Tile2D l, int w,String m) {

		containerType = type;
		location = l;
		weight = w;

		if(m.equals("T")) movable = true;
		else movable = false;

	}

	@Override
	public String getDescription() {
		return new String("Conainer; "+containerType+", xPos :"+xPos+", yPos: "+yPos+", weight: "+weight+", movable: "+movable);

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
	public String interactWith(GameCharacter gc) {
		if(!inInventory){
			gc.getInventory().add(this);
			inInventory = true;
			return new String("You Picked up the Container: "+ containerType);
		}
		else{
			return containerType;
		}

	}



}
