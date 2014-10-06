package gameLogic;

import java.awt.Color;
import java.io.Serializable;

public class ColorChange extends Item implements Serializable{


	private static final long serialVersionUID = 88537099210702004L;

	private Tile2D tile;
	private final int xPos, yPos;
	private Color color;

	public ColorChange(Tile2D tile, Color color){
		this.tile = tile;
		this.xPos = tile.getxPos();
		this.yPos = tile.getyPos();
		this.movable = false;
		this.startX = xPos;
		this.startY = yPos;

		this.color = color;
	}

	@Override
	public String getDescription() {
		String desc = "";
		desc+= "ColorChange ("+color+"), at: ( "+xPos+" , "+xPos+" )";
		return desc;
	}

	@Override
	public Tile2D getTile() {
		return tile;
	}

	@Override
	public boolean moveItemTo(Tile2D toTile) {
		return false; // always returns false as Item is unmovable
	}

	@Override
	public int getWeight() {
		return 0; // Item should never be placed on top of anything else
	}

	@Override
	public boolean interactWith(Avatar avatar) {
		avatar.setColor(color);
		return true;
	}

	@Override
	public void returnToStartPos() {
		// Does nothing - charger is unmovable
	}

	@Override
	public boolean pickItemUp() {
		return false;	// charger cannot be picked up
	}

}
