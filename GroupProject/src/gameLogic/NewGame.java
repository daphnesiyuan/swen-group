package gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




public class NewGame {
	private Game game;

	private List<Room> roomsInGame;

	private String roomPlace;


	private int roomsMade;
	private int doorCount;


	public NewGame(Game g){
		roomsMade = 0;
		doorCount = 0;

		game = g;
		roomsInGame = createRooms();
		linkRooms();
		game.setRoomsInGame(roomsInGame);
		System.out.println("NewGame - setRoomsinGame done");
		roomPlace = "NULL";


	}

	private List<Room> createRooms(){
		List<Room> rooms = new ArrayList<Room>();

		Room arena = makeRoom("/gameLogic/arena.txt");
		roomsMade++;
		Room start1 = makeRoom("/gameLogic/basic_room_north.txt");
		roomsMade++;
		Room start2 = makeRoom("/gameLogic/basic_room_south.txt");
		roomsMade++;
		Room start3 = makeRoom("/gameLogic/basic_room_east.txt");
		roomsMade++;
		Room start4 = makeRoom("/gameLogic/basic_room_west.txt");
		roomsMade++;

		rooms.add(arena);

		rooms.add(start1);
		rooms.add(start2);
		rooms.add(start3);
		rooms.add(start4);

		System.out.println("NewGame.createRooms - added rooms");
		return rooms;
	}

	private void linkRooms(){
		Room arena = roomsInGame.get(0);
		Room n = null, s = null , e = null , w = null;
		for(Room room : roomsInGame){
			if(room.getRoomPlace().equals("north")) n = room;
			else if(room.getRoomPlace().equals("south")) s = room;
			else if(room.getRoomPlace().equals("east")) e = room;
			else if(room.getRoomPlace().equals("west")) w  = room;
		}
		n.getDoors().get(0).setToRoom(arena);
		s.getDoors().get(0).setToRoom(arena);
		e.getDoors().get(0).setToRoom(arena);
		w.getDoors().get(0).setToRoom(arena);

		arena.getDoors().get(0).setToRoom(n);
		arena.getDoors().get(3).setToRoom(s);
		arena.getDoors().get(2).setToRoom(e);
		arena.getDoors().get(1).setToRoom(w);
	}

	/**
	 * Important to Note that if there is an IO exception thrown in this method, even if it is caught the method will return null.
	 * @param string
	 * @return
	 * @author Leon North and Ryan Griffin
	 */
	private Room makeRoom(String v){

		Scanner scan = new Scanner(NewGame.class.getResourceAsStream(v));

		String tile = null;
		int tileRows = 0;
		int tileCols = 0;
		int tileRowsFinal = 0;

		roomPlace = scan.next();
		while (scan.hasNext()) { // Initial loop to count tiles for 2d array
									// construction
			tile = scan.next();
			if (tile == null)
				break;
			else if (tile.toUpperCase().equals("E")) {
				tileCols++;
				tileRowsFinal = tileRows;
				tileRows = 0;
			} else {
				tileRows++;
			}
		}

		scan = new Scanner(NewGame.class.getResourceAsStream(v)); // reset the
																	// scanner
																	// for a
																	// second
																	// file
																	// reading
																	// iteration,
																	// this time
																	// the tiles
																	// will
																	// actually
																	// be
																	// created.
		tile = null; // precautionary read reset
		int x = 0;
		int y = 0;

		Tile2D[][] tiles = new Tile2D[tileRowsFinal][tileCols];
		roomPlace = scan.next();
		while (scan.hasNext()) {
			tile = scan.next();
			if (tile == null)
				break;
			else if (tile.toUpperCase().equals("E")) {
				x = 0;
				y++;
				continue;

			} else if (tile.toUpperCase().equals("W")) {
				Tile2D wall = new Wall(x, y);
				tiles[y][x] = wall;

			} else if (tile.toUpperCase().equals("F")) {
				Tile2D floor = new Floor(x, y);
				tiles[y][x] = floor;
			} else if (tile.toUpperCase().equals("D")) {
				if (roomsMade != 0) {
					if (roomPlace.equals("north")) {
						RedDoor door = new RedDoor(x, y);
						tiles[y][x] = door;
					} else if (roomPlace.equals("south")) {
						GreenDoor door = new GreenDoor(x, y);
						tiles[y][x] = door;
					} else if (roomPlace.equals("east")) {
						YellowDoor door = new YellowDoor(x, y);
						tiles[y][x] = door;
					} else if (roomPlace.equals("west")) {
						PurpleDoor door = new PurpleDoor(x, y);
						tiles[y][x] = door;
					}
				} else {
					if (doorCount == 0) {
						RedDoor door = new RedDoor(x, y);
						tiles[y][x] = door;
					} else if (doorCount == 1) {
						PurpleDoor door = new PurpleDoor(x, y);
						tiles[y][x] = door;
					} else if (doorCount == 2) {
						YellowDoor door = new YellowDoor(x, y);
						tiles[y][x] = door;
					} else if (doorCount == 3) {
						GreenDoor door = new GreenDoor(x, y);
						tiles[y][x] = door;
					}
				}
				doorCount++;
			} else if (tile.toUpperCase().equals("C")) {
				Tile2D column = new Column(x, y);
				tiles[y][x] = column;
			} else if (tile.toUpperCase().equals("T")) {
				Tile2D tree = new Tree(x, y);
				tiles[y][x] = tree;
			} else if (tile.toUpperCase().equals("Z")) {
				Tile2D charger = new Charger(x, y);
				tiles[y][x] = charger;
			}
			x++;

		}
		Room room = new Room(tiles, null);
		room.setRoomPlace(roomPlace);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j].setRoom(room);
				if (tiles[i][j] instanceof Door)
					room.getDoors().add((Door) tiles[i][j]);
				if (tiles[i][j] instanceof Floor)
					room.getFloors().add((Floor) tiles[i][j]);
				if (tiles[i][j] instanceof Wall)
					room.getWalls().add((Wall) tiles[i][j]);
				if (tiles[i][j] instanceof Column)
					room.getColumns().add((Column) tiles[i][j]);
				if (tiles[i][j] instanceof Tree)
					room.getTrees().add((Tree) tiles[i][j]);
				if (tiles[i][j] instanceof Charger)
					room.getChargers().add((Charger) tiles[i][j]);
			}
		}

		return room;
	}
}
