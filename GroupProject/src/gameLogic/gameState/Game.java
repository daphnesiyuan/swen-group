package gameLogic.gameState;

import gameLogic.entity.GameCharacter;
import gameLogic.location.Floor;
import gameLogic.location.Room;

import java.util.ArrayList;
import java.util.List;






public class  Game {

	private List<Room> roomsInGame;
	private List<GameCharacter> activeCharacters;
	private List<Floor> spawnTiles;


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