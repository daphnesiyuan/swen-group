package networking;

import gameLogic.Room;

/**
 * Super class of Ai what enables all AI to contain a room and players
 * @author veugeljame
 *
 */
public abstract class AI implements Thinker{

	protected Room room;
	protected Player player;

	public AI(Room room, Player player){
		this.room = room;
		this.player = player;
	}

	/**
	 *Gets the room that the AI is currently in
	 * @return Room object
	 */
	public Room getRoom(){
		return room;
	}

	/**
	 * Gets the Player that the AI is controlling
	 * @return Player object
	 */
	public Player getPlayer(){
		return player;
	}
}
