package gameLogic;

import java.io.Serializable;

public class Floor extends Tile2D{

	public boolean isSpawn;

	public Floor(int xPos, int yPos, boolean spawn) {
		super(xPos, yPos);
		isSpawn = spawn;

	}

	public boolean isSpawn() {
		return isSpawn;
	}

	public void setSpawn(boolean isSpawn) {
		this.isSpawn = isSpawn;
	}

}
