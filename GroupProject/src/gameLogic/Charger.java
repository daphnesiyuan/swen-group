package gameLogic;

import java.io.Serializable;

public class Charger extends Tile2D implements Serializable{

	private static final long serialVersionUID = -120239992386564353L;

	private final int xPos, yPos;

	public Charger(int xPos, int yPos){
		super(xPos, yPos);
		this.xPos = xPos;
		this.yPos = yPos;

	}



}
