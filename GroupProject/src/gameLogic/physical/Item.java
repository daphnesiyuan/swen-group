package gameLogic.physical;

import gameLogic.location.Tile2D;

import java.util.List;

public abstract class Item {
	private int weight;
	private String description;

	private int x;
	private int y;

	private boolean movable = false;

	List<? extends Item> contains;



public abstract String getDescription();
public abstract Tile2D getTile();
public abstract boolean moveItemTo(Tile2D toTile);



}
