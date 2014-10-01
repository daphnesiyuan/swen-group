package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import networking.Move;


public class Game implements Serializable {


	private static final long serialVersionUID = 2936338356693550591L;

	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;


	public enum Facing { North, South, East, West; }


	public Game(){
		roomsInGame = new ArrayList<Room>();
		activeAvatars = new ArrayList<Avatar>();
		createNewGame();
	}


	public void addRoom(Room room){
		roomsInGame.add(room);
	}

	private void createNewGame(){
		new NewGame(this);
	}


	public boolean addPlayer(String playerName){
		Room room = roomsInGame.get(0);
		Tile2D tile = room.getTiles()[3][3];


		Avatar avatar = new Avatar(playerName,tile,this);
		tile.addPlayer(avatar);
		room.addAvatar(avatar);

		return true;
	}



	public boolean moveAvatar(Move move){

		Avatar mover = null;

		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(move.getPlayer().getName())){
				mover = avatar;
			}
		}
		if(mover==null) return false;
		return mover.moveTo(move);
	}

	public boolean avatarInteractWithItem(String playerName, Item item){
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(playerName)){
				return avatar.interact(item);
			}
		}
		return false;
	}

	/**
	 *
	 * @param playerName - player identidied with their name string
	 * @return the Room the given player is in
	 */
	public Room getRoom(String playerName){
		for(Room room : roomsInGame){
			for(Avatar player : room.getAvatars()){
				if(player.getPlayerName().equals(playerName)){
					return room;
				}
			}
		}
		return null;
	}

	public boolean setPlayerName(String fromName, String toName){
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(fromName)){
				avatar.setPlayerName(toName);
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @param roomIndex attached to the sought room
	 * @return the Room with the associated RoomIndex - useful for finding the room that a door is attached to.
	 */
	public Room getRoom(int roomIndex) {
		for(Room room : roomsInGame){
			if(room.getRoomNumber() == roomIndex){
				return room;
			}
		}
		return null;
	}

	public boolean removePlayerFromGame(String playerName){
		Avatar leaving = null;
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(playerName)){
				leaving = avatar;
			}
		}
		if(leaving == null){
			System.out.println("No player in game with name: "+playerName);
			return false;
		}
		for(Item item : leaving.getInventory()){
			item.returnToStartPos();
		}

		leaving.getCurrentTile().setAvatarOnTile(null);
		leaving.getCurrentRoom().getAvatars().remove(leaving);

		return activeAvatars.remove(leaving);
	}




	public void setActiveAvatars(List<Avatar> activeAvatars) {
		this.activeAvatars = activeAvatars;
	}

	public List<Room> getRoomsInGame() {
		return roomsInGame;
	}


	public void setRoomsInGame(List<Room> roomsInGame) {
		this.roomsInGame = roomsInGame;
	}


	public List<Avatar> getActiveAvatars() {
		return activeAvatars;
	}





}