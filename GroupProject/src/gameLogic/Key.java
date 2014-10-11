package gameLogic;

import java.awt.Color;

public class Key extends Item {

	private Tile2D tile;

	private Tile2D startTile;

	private Color color;

	private int xPos;
	private int yPos;


	public Key(Tile2D tile){
		this.tile = tile;
		this.xPos = tile.getxPos();
		this.yPos = tile.getyPos();

		this.startTile = tile;
		this.movable = true;


	}

	@Override
	public String getDescription() {
		String desc = "";
		return desc + this.color.toString()+" Key object, at X,Y: ("+xPos+","+yPos+")";
	}

	@Override
	public Tile2D getTile() {
		return this.tile;
	}

	@Override
	public boolean moveItemTo(Tile2D toTile) {
		if(toTile == null) return false;
		this.tile = toTile;
		return true;
	}

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public boolean interactWith(Avatar avatar) {
		return avatar.getInventory().add(this);
	}

	@Override
	public void returnToStartPos() {
		tile = startTile;

	}

	@Override
	public boolean pickItemUp(Avatar avatar) {
		return this.interactWith(avatar);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
