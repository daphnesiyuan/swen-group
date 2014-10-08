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

public class NewGame {
	private Game game;

	private List<Room> roomsInGame;


	public NewGame(Game g){
		game = g;

		roomsInGame = createRooms();

		if(linkRooms()== false) System.out.println("Error in Game Creation : NewGame - linkRooms(), could not link home rooms and arena");

		game.setRoomsInGame(roomsInGame);

	}

	private List<Room> createRooms(){
		List<Room> rooms = new ArrayList<Room>();

		URL a = NewGame.class.getResource("/gameLogic/arena.txt");
		URL b = NewGame.class.getResource("/gameLogic/basic_room_north.txt");
		URL c = NewGame.class.getResource("/gameLogic/basic_room_south.txt");
		URL d = NewGame.class.getResource("/gameLogic/basic_room_east.txt");
		URL e = NewGame.class.getResource("/gameLogic/basic_room_west.txt");

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


	private boolean linkRooms(){
		return (fromArena() && fromRooms());
	}

	private boolean fromArena(){
		Room arena = roomsInGame.get(0);
		for(int i = 1; i< roomsInGame.size()-1; i++){
			arena.getDoors().get(i).setToRoom(roomsInGame.get(i));
		}
		return true;
	}

	private boolean fromRooms(){
		Room arena = roomsInGame.get(0);

		for(int i = 1; i< roomsInGame.size()-1; i++){
			roomsInGame.get(i).getDoors().get(0).setToRoom(arena);
		}
		return true;
	}


	/**
	 * Important to Note that if there is an IO exception thrown in this method, even if it is caught the method will return null.
	 * @param string
	 * @return
	 */
	private static Room makeRoom(URL v){
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
					else if(tile.toUpperCase().equals("C")){
						Tile2D column = new Column(x,y);
						tiles[y][x] = column;
					}
					x++;

				}


				Room room = new Room(tiles,null);
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						tiles[i][j].setRoom(room);
						if(tiles[i][j] instanceof Door) room.getDoors().add((Door) tiles[i][j]);
						if(tiles[i][j] instanceof Floor) room.getFloors().add((Floor) tiles[i][j]);
						if(tiles[i][j] instanceof Wall) room.getWalls().add((Wall) tiles[i][j]);
						if(tiles[i][j] instanceof Column) room.getColumns().add((Column) tiles[i][j]);

					}
				}

			return room;

			} catch (URISyntaxException e) { e.printStackTrace(); }
		} catch (FileNotFoundException e) { e.printStackTrace(); }

		return null;
	}
}
