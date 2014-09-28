package gameLogic.entity;


import gameLogic.gameState.Game;
import gameLogic.gameState.Game.Facing;
import gameLogic.gameState.NewGame;
import gameLogic.location.Door;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.physical.Item;

import java.util.ArrayList;
import java.util.List;

import networking.Move;


public class GameCharacter {
	String Description;

	Game.Facing facing;


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




		facing = Facing.North;
	}

	public String getName(){
		return name;

	}

	public Game.Facing getDirectionFacing(){
		return facing;

	}

	public void setDirectionFacing(Game.Facing f){
		facing = f;
	}




	public boolean moveTo(Move move){
		Tile2D newPosition = null;

		if(move.getInteraction().equals("W")) newPosition = currentTile.getTileUp();
		else if(move.getInteraction().equals("A")) newPosition = currentTile.getTileLeft();
		else if(move.getInteraction().equals("S")) newPosition = currentTile.getTileRight();
		else if(move.getInteraction().equals("D")) newPosition = currentTile.getTileDown();




		if(currentRoom.checkValidCharacterMove(this,newPosition)==false) return false;

		// If Player is trying to pass through a door
		if(newPosition instanceof Door){
			Door door = (Door) newPosition;
			if(door.getRoom()!=currentRoom) return false;

			if(door.getLocked()){
				//TODO storing information about keys etc - Antonia
			}

			int newRoomIndex = door.getToRoomIndex();
			int newX = door.getToRoomXPos();
			int newY = door.getToRoomYPos();

			return changeRoom(newRoomIndex,newX,newY);


		}

		currentTile = newPosition;
		return updateFacing(move);

	}

	public boolean updateFacing(Move move){
		// needs thought - rotatable room + factor of current facing direction

//		if(move.getInteraction().equals(/*"W"*/null)) facing = Facing.North;
//		else if(move.getInteraction().equals(/*"A"*/null)) facing = Facing.West;
//		else if(move.getInteraction().equals(/*"S"*/null)) facing = Facing.South;
//		else if(move.getInteraction().equals(/*"D"*/null)) facing = Facing.East;
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
