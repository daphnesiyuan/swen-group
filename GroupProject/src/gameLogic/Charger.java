package gameLogic;

import java.io.Serializable;
/**
 *
 * @author griffiryan
 *
 *	The charger class extends the tile class, and when the avatar charges next to it, it increases its battery life.
 */
public class Charger extends Tile2D implements Serializable{

	private static final long serialVersionUID = -120239992386564353L;

	public Charger(int xPos, int yPos){
		super(xPos, yPos);

	}



}
