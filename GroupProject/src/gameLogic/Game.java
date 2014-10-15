package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import networking.*;

/**
 *
 * @author griffiryan
 * The Game class is used to initialize and store game state information. It utilizes an auxillary class - NewGame to setup game variables, if game loading/saving is not
 * present. The Game class is responsible for maintaining information about players currently playing, and being added to the game. It Stores all game state information -
 * all of the current players and their associated Avatar objects, Rooms in the game, and current active AI units. Typically, a usable game class will have commands sent
 * to it from a server, which will instruct it to move an avatar(moveAvatar(Move move) is called), add a new player to the game (addPlayer() is called), instruct an Avatar
 * to interact with an Item (avatarInteractWithItem()). Any game actions that effect the state of the game need to be communicated to the Game class.
 */
public class Game{

	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;
	private List<AI> activeAI;

	public enum Facing { North, South, East, West; }

	// Avatars are created in unique rooms based on the number of roosm already created.
	private int roomNumber;


	private Score score;

	// Enviroment thread - generate game items
	private Thread environment;

	public Game(){
		this.roomNumber = 1; // RE: 0th room is arena

		this.roomsInGame = new ArrayList<Room>();
		this.activeAvatars = new ArrayList<Avatar>();
		this.activeAI = new ArrayList<AI>();
		createNewGame();
		this.score = new Score();

		this.environment = new Environment(this);
		environment.start();
	}

	public Game(Boolean loaded){
		roomNumber = 1; // RE: 0th room is arena
		roomsInGame = new ArrayList<Room>();
		activeAvatars = new ArrayList<Avatar>();
		activeAI = new ArrayList<AI>();
	}		//ANTONIA: To be used when loading a file


	public void addRoom(Room room){
		roomsInGame.add(room);
	}

	private void createNewGame(){
		new NewGame(this);
	}

	/**
	 * Searches all ActiveAvatars in the game and returns that avatar if their playername matches the given playername string
	 * @param playerName
	 * @return Avatar that equals the given string
	 */
	public Avatar getAvatar(String playerName){
		for(Avatar avatar: activeAvatars){
			if(playerName.equals(avatar.getPlayerName())){
				return avatar;
			}
		}
		return null;
	}

	/**
	 * Server calls addPlayer(playerName) with a name for the new Avatar, this method sets up the avatar and adds it to the game.
	 * @param playerName
	 * @return The room the created avatar starts in (Their home / spawn room)
	 */
	public Room addPlayer(String playerName){
		if(playerName.startsWith("ai")){
			Room room = roomsInGame.get(0);
			Tile2D tile = room.getTiles()[2][2];
			while (tile.getAvatar() != null && tile instanceof Floor){
				int length = room.getTiles().length;
				tile = room.getTiles()[length - tile.getxPos() - 1][tile.getyPos()];
			}
			new Avatar(playerName,tile,room);
			return room;
		}
		else{
			if(roomNumber>=5){
				System.out.println("Cannot add player - there are no rooms left !");
				return null;
			}
			Room room = roomsInGame.get(roomNumber++);
			Tile2D tile = null;
			for(Room r: roomsInGame){
			}
			// The Avatars start position will be infront of the charger, facing toward the arena.
			if(room.getRoomPlace().equals("north")){
				tile = room.getTiles()[4][3];
				System.out.println("Tile is "+room.getTiles()[4][3]);
			}
			else if(room.getRoomPlace().equals("south")){
				tile = room.getTiles()[2][3];
			}
			else if(room.getRoomPlace().equals("east")){
				tile = room.getTiles()[3][2];
			}
			else if(room.getRoomPlace().equals("west")){
				tile = room.getTiles()[3][4];
			}
			Avatar avatar = new Avatar(playerName,tile,room);
			activeAvatars.add(avatar);
			return room;
		}
	}


	/**
	 * Finds the Avatar associated with the given move object, and commands them to perform the move associated with the given move object.
	 * @param move
	 * @return true iff the move is succesful.
	 */
	public boolean moveAvatar(Move move){
		Avatar mover = null;

		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(move.getPlayer().getName())){
				mover = avatar;
			}

		}
		if(mover==null) return false;
		return mover.moveTo(move);
	}


	public boolean avatarInteractWithItem(String playerName, Item item){
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(playerName)){
				return avatar.interact(item);
			}
		}
		return false;
	}

	/**
	 * Return the room in the game that a player with the given playername string has.
	 * @param playerName - player identified with their name string
	 * @return the Room the given player is in
	 */

	public Room getRoom(String playerName){
		for(Room room : roomsInGame){
			for(Avatar player : room.getAvatars()){
				if(player.getPlayerName().equals(playerName)){
					return room;
				}
			}
		}
		return null;
	}

	public boolean setPlayerName(String fromName, String toName){
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(fromName)){
				avatar.setPlayerName(toName);
				return true;
			}
		}
		return false;
	}

	/**
	 * Servercalls removePlayerFromGame(playerName) if they disconnect. This method finds the avatar in the game with a matching playername, returns all of the items
	 * in their inventory to their starting positions and removes all references to the avatar.
	 * @param playerName
	 * @return true iff the Avatar is successfully removed from the game, and all referenced information is reset.
	 */
	public boolean removePlayerFromGame(String playerName){
		Avatar leaving = null;
		for(Avatar avatar : activeAvatars){
			if(avatar.getPlayerName().equals(playerName)){
				leaving = avatar;
			}
		}
		if(leaving == null){
			System.out.println("No player in game with name: "+playerName);
			return false;
		}
		for(Item item : leaving.getInventory()){
			item.returnToStartPos();
		}

		leaving.getCurrentRoom().removeAvatar(leaving);
		leaving.getCurrentTile().removeAvatar(leaving);
		score.getScore().remove(leaving.getPlayerName());
		return activeAvatars.remove(leaving);
	}

	public boolean addAI(AI ai){
		return activeAI.add(ai);
	}
	public boolean removeAI(AI ai){
		return activeAI.remove(ai);
	}

	/**
	 * Server thread calls this method to systematically instruct AI to think / generate a movement.
	 * @return an integer - the number of AI in game that think() was called on.
	 */
	public int tickAllAI(){
		int count = 0;
		for(AI ai : activeAI){
			if(ai instanceof Thinker){
				ai.think(this);
				count ++;
			}
		}
		return count;
	}

	public Room getRoomByName(String roomName){
		System.out.println("HERE FOR FUCKS SAKE");
		System.out.println("Rooms in game size " + roomsInGame.size());
		for(Room r: roomsInGame){
			System.out.println("GET ROOM PLACE " + r.getRoomPlace());
			System.out.println("GET ROOMNAME "+ roomName);

			if(r.getRoomPlace().equals(roomName)){
				return r;
			}
		}
		return null;
	}

	public void setActiveAvatars(List<Avatar> activeAvatars) {
		this.activeAvatars = activeAvatars;
	}

	public List<Room> getRoomsInGame() {
		return roomsInGame;
	}


	public void setRoomsInGame(List<Room> roomsInGame) {
		this.roomsInGame = roomsInGame;
	}


	public List<Avatar> getActiveAvatars() {
		return activeAvatars;
	}

	public Score getScore(){
		score.updateMap(activeAvatars);
		return score;
	}

	public List<AI> getActiveAI() {
		return activeAI;
	}

	public void setActiveAI(List<AI> activeAI) {
		this.activeAI = activeAI;
	}

	public void setScore(Score score) {
		this.score = score;
	}





}
