package gameLogic.entity;


import gameLogic.gameState.NewGame;
import gameLogic.location.Door;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.physical.Item;

import java.util.ArrayList;
import java.util.List;


public class GameCharacter {
	String Description;

	Client client;

	List <? extends Item> Inventory;

	Room currentRoom;

	Tile2D currentTile;

	NewGame game;

	// all rooms in the house in this list
	private List<Room> roomsInGame;


	public GameCharacter(String name, Tile2D start, Room cr, NewGame n){
		currentTile = start;
		currentRoom = cr;
		game = n;

		Inventory = new ArrayList<Item>();

		roomsInGame = game.getRoomsInGame();


	}


	public boolean moveTo(Tile2D move){


		if(currentRoom.checkValidCharacterMove(this,move)==false) return false;

		// If Player is trying to pass through a door
		if(move instanceof Door){
			Door door = (Door)move;
			if(door.getRoom()!=currentRoom) return false;
			else{
				int newRoomIndex = door.getToRoomIndex();
				int newX = door.getToRoomXPos();
				int newY = door.getToRoomYPos();
				for(Room room : roomsInGame){
					if(room.getRoomNumber()== newRoomIndex){
						// if find room
					}
				}
				// if room doesnt change
			}

		}

		currentTile = move;
		return true;

	}

	public boolean interact(int x, int y, String action){
		return false;

	}


	public Tile2D getCurrentTile() {
		return currentTile;
	}


	public Room getCurrentRoom() {
		return currentRoom;
	}
}
