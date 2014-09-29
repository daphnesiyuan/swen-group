package gameLogic;


import gameLogic.Game.Facing;

import java.util.ArrayList;
import java.util.List;

import networking.Move;


public class Avatar {

	Game game;

	Game.Facing facing;
	List <Item> Inventory;
	Room currentRoom;

	Tile2D currentTile; // current Tile the character is standing on
	Tile2D oldTile;		// Field used for when player moves off of tile, oldTile then removes the player from its charactersOnTile

	String playerName;


	public Avatar(String name, Tile2D start, Game game){
		this.playerName = name;
		this.currentTile = start;
		this.currentRoom = start.getRoom();
		this.game = game;

		Inventory = new ArrayList<Item>();
		facing = Facing.North;

		updateTile();
	}



	public boolean interact(Item item){
		//TODO check item range from character
		return item.interactWith(this) != null;
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



		oldTile = currentTile;	// remove player from old tile

		currentTile = newPosition;
		updateTile();
		return updateFacing(move);

	}

	public void updateTile(){
		if(oldTile != null){
			oldTile.removePlayer(this);
		}
		currentTile.addPlayer(this);
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




	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public String getPlayerName(){
		return playerName;
	}

	public Tile2D getCurrentTile() {
		return currentTile;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public List<Item> getInventory() {
		return Inventory;
	}

	public void setInventory(List<Item> inventory) {
		Inventory = inventory;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public Game.Facing getDirectionFacing(){
		return facing;
	}

	public void setDirectionFacing(Game.Facing f){
		facing = f;
	}
}
