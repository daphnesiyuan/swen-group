package gameLogic;

public class Charger extends Item{

	private Tile2D tile;
	private final int xPos, yPos;

	public Charger(Tile2D tile){
		this.tile = tile;
		this.xPos = tile.getxPos();
		this.yPos = tile.getyPos();
		this.movable = false;
		this.startX = xPos;
		this.startY = yPos;
	}

	@Override
	public String getDescription() {
		String desc = "";
		desc+= "Charger at: ( "+xPos+" , "+xPos+" )";
		return desc;
	}

	@Override
	public Tile2D getTile() {
		return tile;
	}

	@Override
	public boolean moveItemTo(Tile2D toTile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String interactWith(Avatar avatar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnToStartPos() {
		// TODO Auto-generated method stub

	}

}
