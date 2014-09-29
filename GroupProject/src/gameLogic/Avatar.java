package gameLogic;


import gameLogic.Game.Facing;

import java.util.ArrayList;
import java.util.List;

import rendering.Direction;
import networking.Move;


public class Avatar {

	Game game;

	Game.Facing facing;
	List <Item> Inventory;
	Room currentRoom;

	Tile2D currentTile; // current Tile the character is standing on

	String playerName;


	public Avatar(String name, Tile2D start, Game game){
		this.playerName = name;
		this.currentTile = start;
		this.currentRoom = start.getRoom();
		this.game = game;

		Inventory = new ArrayList<Item>();
		facing = Facing.North;
	}



	public boolean interact(Item item){
		//TODO check item range from character
		return item.interactWith(this) != null;
	}


	public boolean moveTo(Move move){
		Tile2D newPosition = null;

		int dir = Direction.get(move.getRenderDirection());
		int key = Direction.getKeyDirection(move.getInteraction());
		int change = dir + key;
		change = change % 4;

		if(change == 0) newPosition = currentTile.getTileUp();
		else if(change == 1) newPosition = currentTile.getTileRight();
		else if(change == 2) newPosition = currentTile.getTileDown();
		else if(change == 3) newPosition = currentTile.getTileLeft();

		// Ask the room the character is in if it can move to this tile
		if(currentRoom.checkValidMove(this,newPosition)==false) return false;

		// If Player is trying to pass through a door
		if(newPosition instanceof Door){
			Door door = (Door) newPosition;
			if(door.getRoom()!=currentRoom) return false;

			if(door.getLocked()){
				//TODO storing information about keys
			}

			int newRoomIndex = door.getToRoomIndex();
			int newX = door.getToRoomXPos();
			int newY = door.getToRoomYPos();

			if (changeRoom(newRoomIndex,newX,newY) == false) return false;


		}
		updateFacing(newPosition);
		updateTile(newPosition);
		return true;

	}

	public void updateTile(Tile2D newPosition){
		Tile2D oldTile = currentTile;
		currentTile = newPosition;

		oldTile.removePlayer(this);
		currentTile.addPlayer(this);
	}


	public void updateFacing(Tile2D newPosition){
		if(newPosition.equals(currentTile.getTileUp())) facing = Facing.North;
		else if(newPosition.equals(currentTile.getTileRight())) facing = Facing.East;
		else if(newPosition.equals(currentTile.getTileDown())) facing = Facing.South;
		else if(newPosition.equals(currentTile.getTileLeft())) facing = Facing.West;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game.Facing getFacing() {
		return facing;
	}

	public void setFacing(Game.Facing facing) {
		this.facing = facing;
	}

	public void setCurrentTile(Tile2D currentTile) {
		this.currentTile = currentTile;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Inventory == null) ? 0 : Inventory.hashCode());
		result = prime * result
				+ ((currentRoom == null) ? 0 : currentRoom.hashCode());
		result = prime * result
				+ ((currentTile == null) ? 0 : currentTile.hashCode());
		result = prime * result + ((facing == null) ? 0 : facing.hashCode());
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result
				+ ((playerName == null) ? 0 : playerName.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Avatar other = (Avatar) obj;
		if (Inventory == null) {
			if (other.Inventory != null)
				return false;
		} else if (!Inventory.equals(other.Inventory))
			return false;
		if (currentRoom == null) {
			if (other.currentRoom != null)
				return false;
		} else if (!currentRoom.equals(other.currentRoom))
			return false;
		if (currentTile == null) {
			if (other.currentTile != null)
				return false;
		} else if (!currentTile.equals(other.currentTile))
			return false;
		if (facing != other.facing)
			return false;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
	}
}
