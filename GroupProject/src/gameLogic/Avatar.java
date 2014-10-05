package gameLogic;


import gameLogic.Game.Facing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rendering.Direction;
import networking.Move;


public class Avatar implements Serializable {

	private static final long serialVersionUID = 4723069455200795911L;

	private Game.Facing facing;
	private List <Item> Inventory;


	private Tile2D currentTile;
	private Room currentRoom;

	private String playerName;

	private Battery battery;

	// coordinates used for animation
	private double globalXPos, globalYPos;
	private double tileXPos, tileYPos;


	// TODO tile dimensions needed
	private final double tileWidth = 10;
	private final double tileHeight = 10;



	public Avatar(String name, Tile2D tile, Room room){
		this.playerName = name;

		updateLocations(tile, room);

		Inventory = new ArrayList<Item>();
		facing = Facing.North;

		battery = new Battery(this);


		// Avatar start coordinates initialized to the middle of its starting tile
		globalXPos = currentTile.getxPos()+(tileWidth/2);
		globalYPos = currentTile.getyPos()+(tileHeight/2);

		// Avatars relative tile coordinates are initalized to the center of the tile
		tileXPos = (tileWidth/2);
		tileYPos = (tileHeight/2);


	}

	public double getBatteryLife(){
		return battery.getBatteryLife();
	}


	public void updateLocations(Tile2D tile, Room room) {
		updateTile(tile);
		updateRoom(room);
	}

	public void updateTile(Tile2D newTile){
		if(newTile.equals(currentTile)) return;
		if(currentTile != null) currentTile.removeAvatar(this);
		newTile.addAvatar(this);
		currentTile = newTile;
	}

	public void updateRoom(Room newRoom){
		if(newRoom.equals(currentRoom)) return;
		if(currentRoom != null)	currentRoom.removeAvatar(this);
		newRoom.addAvatar(this);
		currentRoom = newRoom;
	}



	public boolean interact(Item item){
		//TODO check item range from character
		return item.interactWith(this) != null;
	}


	public boolean moveTo(Move move){
		if(move.getRenderDirection() == null){
			System.out.println("Avatar: moveTo() - RenderDirection in provided move object is null");
			return false;
		}
		if(move.getInteraction() == null){
			System.out.println("Avatar: moveTo() - Interaction in provided move object is null");
			return false;
		}

		// Calculate direction to be moved
		int dir = Direction.get(move.getRenderDirection());
		int key = Direction.getKeyDirection(move.getInteraction());
		int change = dir + key;
		change = change % 4;


		Tile2D newPosition = null;


		if(change == 0) newPosition = moveUp(currentTile.getTileUp());
		else if(change == 1) newPosition = moveRight(currentTile.getTileRight());
		else if(change == 2) newPosition = moveDown(currentTile.getTileDown());
		else if(change == 3) newPosition = moveLeft(currentTile.getTileLeft());

		if(newPosition == null) System.out.println("Avatar: moveTo() - Problem locating move to Tile - newPostion not found");



		// if the move is in a different room to the characters current room - return false NB: moving through door moves onto tile, which IS in same room.
		if(newPosition.getRoom()!= this.currentRoom) return false;



		// If Player is trying to pass through a door
		if(newPosition instanceof Door) newPosition = moveDoor(newPosition);


		updateFacing(newPosition);

		updateLocations(newPosition,currentRoom);


		battery.iMoved();
		return true;

	}

	/**
	 *
	 * @param tile2d = the Tile above the current tile
	 * @return tile2d = returns the current tile if the movement made keeps the avatar on the same tile, else returns the tile above the current tile.
	 */
	public Tile2D moveUp(Tile2D tileUp){
		globalYPos--;
		tileYPos--;

		if(tileYPos>tileHeight){
			tileYPos = tileYPos % tileHeight;
			if(tileUp instanceof Wall){
				globalYPos--;
				tileYPos--;
				return null;
			}
			return tileUp;
		}
		else{
			return currentTile;

		}
	}

	public Tile2D moveDown(Tile2D tileDown){
		globalYPos++;
		tileYPos++;


		return null;
	}

	public Tile2D moveLeft(Tile2D tileLeft){
		globalXPos--;
		tileXPos--;

		return null;

	}

	public Tile2D moveRight(Tile2D tileRight){
		globalXPos++;
		tileXPos++;


		return null;
	}

	public Tile2D moveDoor(Tile2D tileDoor){
		Door door = (Door) tileDoor;

		if(door.getLocked()){
			/*For when Doors and keys are implemented*/
		}

		int newRoomIndex = door.getToRoomIndex();
		int newX = door.getToRoomXPos();
		int newY = door.getToRoomYPos();

		//if (changeRoom(newRoomIndex,newX,newY) == false) return false;

		return tileDoor;
	}





	public void updateFacing(Tile2D newPosition){
		if(newPosition.equals(currentTile.getTileUp())) facing = Facing.North;
		else if(newPosition.equals(currentTile.getTileRight())) facing = Facing.East;
		else if(newPosition.equals(currentTile.getTileDown())) facing = Facing.South;
		else if(newPosition.equals(currentTile.getTileLeft())) facing = Facing.West;
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
		final int prime = 41;
		int result = 1;
		result = prime * result
				+ ((Inventory == null) ? 0 : Inventory.hashCode());

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
		if (facing != other.facing)
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
	}

}
