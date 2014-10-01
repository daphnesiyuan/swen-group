package gameLogic;

import java.util.ArrayList;
import java.util.List;

import networking.Move;


public class  Game {

	//TODO ryan
	// any class in gamelogic updates something, make sure all other classes are updated respectivley

	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;
	private List<Floor> spawnTiles;


	public enum Facing { North, South, East, West; }


	public Game(){
		roomsInGame = new ArrayList<Room>();
		activeAvatars = new ArrayList<Avatar>();
		spawnTiles = new ArrayList<Floor>();

		createNewGame();
	}

	private void createNewGame(){
		new NewGame(this);
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
		System.out.println("\n Game : getRoom()");
		for(Room room : roomsInGame){
			System.out.println("\n  Room: "+room);
			for(Avatar player : room.getAvatars()){
				System.out.println("\n   Avatar: " + player);
				if(player.getPlayerName().equals(playerName)){
					System.out.println("\n    player.getCurrentRoom()" +  player.getCurrentRoom());
					return player.getCurrentRoom();
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


	public void setSpawnTiles(List<Floor> spawnTiles) {
		this.spawnTiles = spawnTiles;
	}


	public List<Floor> getSpawnTiles() {
		return spawnTiles;
	}




}