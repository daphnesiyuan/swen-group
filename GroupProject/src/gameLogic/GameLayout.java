package gameLogic;

import java.util.List;

public class GameLayout {


	private List<Avatar> avatars;
	private List<Room> rooms;

	public GameLayout(List<Avatar> avatars, List<Room> rooms){

		this.avatars = avatars;
		this.rooms = rooms;
		setHomeRooms();

	}

	private void setHomeRooms(){
		if(avatars.size()!=rooms.size()){
			System.out.println("GameLayout : setHomeRomos() - Problem!  number of rooms ! = number of avatars");
			return;
		}

		for(int i = 1; i < rooms.size()-1; i++){		// This starts at 1, as the 0th indexed room is the arena


		}

	}


}
