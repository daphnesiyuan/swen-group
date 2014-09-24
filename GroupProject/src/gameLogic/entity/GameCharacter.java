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


	List <? extends Item> Inventory;

	Room currentRoom;

	Tile2D currentTile;

	Game game;


	public GameCharacter(String name, Tile2D start, Game n){
		currentTile = start;
		currentRoom = start.getRoom();
		game = n;

		Inventory = new ArrayList<Item>();



	}


	public boolean moveTo(Tile2D move){


		if(currentRoom.checkValidCharacterMove(this,move)==false) return false;

		// If Player is trying to pass through a door
		if(move instanceof Door){
			Door door = (Door)move;
			if(door.getRoom()!=currentRoom) return false;

			if(door.getLocked()){

			}


			else{
				int newRoomIndex = door.getToRoomIndex();
				int newX = door.getToRoomXPos();
				int newY = door.getToRoomYPos();

				Room newRoom = game.getRoom(newRoomIndex);

				//Room does not exist in game
				if(newRoom==null) return false;

				return changeRoom(newRoomIndex,newX,newY);
			}

		}

		currentTile = move;
		return true;

	}

	private boolean changeRoom(int newRoomIndex, int newX, int newY){


		return false;
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
