package gameLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.plaf.FileChooserUI;



/**
 * NewGame class is used when starting a new game.
 * Room data arguments must be provided when constructing a new NewGame object,
 * so that the class can create and populate game attributes, including rooms, tiles, characters and items.
 *
 * @author Ryan Griffin
 */

public class NewGame {
	private List<Room> roomsInGame;
	private List<Avatar> activeAvatars;

	private Game game;

	private static int roomNumber;

	public NewGame(Game g){
		roomNumber = 0;
		game = g;

		roomsInGame = createRooms();
		activeAvatars = createCharacters();

		//new GameLayout(activeAvatars,roomsInGame);

		game.setRoomsInGame(roomsInGame);
		game.setActiveAvatars(activeAvatars);


	}

	private List<Room> createRooms(){
		List<Room> rooms = new ArrayList<Room>();

		URL a = NewGame.class.getResource("/gameLogic/arena.txt");
		URL b = NewGame.class.getResource("/gameLogic/basic_room.txt");
		URL c = NewGame.class.getResource("/gameLogic/basic_room.txt");
		URL d = NewGame.class.getResource("/gameLogic/basic_room.txt");
		URL e = NewGame.class.getResource("/gameLogic/basic_room.txt");

		Room arena = makeRoom(a);
		Room start1 = makeRoom(b);
		Room start2 = makeRoom(c);
		Room start3 = makeRoom(d);
		Room start4 = makeRoom(e);

		rooms.add(arena);
		rooms.add(start1);
		rooms.add(start2);
		rooms.add(start3);
		rooms.add(start4);

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

	/**
	 * Important to Note that if there is an IO exception thrown in this method, even if it is caught the method will return null.
	 * @param string
	 * @return
	 */
	public static Room makeRoom(URL v){
		//TODO inverse x and y for array ?????


		try {
			try {
				File file = new File(v.toURI());
				Scanner scan = new Scanner(file);

				String tile = null;
				int tileRows = 0;
				int tileCols = 0;
				int tileRowsFinal = 0;

				while(scan.hasNext()){	// Initial loop to count tiles for 2d array construction
					tile = scan.next();
					if(tile == null) break;
					else if(tile.toUpperCase().equals("E")){
						tileCols++;
						tileRowsFinal = tileRows;
						tileRows = 0;
					}
					else{
						tileRows ++;
					}
				}

				scan = new Scanner(file);		// reset the scanner for a second file reading iteration, this time the tiles will actually be created.
				tile = null;				// precautionary read reset
				int x = 0;
				int y = 0;

				Tile2D[][] tiles = new Tile2D[tileRowsFinal][tileCols];

				while(scan.hasNext()){
					tile = scan.next();
					if(tile == null) break;
					else if(tile.toUpperCase().equals("E")){
						x = 0;
						y ++;
						continue;

					}
					else if(tile.toUpperCase().equals("W")){
						System.out.println(x+" , "+y);
						Tile2D wall = new Wall(x,y);
						tiles[y][x] = wall;

					}
					else if(tile.toUpperCase().equals("F")){
						Tile2D floor = new Floor(x,y);
						tiles[y][x] = floor;
					}
					else if(tile.toUpperCase().equals("D")){
						Tile2D door = new Door(x,y);
						tiles[y][x] = door;
					}
					x++;

				}


				Room room = new Room(roomNumber++,tiles,null);
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						tiles[i][j].setRoom(room);
					}
				}

			return room;

			} catch (URISyntaxException e) { e.printStackTrace(); }
		} catch (FileNotFoundException e) { e.printStackTrace(); }

		return null;
	}
}
