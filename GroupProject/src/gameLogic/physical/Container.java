package gameLogic.physical;

import gameLogic.location.Tile2D;


public class Container extends Item{

	boolean movable;

	int xPos;
	int yPos;

	int weight;

	Tile2D location;

	String containerType;

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
		// TODO Auto-generated method stub
		return false;
	}



}
