package gameLogic;

public class Floor extends Tile2D{

	public boolean isSpawn;

	public Floor(int x, int y, String t, boolean spawn) {
		super(x, y, t);
		isSpawn = spawn;

	}

	public boolean isSpawn() {
		return isSpawn;
	}

	public void setSpawn(boolean isSpawn) {
		this.isSpawn = isSpawn;
	}

}
