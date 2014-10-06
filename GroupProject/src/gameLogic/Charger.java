package gameLogic;

import java.io.Serializable;

public class Charger extends Item implements Serializable{

	private static final long serialVersionUID = -120239992386564353L;

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
		return false; // always returns false as Item is unmovable
	}

	@Override
	public int getWeight() {
		return 0; // Item should never be placed on top of anything else
	}

	@Override
	public boolean interactWith(Avatar avatar) {
		ChargerThread ct = new ChargerThread(avatar, this);
		ct.start();
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

	static class ChargerThread extends Thread{
		Avatar avatar;
		Charger charger;
		public ChargerThread(Avatar avatar, Charger charger){
			this.avatar = avatar;
			this.charger = charger;
		}
		public void run(){
			while(true){
				if(avatar.getCurrentTile().equals(charger.tile)){
					avatar.setCharging(true);
				}
				else{
					avatar.setCharging(false);
					break;
				}
			}
		}
	}


}
