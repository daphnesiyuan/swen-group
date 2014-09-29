package gameLogic;

import java.util.List;


public class Room {

	Tile2D[][] tiles;
	List <? extends Item> items;
	List<Avatar> characters;

	public List<Avatar> getCharacters() {
		return characters;
	}

	public void setCharacters(List<Avatar> characters) {
		this.characters = characters;
	}

	List<Door> doors;
	List<Floor> floors;
	List<Floor> spawns;
	List<Wall> walls;

	int roomNumber;



	public Room(int r, Tile2D[][] roomTiles, List<? extends Item> roomitems) {
		roomNumber = r;
		tiles = roomTiles;
		items = roomitems;
	}

	public Item getItemAt(int x, int y){
		return tiles[x][y].getTopItem();
	}

	public Tile2D[][] getTiles(){
		return tiles;
	}


	public boolean checkValidCharacterMove(Avatar mover, Tile2D move) {

		// if the move is the characters current square - return false
		if(mover.getCurrentTile().equals(move)) return false;

		// if the move is in a different room to the characters current room - return false
		if(move.getRoom()!= mover.getCurrentRoom()) return false;

		// if move position is a wall - return false
		if(move instanceof Wall) return false;

		// if there is an Item in the move position - return false;
		if(move.canMoveTo()==false) return false;


		return true;
	}

	public void setDoors(List<Door> roomDoors) {
		doors = roomDoors;

	}

	public void setFloors(List<Floor> roomFloors) {
		floors = roomFloors;

	}

	public void setSpawns(List<Floor> roomSpawns) {
		spawns = roomSpawns;

	}

	public void setWalls(List<Wall> roomWalls) {
		walls = roomWalls;

	}

	public int getRoomNumber(){
		return roomNumber;
	}


}
