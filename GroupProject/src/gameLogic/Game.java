package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import networking.*;


public class Game{


// TODO ryan: tidy room and door and movement between code,
	// correct implementation.
	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;
	private List<AI> activeAI;


	public enum Facing { North, South, East, West; }

	private int roomNumber;

	private Score score;

	//tiles[1][1].addItem(new Light(tiles[1][1]));
	
	public Game(){
		this.roomNumber = 0; // RE: 0th room is arena


		this.roomsInGame = new ArrayList<Room>();
		this.activeAvatars = new ArrayList<Avatar>();
		this.activeAI = new ArrayList<AI>();
		createNewGame();
		this.score = new Score();
		
		Thread environment = new Environment(this);
		environment.start();
	}

	public Game(Boolean loaded){
		roomNumber = 0; // RE: 0th room is arena


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
			Tile2D tile = room.getTiles()[3][3];
			new Avatar(playerName,tile,room);
			return room;
		}
		else{
			if(roomNumber>=4){
				System.out.println("Cannot add player - there are no rooms left !");
				return null;
			}
			Room room = roomsInGame.get(roomNumber++);
			Tile2D tile = room.getTiles()[3][3];
			Avatar avatar = new Avatar(playerName,tile,room);
			activeAvatars.add(avatar);
			if(avatar.getPlayerName().startsWith("ai")){
				return roomsInGame.get(0);
			}
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
	 *
	 * @param playerName - player identidied with their name string
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

		//TODO update method for when player leaves - null case in updateLocations.
		//leaving.updateLocations(null, null);

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

	private class Environment extends Thread{
		
		Game game;
		
		public Environment(Game game){
			this.game = game;
		}
		
		@Override
		public void run(){
			while(true){
				
			}
		}
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





}