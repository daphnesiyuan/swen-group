package gameLogic.gameState;

import gameLogic.entity.GameCharacter;
import gameLogic.location.Floor;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.physical.Item;

import java.util.ArrayList;
import java.util.List;

import networking.Move;


public class  Game {

	private List<Room> roomsInGame;
	private List<GameCharacter> activeCharacters;
	private List<Floor> spawnTiles;


	public enum Facing {
		North, South, East, West;

	}



	public Game(){
		roomsInGame = new ArrayList<Room>();
		activeCharacters = new ArrayList<GameCharacter>();
		spawnTiles = new ArrayList<Floor>();

		createNewGame();
	}

	private void createNewGame(){

		// newgame will set up the game, and populate this game object
		NewGame newgame = new NewGame(this);
	}

	public boolean moveCharacter(Move move){

		GameCharacter mover = null;

		for(GameCharacter character : activeCharacters){
			if(character.getPlayerName().equals(null/*move.getPlayer().getName()*/)){
				mover = character;
			}
		}
		if(mover==null) return false;
		return mover.moveTo(move);
	}

	public boolean characterInteractWithItem(String charName, Item item){
		for(GameCharacter character : activeCharacters){
			if(character.getPlayerName().equals(charName)){
				return character.interact(item);
			}
		}
		return false;
	}


	public Room getRoom(String playerName){
		for(Room room : roomsInGame){
			for(GameCharacter player : room.getCharacters()){
				if(player.getPlayerName().equals(playerName)){
					return player.getCurrentRoom();
				}
			}

		}
		return null;
	}



	public void setActiveCharacters(List<GameCharacter> activeCharacters) {
		this.activeCharacters = activeCharacters;
	}

	public List<Room> getRoomsInGame() {
		return roomsInGame;
	}


	public void setRoomsInGame(List<Room> roomsInGame) {
		this.roomsInGame = roomsInGame;
	}


	public List<GameCharacter> getActiveCharacters() {
		return activeCharacters;
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