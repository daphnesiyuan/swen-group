package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import networking.*;


public class Game{

	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;
	private List<AI> activeAI;


	public enum Facing { North, South, East, West; }

	private int roomNumber;

	private Score score;

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

	public Avatar getAvatar(String playerName){
		for(Avatar avatar: activeAvatars){
			if(playerName.equals(avatar.getPlayerName())){
				return avatar;
			}
		}
		return null;
	}


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
