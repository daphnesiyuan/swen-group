package gameLogic.physical;

import gameLogic.location.Floor;
import gameLogic.location.Tile2D;


public class ItemObject extends Item {

int xPos;
int yPos;

int weight;

Tile2D location;

boolean movable;

String itemObjectType;



	public ItemObject(String type, Tile2D l, int w,String m) {
		itemObjectType = type;
		location = l;
		weight = w;

		if(m.equals("T")) movable = true;
		else movable = false;
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



}