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

	private String playerName;
	private Tile2D tile;
	private Room room;
	private Game.Facing facing;
	private String renderDirection = "North";

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
	private int stepAmount = 32;


	// While the sprite is animating, spriteIndex will hold the index to the current frame to be displayed for the animation.
	private int spriteIndex;

	//Is this Avatar object a Player, or an AI
	private boolean isAI;

	// Locations for when Avatar dies, can be returned to here.
	private Tile2D startTile;
	private Room startRoom;

	// The avatars current kill score
	private int score;

	// To tell the avatar that killed you to increment their score, also if multiple people hitting you, the score goes to the person who hit you last.
	private Avatar lastHit;

	private int maxCharge = 500;
	private int damage = 125;

	private Cell cell;

	private List <Item> inventory;

	public Avatar(String name, Tile2D tile, Room room){
		this.playerName = name;
		this.startTile = tile;
		this.startRoom = room;

		updateLocations(tile, room);

		this.inventory = new ArrayList<Item>();
		this.facing = Facing.North;

		this.cell = new Cell(this);

		// Avatars relative tile coordinates are initalized to the center of the tile
		this.tileXPos = (tileWidth/2);
		this.tileYPos = (tileHeight/2);

		// Avatars initial sprite image is the 0th element in the animation sequence
		this.spriteIndex = 0;

		if(name.startsWith("ai")){
			stepAmount = 102;
			this.isAI=true;
		}
		else this.isAI = false;

		this.score = 0;
		this.lastHit = null;
	}
	/**
	 * Every time an avatar move is performed, this method is called to update the avatars current room and tile postitions.
	 * @param tile the new Tile position of the avatar.
	 * @param room the new Room of the avatar.
	 */
	public void updateLocations(Tile2D tile, Room room) {

		updateTile(tile);
		updateRoom(room);
	}

	private void updateTile(Tile2D newTile){
		if(newTile.equals(tile)) return;
		if(tile != null) tile.removeAvatar(this);
		newTile.addAvatar(this);
		tile = newTile;
	}

	private void updateRoom(Room newRoom){
		if(newRoom.equals(room)) return;
		if(room != null)	room.removeAvatar(this);
		newRoom.addAvatar(this);
		room = newRoom;
	}

	public boolean interact(Item item){
		return item.interactWith(this);
	}

	public boolean useItem(Item item){
		if((item == null)||(!(inventory.contains(null)))) return false;
		return true;


	}

	/**
	 *  Move the item from the avatars inventory
	 * @param move
	 * @return true iff the item is removed succesfully
	 */
	public boolean dropItem(Move move){
		Tile2D dropTile = null;
		// Find a Tile close to the avatar that the item can be dropped on
		if(tile.getTileUp() != tile && tile.getTileUp() instanceof Floor) dropTile = tile.getTileUp();
		else if(tile.getTileLeft() != tile && tile.getTileLeft() instanceof Floor) dropTile = tile.getTileLeft();
		else if(tile.getTileRight() != tile && tile.getTileRight() instanceof Floor) dropTile = tile.getTileRight();
		else if (tile.getTileDown() != tile && tile.getTileDown() instanceof Floor) dropTile = tile.getTileDown();
		else return false;

		int remove = move.getIndex();
		if(remove >= inventory.size()) return false;
		Item item = inventory.get(remove);
		inventory.remove(item);

		item.moveItemTo(dropTile);
		if(item instanceof Shoes) stepAmount = 32;
		return true;
	}

	public void emptyInventory(){
		for(Item item : inventory){
			item.returnToStartPos();
		}
		this.inventory = new ArrayList<Item>();
	}


	public boolean openItem(Move move){
		Item item = inventory.get(move.getIndex());
		if(item instanceof Box){
			Box box = (Box)item;
			if(box.getContains().size() == 0){
				System.out.println("Theres nothing in here!");
				return false;
			}
			Item inner = box.getContains().get(0);
			box.getContains().remove(inner);
			return this.interact(inner);
		}



		return false;
	}
	/**
	 * Move position is calculated using the give Move object. The moveTo() method finds the new position and checks to see if the avatar can move there.
	 * @param move
	 * @return true if the move is completed succesfully
	 */
	public boolean moveTo(Move move){
		if(move.getInteraction().equals("drop")) return dropItem(move);
		if(move.getInteraction().equals("open")) return openItem(move);
		if(move.getRenderDirection() == null) return false;
		if(move.getInteraction() == null) return false;


		updateFacing(move.getInteraction());

		if(move.getInteraction().equals("O")){
			cell.setCharging(!cell.isCharging());
			charge(findTile());
			return false;
		}
		this.renderDirection = move.getRenderDirection();

		//change direction without moving
		if (move.getInteraction().equals("")){
			if (move.getRenderDirection().toLowerCase().equals("north")){ facing = Game.Facing.North;}
			else if (move.getRenderDirection().toLowerCase().equals("south")){ facing = Game.Facing.South;}
			else if (move.getRenderDirection().toLowerCase().equals("east")){ facing = Game.Facing.East;}
			else if (move.getRenderDirection().toLowerCase().equals("west")){ facing = Game.Facing.West;}
			return false;
		}

		Tile2D newPosition = findTile(move);
		if(!((newPosition instanceof Door)||(newPosition instanceof Floor))) return false;
		if(newPosition.getAvatar() != null) return false;

		if(newPosition instanceof Door){
			Door oldPosition = (Door) newPosition;

			boolean haveKey = false;


			// Find the Door color, and check to see if the avatar has the same color key to get through it !
			if(oldPosition instanceof GreenDoor){
				for(Item item : inventory){
					if(item instanceof GreenKey) haveKey = true;
				}
				if(!haveKey) return false;
			}

			else if(oldPosition instanceof RedDoor){
				for(Item item : inventory){
					if(item instanceof RedKey) haveKey = true;
				}
				if(!haveKey) return false;
			}

			else if(oldPosition instanceof YellowDoor){
				for(Item item : inventory){
					if(item instanceof YellowKey) haveKey = true;
				}
				if(!haveKey) return false;
			}

			else if(oldPosition instanceof PurpleDoor){
				for(Item item : inventory){
					if(item instanceof PurpleKey) haveKey = true;
				}
				if(!haveKey) return false;
			}

			if(room.getRoomPlace().equals("arena")){
				Room room  = oldPosition.getToRoom();
				Door door = room.getDoors().get(0);
				updateLocations(door, room);
			}
			else{
				Room arena = oldPosition.getToRoom();
				Door door = null;
				System.out.println("TO ROOM" + oldPosition.getToRoom());
//				if(oldPosition.getRoom().getRoomPlace().equals("north")) door = arena.getDoors().get(0);
//				else if(oldPosition.getRoom().getRoomPlace().equals("south")) door = arena.getDoors().get(3);
//				else if(oldPosition.getRoom().getRoomPlace().equals("east")) door = arena.getDoors().get(2);
//				else if(oldPosition.getRoom().getRoomPlace().equals("west")) door = arena.getDoors().get(1);
				if(oldPosition.getRoom().getRoomPlace().equals("north")) door = arena.getDoors().get(1);
				else if(oldPosition.getRoom().getRoomPlace().equals("south")) door = arena.getDoors().get(2);
				else if(oldPosition.getRoom().getRoomPlace().equals("east")) door = arena.getDoors().get(3);
				else if(oldPosition.getRoom().getRoomPlace().equals("west")) door = arena.getDoors().get(0);
				updateLocations(door, arena);
			}
		}
		else updateLocations(newPosition,room);

		if(tile.getItems().size() != 0) interact(tile.getItems().get(0));

		if(cell.isCharging()){
			int result = charge(newPosition);
			if(result == 0) cell.decExtraBattery();
			else if(result == 1) cell.decBattery();
		}
		else{
			cell.decBattery();
		}

		if(cell.getBatteryLife()<=0) die();

		animation();
		return true;
	}


	/**
	 * @author Ryan Griffin and Leon North
	 * @return
	 */
	private int charge(Tile2D target1){
		Tile2D target2 = findTile();
		//System.out.println(target);
		if(target1 instanceof Charger || target2 instanceof Charger) return useCharger();
		else return attack(target1, target2);

	}

	/**
	 * @author Ryan Griffin and Leon North
	 * @return
	 */
	private int useCharger(){
		centerAvatar();
		while(cell.getBatteryLife()<=maxCharge && cell.isCharging()){
			cell.incBattery();
		}
		return 2;
	}

	/**
	 * @author Ryan Griffin and Leon North
	 * @param target
	 * @return
	 */
	private int attack(Tile2D target1, Tile2D target2){
		if(target1.getAvatar() == null && target1.getAvatar() == null){
			return 0;
		}

		Avatar enemy2 = target2.getAvatar();
		if(enemy2 != null && !enemy2.equals(this)){// && enemy1 != null && enemy2.equals(enemy1)){
			System.out.println(enemy2);
			enemy2.takeDamage(damage);
			enemy2.setLastHit(this);
			return 1;
		}
		Avatar enemy1 = target1.getAvatar();
		if(enemy1 != null && !enemy1.equals(this)){
			System.out.println(enemy1);
			enemy1.takeDamage(damage);
			enemy1.setLastHit(this);
			return 1;
		}
		return 1;
	}

	/**
	 * @author Ryan Griffin and Leon North
	 * @param damage
	 */
	public void takeDamage(int damage){
		cell.takeHit(damage);
		//cell.decExtraBattery();
		if(cell.getBatteryLife()<=0){
			die();
		}
	}

	private void die(){
		score--;
		System.out.println(""+playerName+" died! Score is now: "+ score);
		if(lastHit != null){
			lastHit.addKill();
		}
		reset();
	}

	private void reset(){
		cell.setBatteryLife(maxCharge);
		updateLocations(startTile,startRoom);
		lastHit = null;
		emptyInventory();
	}

	public void addKill(){
		score++;
		System.out.println(""+playerName+" got a kill! Score is now: "+ score);
	}




	private Tile2D findTile(){
		int change = (Direction.get(facing.toString()) + Direction.get(renderDirection))%4;
		Tile2D target = null;
		if(change == 0) target = tile.getTileUp();
		else if(change == 1) target = tile.getTileRight();
		else if(change == 2) target = tile.getTileDown();
		else if(change == 3) target = tile.getTileLeft();
		return target;
	}


	private Tile2D findTile(Move move){
		int change = calcDirection(move);
		Tile2D newPosition = null;
		if(change == 0) newPosition = moveUp(tile.getTileUp());
		else if(change == 1) newPosition = moveRight(tile.getTileRight());
		else if(change == 2) newPosition = moveDown(tile.getTileDown());
		else if(change == 3) newPosition = moveLeft(tile.getTileLeft());
		return newPosition;
	}


	private int calcDirection(Move move){
		int dir = Direction.get(move.getRenderDirection());
		int key = Direction.getKeyDirection(move.getInteraction());
		int change = dir + key;
		change = change % 4;
		return change;
	}

	private void animation(){
		spriteIndex++;
		spriteIndex = spriteIndex % 4;
	}


	private Tile2D moveUp(Tile2D tileUp){
		tileYPos-=stepAmount;

		if(tileYPos<tileMinPos){
			if(!((tileUp instanceof Floor ) || (tileUp instanceof Door /*&& (!(checkHasKey(tileUp)))*/))){
				tileYPos+=stepAmount;
				return null;
			}
			tileYPos = tileMaxPos;
			return tileUp;
		}
		else{
			return tile;

		}
	}

	private Tile2D moveDown(Tile2D tileDown){
		tileYPos+=stepAmount;

		if(tileYPos>tileHeight){
			if(!((tileDown instanceof Floor ) || (tileDown instanceof Door ))){
				tileYPos-=stepAmount;
				return null;
			}
			tileYPos = tileMinPos;
			return tileDown;
		}
		else{
			return tile;
		}
	}

	private Tile2D moveLeft(Tile2D tileLeft){
		tileXPos-=stepAmount;
		if(tileXPos<tileMinPos){

			if(!(( tileLeft instanceof Floor ) || (tileLeft instanceof Door ))){
				tileXPos+=stepAmount;
				return null;
			}
			tileXPos = tileMaxPos;
			return tileLeft;
		}
		else{
			return tile;
		}
	}

	private Tile2D moveRight(Tile2D tileRight){
		tileXPos+=stepAmount;
		if(tileXPos>tileWidth){

			if(!(( tileRight instanceof Floor ) || (tileRight instanceof Door ))){
				tileXPos-=stepAmount;
				return null;
			}
			tileXPos = tileMinPos;
			return tileRight;
		}
		else{
			return tile;
		}
	}

	private boolean checkHasKey(Tile2D door){
		if(door instanceof GreenDoor){
			for(Item item : inventory){
				if(item instanceof GreenKey) return true;
			}
			return false;
		}
		if(door instanceof RedDoor){
			for(Item item : inventory){
				if(item instanceof RedKey) return true;
			}
			return false;
		}
		if(door instanceof YellowDoor){
			for(Item item : inventory){
				if(item instanceof YellowKey) return true;
			}
			return false;
		}
		if(door instanceof PurpleDoor){
			for(Item item : inventory){
				if(item instanceof PurpleKey) return true;
			}
			return false;
		}
		return false;
	}



	private void updateFacing(String dirKey){
		if(dirKey.toLowerCase().equals("w")) facing = Facing.North;
		else if(dirKey.toLowerCase().equals("d")) facing = Facing.East;
		else if(dirKey.toLowerCase().equals("s")) facing = Facing.South;
		else if(dirKey.toLowerCase().equals("a")) facing = Facing.West;
	}

	private void centerAvatar(){
		tileXPos = tileXCenter;
		tileYPos = tileYCenter;
	}


	public void setLastHit(Avatar lastHit){
		this.lastHit = lastHit;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public String getPlayerName(){
		return playerName;
	}

	public Tile2D getCurrentTile() {
		return tile;
	}

	public Room getCurrentRoom() {
		return room;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.room = currentRoom;
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
		this.tile = currentTile;
	}

	public double getTileXPos(){
		return tileXPos;
	}

	public double getTileYPos(){
		return tileYPos;
	}

	public int getSpriteIndex(){
		return spriteIndex;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean getAI() {
		return isAI;
	}

	public void setAI(boolean isAI) {
		this.isAI = isAI;
	}

	public String getRenderDirection(){
		return renderDirection;
	}




	public Tile2D getStartTile() {
		return startTile;
	}

	public void setStartTile(Tile2D startTile) {
		this.startTile = startTile;
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

	public void setStepAmount(int i) {
		this.stepAmount = i;

	}

}
