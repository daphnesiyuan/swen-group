package gameLogic;

import java.util.ArrayList;
import java.util.List;

import networking.Move;


public class  Game {

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

	public boolean setPlayerName(String fromName, String toName){
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(fromName)){
				avatar.setPlayerName(toName);
				return true;
			}
		}
		return false;
	}


	public boolean moveCharacter(Move move){

		Avatar mover = null;

		for(Avatar character : activeAvatars){
			if(character.getPlayerName().equals(move.getPlayer().getName())){
				mover = character;
			}
		}
		if(mover==null) return false;
		return mover.moveTo(move);
	}

	public boolean characterInteractWithItem(String charName, Item item){
		for(Avatar character : activeAvatars){
			if(character.getPlayerName().equals(charName)){
				return character.interact(item);
			}
		}
		return false;
	}


	public Room getRoom(String playerName){
		for(Room room : roomsInGame){
			for(Avatar player : room.getCharacters()){
				if(player.getPlayerName().equals(playerName)){
					return player.getCurrentRoom();
				}
			}

		}
		return null;
	}



	public void setActiveCharacters(List<Avatar> activeCharacters) {
		this.activeAvatars = activeCharacters;
	}

	public List<Room> getRoomsInGame() {
		return roomsInGame;
	}


	public void setRoomsInGame(List<Room> roomsInGame) {
		this.roomsInGame = roomsInGame;
	}


	public List<Avatar> getActiveCharacters() {
		return activeAvatars;
	}


	public void setSpawnTiles(List<Floor> spawnTiles) {
		this.spawnTiles = spawnTiles;
	}


	public List<Floor> getSpawnTiles() {
		return spawnTiles;
	}



	public Room getRoom(int newRoomIndex) {
		for(Room room : roomsInGame){
			if(room.getRoomNumber() == newRoomIndex){
				return room;
			}
		}
		return null;
	}


	public static void main(String[] args){
		new Game();
	}



}