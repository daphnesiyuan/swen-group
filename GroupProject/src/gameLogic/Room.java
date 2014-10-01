package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Room implements Serializable {


	private static final long serialVersionUID = -6429120453292131025L;

	Tile2D[][] tiles;
	List <Item> items;
	List<Avatar> avatars;
	List<Door> doors;
	List<Floor> floors;
	List<Wall> walls;

	int roomNumber;



	public Room(int roomNumber, Tile2D[][] tiles, List<Item> items) {
		this.roomNumber = roomNumber;
		this.tiles = tiles;
		this.items = items;

		avatars = new ArrayList<Avatar>();

	}

	public Avatar getAvatar(String playerName){
		for(Avatar avatar: avatars){
			if(playerName.equals(avatar.getPlayerName())){
				return avatar;
			}
		}
		return null;
	}




	public void removeAvatar(Avatar avatar){
		avatars.remove(avatar);
	}

	public void addAvatar(Avatar avatar){
		avatars.add(avatar);
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


	public List<Wall> getWalls() {
		return walls;
	}

	public void setTiles(Tile2D[][] tiles) {
		this.tiles = tiles;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatars == null) ? 0 : avatars.hashCode());
		result = prime * result + ((doors == null) ? 0 : doors.hashCode());
		result = prime * result + ((floors == null) ? 0 : floors.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + roomNumber;
		result = prime * result + Arrays.hashCode(tiles);
		result = prime * result + ((walls == null) ? 0 : walls.hashCode());
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
		Room other = (Room) obj;
		if (avatars == null) {
			if (other.avatars != null)
				return false;
		} else if (!avatars.equals(other.avatars))
			return false;
		if (doors == null) {
			if (other.doors != null)
				return false;
		} else if (!doors.equals(other.doors))
			return false;
		if (floors == null) {
			if (other.floors != null)
				return false;
		} else if (!floors.equals(other.floors))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (roomNumber != other.roomNumber)
			return false;
		if (!Arrays.deepEquals(tiles, other.tiles))
			return false;
		if (walls == null) {
			if (other.walls != null)
				return false;
		} else if (!walls.equals(other.walls))
			return false;
		return true;
	}
}
