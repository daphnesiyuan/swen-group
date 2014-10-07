package gameLogic;

import java.io.Serializable;
import java.util.List;

public abstract class Item implements Serializable{

private static final long serialVersionUID = 2903139965045313571L;

protected int startX, startY; // used for when a player leaves the game, items in their inverntory are not lost but returned here.
protected Tile2D tile;
protected boolean movable = false;

public abstract String getDescription();
public abstract Tile2D getTile();
public abstract boolean moveItemTo(Tile2D toTile);
public abstract int getWeight();
public abstract boolean interactWith(Avatar avatar);
public abstract void returnToStartPos();
public abstract boolean pickItemUp();



}
