package gameLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;



/**
 * NewGame class is used when starting a new game.
 * Room data arguments must be provided when constructing a new NewGame object, so that the class can create and populate game attributes, including rooms, tiles, characters and items.
 *
 * @author Ryan Griffin
 */

public class NewGame {
	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;

	private Game game;

	public NewGame(Game g){
		game = g;

		roomsInGame = createRooms();
		activeAvatars = createCharacters();

		game.setRoomsInGame(roomsInGame);
		game.setActiveAvatars(activeAvatars);

	}

	private List<Room> createRooms(){
		List<Room> rooms = new ArrayList<Room>();
		Room room = makeRoom();
		rooms.add(room);
		return rooms;
	}


	private List<Avatar> createCharacters() {
		List<Avatar> avatars = new ArrayList<Avatar>();


		Room room = roomsInGame.get(0);
		Tile2D tile = room.getTiles()[3][3];

		Avatar avatar = new Avatar("Ryan",tile,room);
		avatars.add(avatar);

		return avatars;

	}


	public static Room makeRoom() {

		int roomNumber = 1;

		Tile2D[][] tiles = new Tile2D[][]{
				{new Wall(0,0),  new Wall(1,0),         new Wall(2,0),         new Door(3,0),        new Wall(4,0),         new Wall(5,0),        new Wall(6,0)},
				{new Wall(0,1), new Floor(1,1, false), new Floor(2,1, false), new Floor(3,1, false),new Floor(4,1, false), new Floor(5,1, false), new Wall(6,1) },
				{new Wall(0,2), new Floor(1,2, false), new Floor(2,2, false), new Floor(3,2, false),new Floor(4,2, false), new Floor(5,2, false), new Wall(6,2) },
				{new Wall(0,3), new Floor(1,3, false), new Floor(2,3, false), new Floor(3,3, false),new Floor(4,3, false), new Floor(5,3, false), new Wall(6,3) },
				{new Wall(0,4), new Floor(1,4, false), new Floor(2,4, false), new Floor(3,4, false),new Floor(4,4, false), new Floor(5,4, false), new Wall(6,4) },
				{new Wall(0,5), new Floor(1,5, false), new Floor(2,5, false), new Floor(3,5, false),new Floor(4,5, false), new Floor(5,5, false), new Wall(6,5) },
				{new Wall(0,6),  new Wall(1,6),         new Wall(2,6),         new Wall(3,6),        new Wall(4,6),         new Wall(5,6),        new Wall(6,6), }
		};

		Room room = new Room(roomNumber,tiles,null);
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j].setRoom(room);
			}
		}
		return room;
	}




}
