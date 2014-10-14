package gameLogic;

import java.io.Serializable;
import java.util.List;

public abstract class Item implements Serializable{

private static final long serialVersionUID = 2903139965045313571L;

protected Tile2D startTile;
protected Tile2D tile;
protected boolean movable = false;
protected int itemID;

public abstract String getDescription();
public abstract Tile2D getTile();
public abstract boolean moveItemTo(Tile2D toTile);
public abstract int getWeight();
public abstract boolean interactWith(Avatar avatar);
public abstract void returnToStartPos();
public abstract boolean pickItemUp(Avatar avatar);

public int getItemID(){
	return itemID;
}
public void setItemID(int id){
	this.itemID = id;
}

public Tile2D getStartTile() {
	return startTile;
}
public void setStartTile(Tile2D startTile) {
	this.startTile = startTile;
}

@Override
public abstract int hashCode();

@Override
public abstract boolean equals(Object obj);

}

