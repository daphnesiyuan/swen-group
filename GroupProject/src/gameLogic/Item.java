package gameLogic;

import java.util.List;

public abstract class Item {



public abstract String getDescription();
public abstract Tile2D getTile();
public abstract boolean moveItemTo(Tile2D toTile);
public abstract int getWeight();
public abstract String interactWith(Avatar avatar);



}
