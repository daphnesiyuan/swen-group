package gameLogic;

import java.util.List;


public class Room {

	Tile2D[][] tiles;
	List <Item> items;
	List<Avatar> avatars;
	List<Door> doors;
	List<Floor> floors;
	List<Floor> spawns;
	List<Wall> walls;

	int roomNumber;



	public Room(int roomNumber, Tile2D[][] tiles, List<Item> items) {
		this.roomNumber = roomNumber;
		this.tiles = tiles;
		this.items = items;
	}

	public boolean checkValidMove(Avatar mover, Tile2D move) {

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

	public Item getItemAt(int x, int y){
		return tiles[x][y].getTopItem();
	}

	public Tile2D[][] getTiles(){
		return tiles;
	}

	public List<Avatar> getAvatars() {
		return avatars;
	}

	public void setAvatars(List<Avatar> avatars) {
		this.avatars = avatars;
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

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Door> getDoors() {
		return doors;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public List<Floor> getSpawns() {
		return spawns;
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public void setTiles(Tile2D[][] tiles) {
		this.tiles = tiles;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
}
