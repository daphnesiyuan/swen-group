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
	private List<Avatar> activeAI;


	public enum Facing { North, South, East, West; }

	private int roomNumber;

	private Score score;


	public Game(){
		roomNumber = 0; // RE: 0th room is arena


		roomsInGame = new ArrayList<Room>();
		activeAvatars = new ArrayList<Avatar>();
		activeAI = new ArrayList<Avatar>();
		createNewGame();
		score = new Score();
	}

	public Game(Boolean loaded){
		roomNumber = 0; // RE: 0th room is arena


		roomsInGame = new ArrayList<Room>();
		activeAvatars = new ArrayList<Avatar>();
		activeAI = new ArrayList<Avatar>();

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
			Avatar avatar = new Avatar(playerName,tile,room);
			activeAI.add(avatar);
			avatar.setAI(true);
			return room;
		}
		else{
			if(roomNumber>=4){
				System.out.println("Cannot add player - there are no rooms left !");
				return null;
			}
			Room room = roomsInGame.get(roomNumber++);
			Tile2D tile = room.getTiles()[3][1];
			Avatar avatar = new Avatar(playerName,tile,room);
			activeAvatars.add(avatar);
			avatar.setAI(false);
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


	public int tickAllAI(){
		int count = 0;
		for(Avatar ai : activeAI){
			if(ai instanceof Thinker){
				((Thinker) ai).think(this);
				count ++;
			}
		}
		return count;
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

	public List<Avatar> getActiveAI() {
		return activeAI;
	}

	public void setActiveAI(List<Avatar> activeAI) {
		this.activeAI = activeAI;
	}





}