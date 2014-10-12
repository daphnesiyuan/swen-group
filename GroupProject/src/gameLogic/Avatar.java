package gameLogic;


import gameLogic.Game.Facing;

import java.awt.Color;
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

	private Cell cell;

	// Avatar's coordinates relative to the room - global.
	private double globalXPos, globalYPos;

	// Avatar's coordinates relative to the tile - local.
	private double tileXPos, tileYPos;


	// Tile dimensions
	private final double tileWidth = 100;
	private final double tileHeight = 100;

	// Tile Centers
	private final double tileXCenter = tileWidth / 2 ;
	private final double tileYCenter = tileHeight / 2;


	//If Avatar moves to adjacent tile their local tile coordinates are set with these fields
	private final double tileMinPos = 1;
	private final double tileMaxPos = 100;

	// The amount the Avatar moves with each key press
	private int stepAmount = 50;


	// While the sprite is animating, spriteIndex will hold the index to the current frame to be displayed for the animation.
	private int spriteIndex;

	//Is this Avatar object a Player, or an AI
	private boolean isAI;

	public Avatar(String name, Tile2D tile, Room room){
		this.playerName = name;

		this.updateLocations(tile, room);

		this.Inventory = new ArrayList<Item>();
		this.facing = Facing.North;

		this.cell = new Cell(this);


		// Avatar start coordinates initialized to the middle of its starting tile
		this.globalXPos = currentTile.getxPos()+(tileWidth/2);
		this.globalYPos = currentTile.getyPos()+(tileHeight/2);

		// Avatars relative tile coordinates are initalized to the center of the tile
		this.tileXPos = (tileWidth/2);
		this.tileYPos = (tileHeight/2);

		// Avatars initial sprite image is the 0th element in the animation sequence
		this.spriteIndex = 0;

		if(name.startsWith("ai")){
			this.isAI=true;
		}else{
			this.isAI = false;
		}

	}


	/**
	 * When called, the Avatars tileXPos and tileYPos are set to 50. The global coordinates are updated respectivley.
	 * This method is used to center the avatar on a tile.
	 */
	private void centerAvatar(){
		double xChange = tileXCenter - tileXPos;
		double yChange = tileYCenter - tileYPos;
		tileXPos = tileXCenter;
		tileYPos = tileYCenter;
		globalXPos += xChange;
		globalYPos += yChange;
	}

	public boolean isAI() {
		return isAI;
	}


	public void setAI(boolean isAI) {
		this.isAI = isAI;
	}


	public void updateLocations(Tile2D tile, Room room) {
		updateTile(tile);
		updateRoom(room);
	}

	private void updateTile(Tile2D newTile){
		if(newTile.equals(currentTile)) return;
		if(currentTile != null) currentTile.removeAvatar(this);
		newTile.addAvatar(this);
		currentTile = newTile;
	}

	private void updateRoom(Room newRoom){
		if(newRoom.equals(currentRoom)) return;
		if(currentRoom != null)	currentRoom.removeAvatar(this);
		newRoom.addAvatar(this);
		currentRoom = newRoom;
	}



	public boolean interact(Item item){
		// check item within range before interacting -> (absolute value of the difference between location coordiantes)
		if(Math.abs(item.getTile().getxPos()-currentTile.getxPos())>1) return false;
		if(Math.abs(item.getTile().getyPos()-currentTile.getyPos())>1) return false;
		return item.interactWith(this);
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

		if(move.getInteraction().equals("O")){
			charge();
			cell.setCharging(false);
		}

		updateFacing(move.getInteraction());

		int change = calcDirection(move);
		Tile2D newPosition = null;
		if(change == 0) newPosition = moveUp(currentTile.getTileUp());
		else if(change == 1) newPosition = moveRight(currentTile.getTileRight());
		else if(change == 2) newPosition = moveDown(currentTile.getTileDown());
		else if(change == 3) newPosition = moveLeft(currentTile.getTileLeft());

		// newPosition invalid or not found - ie if movement up is a wall
		if(newPosition == null){
			return false;
		}

		if(newPosition.getAvatar() != null) return false; // CANNOT move to a tile if there is another player on it
		if(newPosition instanceof Column) return false; // CANNOT pass through columuns
		if(newPosition instanceof Door) return moveDoor((Door) newPosition);// If Player is trying to pass through a door
		else{
			updateLocations(newPosition,currentRoom);
			cell.useBattery();
			animation();
			if(currentTile.getItems().size() != 0) interact(currentTile.getItems().get(0));
			return true;
		}





	}
	/**
	 *  Calculate direction to be moved
	 * @param move object - holds information about key press and rendering direction
	 * @return change - the direction the avatar will move with 0,1,2,3 representing North, East, South and West respectively
	 */
	private int calcDirection(Move move){
		int dir = Direction.get(move.getRenderDirection());
		int key = Direction.getKeyDirection(move.getInteraction());
		int change = dir + key;
		change = change % 4;
		return change;
	}

	private void animation(){
		spriteIndex++;
		spriteIndex = spriteIndex % 4; // 4 images but 0 indexed.
	}

	/**
	 *
	 * @param tile2d = the Tile above the current tile
	 * @return tile2d = returns the current tile if the movement made keeps the avatar on the same tile, else returns the tile above the current tile.
	 */
	private Tile2D moveUp(Tile2D tileUp){
		globalYPos-=stepAmount;
		tileYPos-=stepAmount;

		if(tileYPos<tileMinPos){
			if(!(( tileUp instanceof Floor ) || (tileUp instanceof Door ))){ // cant actually move here so undo changes to position and return null
				globalYPos+=stepAmount;
				tileYPos+=stepAmount;
				return null;
			}
			tileYPos = tileMaxPos;
			return tileUp;
		}
		else{
			return currentTile;

		}
	}

	private Tile2D moveDown(Tile2D tileDown){
		globalYPos+=stepAmount;
		tileYPos+=stepAmount;

		if(tileYPos>tileHeight){
			if(!((tileDown instanceof Floor ) || (tileDown instanceof Door ))){ // cant actually move here so undo changes to position and return null
				globalYPos-=stepAmount;
				tileYPos-=stepAmount;
				return null;
			}
			tileYPos = tileMinPos;
			return tileDown;
		}
		else{
			return currentTile;
		}
	}

	private Tile2D moveLeft(Tile2D tileLeft){
		globalXPos-=stepAmount;
		tileXPos-=stepAmount;
		if(tileXPos<tileMinPos){

			if(!(( tileLeft instanceof Floor ) || (tileLeft instanceof Door ))){ // cant actually move here so undo changes to position and return null
				globalXPos+=stepAmount;
				tileXPos+=stepAmount;
				return null;
			}
			tileXPos = tileMaxPos;
			return tileLeft;
		}
		else{
			return currentTile;
		}
	}

	private Tile2D moveRight(Tile2D tileRight){
		globalXPos+=stepAmount;
		tileXPos+=stepAmount;
		if(tileXPos>tileWidth){

			if(!(( tileRight instanceof Floor ) || (tileRight instanceof Door ))){// cant actually move here so undo changes to position and return null
				globalXPos-=stepAmount;
				tileXPos-=stepAmount;
				return null;
			}
			tileXPos = tileMinPos;
			return tileRight;
		}
		else{
			return currentTile;
		}
	}

	private boolean moveDoor(Door oldPosition){	//oldposition is the door the avatar stepped onto
		if(currentRoom.getRoomPlace().equals("arena")){	// if avatar is leaving arena
			Room room  = oldPosition.getToRoom();
			Door door = room.getDoors().get(0);
			updateLocations(door, room);
		}
		else{											// avatar is going into arena
			Room arena = oldPosition.getToRoom();
			Door door = null;
			if(oldPosition.getRoom().getRoomPlace().equals("north")) door = arena.getDoors().get(0);
			else if(oldPosition.getRoom().getRoomPlace().equals("south")) door = arena.getDoors().get(3);
			else if(oldPosition.getRoom().getRoomPlace().equals("east")) door = arena.getDoors().get(2);
			else if(oldPosition.getRoom().getRoomPlace().equals("west")) door = arena.getDoors().get(1);
			updateLocations(door, arena);

		}
		cell.useBattery();

		animation();
		return true;

	}




	/**
	 * 	Update the direction the avatar is facing based to what movement key was pressed.
	 * @param dirKey - string representing the movement key that were pressed.
	 */
	private void updateFacing(String dirKey){
		if(dirKey.toLowerCase().equals("w")) facing = Facing.North;
		else if(dirKey.toLowerCase().equals("d")) facing = Facing.East;
		else if(dirKey.toLowerCase().equals("s")) facing = Facing.South;
		else if(dirKey.toLowerCase().equals("a")) facing = Facing.West;
	}

	private void attack(){

	}

	private void charge(){
		cell.setCharging(true);
		cell.chargeBattery();
		System.out.println(cell.getBatteryLife());
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
		return cell.getBatteryLife();
	}

	public void setCharging(boolean charging){
		this.cell.setCharging(charging);
	}

	public int getSpriteIndex(){
		return spriteIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
	}


	public Cell getCell() {
		return cell;
	}


	public void setCell(Cell cell) {
		this.cell = cell;
	}


}
