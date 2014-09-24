package gameLogic.physical;

import gameLogic.location.Tile2D;

public class Furniture extends Item{


	boolean movable;

	int xPos;
	int yPos;
	Tile2D location;

	int weight;

	String furnitureType;


	public Furniture(String type, Tile2D l, int w,String m) {
		furnitureType = type;
		location = l;
		weight = w;

		if(m.equals("T")) movable = true;
		else movable = false;


	}


	@Override
	public String getDescription() {
		return new String("Furniture; "+furnitureType+", xPos :"+xPos+", yPos: "+yPos+", weight: "+weight+", movable: "+movable);
	}


	@Override
	public Tile2D getTile() {

		return location;
	}


	@Override
	public boolean moveItemTo(Tile2D toTile) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getWeight() {
		return weight;
	}



}
