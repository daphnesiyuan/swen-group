package gameLogic;

import java.util.List;

public abstract class Item {
public abstract String getDescription();
public abstract Tile2D getTile();
public abstract boolean moveItemTo(Tile2D toTile);
public abstract int getWeight();
public abstract String interactWith(Avatar avatar);
public abstract void returnToStartPos();

public int startX, startY; // used for when a player leaves the game, items in their inverntory are not lost but returned here.
}