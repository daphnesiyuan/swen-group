package gameLogic.entity;


import gameLogic.gameState.Game;
import gameLogic.gameState.NewGame;
import gameLogic.location.Door;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.physical.Item;

import java.util.ArrayList;
import java.util.List;


public class GameCharacter {
	String Description;


	List <Item> Inventory;

	public List<Item> getInventory() {
		return Inventory;
	}

	public void setInventory(List<Item> inventory) {
		Inventory = inventory;
	}


	Room currentRoom;

	Tile2D currentTile;

	Game game;

	String name;


	public GameCharacter(String n, Tile2D start, Game g){
		name = n;
		currentTile = start;
		currentRoom = start.getRoom();
		game = g;

		Inventory = new ArrayList<Item>();



	}

	public String getName(){
		return name;

	}




	public boolean moveTo(Tile2D move){


		if(currentRoom.checkValidCharacterMove(this,move)==false) return false;

		// If Player is trying to pass through a door
		if(move instanceof Door){
			Door door = (Door)move;
			if(door.getRoom()!=currentRoom) return false;

			if(door.getLocked()){
				//TODO storing information about keys etc - Antonia
			}

			int newRoomIndex = door.getToRoomIndex();
			int newX = door.getToRoomXPos();
			int newY = door.getToRoomYPos();

			return changeRoom(newRoomIndex,newX,newY);


		}

		currentTile = move;
		return true;

	}

	private boolean changeRoom(int newRoomIndex, int newX, int newY){
		List<Room> rooms = game.getRoomsInGame();
		for(Room room : rooms){
			if(room.getRoomNumber()==newRoomIndex){
				currentRoom = room;
				return true;
			}
		}
		return false;
	}




	public boolean interact(Item item){
		//TODO check item range from character
		return item.interactWith(this) != null;
	}


	public Tile2D getCurrentTile() {
		return currentTile;
	}


	public Room getCurrentRoom() {
		return currentRoom;
	}
}
