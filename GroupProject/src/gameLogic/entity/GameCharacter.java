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

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}


	Room currentRoom;




	Tile2D currentTile; // current Tile the character is standing on
	
	Tile2D oldTile;		// Field used for when player moves off of tile, oldTile then removes the player from its charactersOnTile

	Game game;

	String name;


	public GameCharacter(String n, Tile2D start, Game g){
		name = n;
		currentTile = start;
		currentRoom = start.getRoom();
		game = g;

		Inventory = new ArrayList<Item>();




		facing = Facing.North;
		updateTile();
	}

	public String getPlayerName(){
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

		// Character updates the tile its standing on, but also lets the tile know that it is now standing on it
		// (tiles keep track of characters on them too )
		
		
		oldTile = currentTile;	// remove player from old tile
		
		currentTile = newPosition;
		updateTile();
		return updateFacing(move);

	}
	
	public void updateTile(){
		oldTile.removePlayer(this);
		currentTile.addPlayer(this);
		
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

	public boolean updateFacing(Move move){
		// needs thought - rotatable room + factor of current facing direction

		if(move.getInteraction().equals("W")) facing = Facing.North;
		else if(move.getInteraction().equals("A")) facing = Facing.West;
		else if(move.getInteraction().equals("S")) facing = Facing.South;
		else if(move.getInteraction().equals("D")) facing = Facing.East;
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
