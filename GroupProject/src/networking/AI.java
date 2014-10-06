package networking;

import gameLogic.Room;

public abstract class AI implements Thinker{

	protected Room room;
	protected Player player;

	public AI(Room room, Player player){
		this.room = room;
		this.player = player;
	}

	public Room getRoom(){
		return room;
	}

	public Player getPlayer(){
		return player;
	}
}
