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

	// Avatars coordinates relative to the room - global.
	private double globalXPos, globalYPos;

	// Avatars coordinates relative to the tile - lovcal.
	private double tileXPos, tileYPos;


	// Tile dimensions
	private final double tileWidth = 100;
	private final double tileHeight = 100;


	//If avatar moves to adjacent tile their local tile coordinates are set with these fields
	private final double tileMinPos = 1;
	private final double tileMaxPos = 100;


	// The total number of images the animation sequence will cycle through
	private final int spriteImages = 4;

	// While the sprite is animating, spriteIndex will hold the index to the current frame to be displayed for the animation.
	private int spriteIndex;


	//TODO - for animation of moving heads to charge like socket, create system wherein Avatar sprite is moved to the center of a tile,
	// regardless of their current x and y tile coordinates.


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

		// Avatars initial sprite image is the 0th element in the animation sequence
		spriteIndex = 0;

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
		// check item within range before interacting -> (absolute value of the difference between location coordiantes)
		if(Math.abs(item.getTile().getxPos()-currentTile.getxPos())>1) return false;
		if(Math.abs(item.getTile().getyPos()-currentTile.getyPos())>1) return false;
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

		int change = calcDirection(move);
		Tile2D newPosition = null;
		if(change == 0) newPosition = moveUp(currentTile.getTileUp());
		else if(change == 1) newPosition = moveRight(currentTile.getTileRight());
		else if(change == 2) newPosition = moveDown(currentTile.getTileDown());
		else if(change == 3) newPosition = moveLeft(currentTile.getTileLeft());

		// newPosition invalid or not found - ie if movement up is a wall
		if(newPosition == null){
			System.out.println("Avatar: moveTo() - Problem locating move to Tile - newPostion not found");
			return false;
		}
		if(newPosition instanceof Door) newPosition = moveDoor(newPosition); // If Player is trying to pass through a door

		updateFacing(move.getInteraction());
		updateLocations(newPosition,currentRoom);

		battery.iMoved();
		animation();
		return true;

	}
	/**
	 *  Calculate direction to be moved
	 * @param move object - holds information about key press and rendering direction
	 * @return change - the direction the avatar will move with 0,1,2,3 representing North, East, South and West respectively
	 */
	public int calcDirection(Move move){
		int dir = Direction.get(move.getRenderDirection());
		int key = Direction.getKeyDirection(move.getInteraction());
		int change = dir + key;
		change = change % 4;
		return change;
	}

	public void animation(){
		spriteIndex++;
		spriteIndex = spriteIndex % 4; // 4 images but 0 indexed.
	}

	/**
	 *
	 * @param tile2d = the Tile above the current tile
	 * @return tile2d = returns the current tile if the movement made keeps the avatar on the same tile, else returns the tile above the current tile.
	 */
	public Tile2D moveUp(Tile2D tileUp){
		globalYPos-=5;
		tileYPos-=5;

		if(tileYPos<tileMinPos){
			tileYPos = tileMaxPos;	 // might not work coz logic
			// tileYPos = tileYPos % tileHeight;
			if(tileUp instanceof Wall){
				// cant actually move here so undo changes to position and return null
				globalYPos+=5;
				tileYPos+=5;
				return null;
			}
			return tileUp;
		}
		else{
			return currentTile;

		}
	}

	public Tile2D moveDown(Tile2D tileDown){
		globalYPos+=5;
		tileYPos+=5;

		if(tileYPos>tileHeight){
			tileYPos = tileMinPos;
			if(tileDown instanceof Wall){
				// cant actually move here so undo changes to position and return null
				globalYPos-=5;
				tileYPos-=5;
				return null;
			}
			return tileDown;
		}
		else{
			return currentTile;
		}
	}

	public Tile2D moveLeft(Tile2D tileLeft){
		globalXPos-=5;
		tileXPos-=5;
		if(tileXPos<tileMinPos){
			tileXPos = tileMaxPos;
			if(tileLeft instanceof Wall){
				// cant actually move here so undo changes to position and return null
				globalXPos+=5;
				tileXPos+=5;
				return null;
			}
			return tileLeft;
		}
		else{
			return currentTile;
		}
	}

	public Tile2D moveRight(Tile2D tileRight){
		globalXPos+=5;
		tileXPos+=5;
		if(tileXPos>tileWidth){
			tileXPos = tileMinPos;
			if(tileRight instanceof Wall){
				// cant actually move here so undo changes to position and return null
				globalXPos-=5;
				tileXPos-=5;
				return null;
			}
			return tileRight;
		}
		else{
			return currentTile;
		}
	}

	public Tile2D moveDoor(Tile2D tileDoor){
		Door door = (Door) tileDoor;

		if(door.getLocked()){
			/*For when Doors and keys are implemented*/
		}

		//		int newRoomIndex = door.getToRoomIndex();
		//		int newX = door.getToRoomXPos();
		//		int newY = door.getToRoomYPos();

		//if (changeRoom(newRoomIndex,newX,newY) == false) return false;


		//updateLocations(...will be a different room here);
		return tileDoor;
	}




	/**
	 * 	Update the direction the avatar is facing based to what movement key was pressed.
	 * @param dirKey - string representing the movement key that were pressed.
	 */
	public void updateFacing(String dirKey){
		if(dirKey.toLowerCase().equals("w")) facing = Facing.North;
		else if(dirKey.toLowerCase().equals("D")) facing = Facing.East;
		else if(dirKey.toLowerCase().equals("S")) facing = Facing.South;
		else if(dirKey.toLowerCase().equals("A")) facing = Facing.West;
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

	public double getTileXPos(){
		return tileXPos;
	}

	public double getTileYPos(){
		return tileYPos;
	}

	public double getGlobalXPos(){
		return globalXPos;
	}

	public double getGlobalYPos(){
		return globalYPos;
	}

	public double getBatteryLife(){
		return battery.getBatteryLife();
	}

	public int getSpriteIndex(){
		return spriteIndex;
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
