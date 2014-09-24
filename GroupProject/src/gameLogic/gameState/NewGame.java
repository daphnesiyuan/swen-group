package gameLogic.gameState;

import gameLogic.entity.GameCharacter;
import gameLogic.location.Door;
import gameLogic.location.Floor;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.location.Wall;
import gameLogic.physical.Container;
import gameLogic.physical.Furniture;
import gameLogic.physical.Item;
import gameLogic.physical.ItemObject;

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

	// all rooms in the house in this list
	private List<Room> roomsInGame;

	// a subset of characters, that are being played for this game instance
	private List<GameCharacter> activeCharacters;

	// The characters start / spawn in a room - list of all start locations in the game - universal spawn field (not local to room creation method) allows for spawn positions in multiple rooms
	// This should be used for accessing spawn points as opposed to the List<Spawn> in each Room object.
	private List<Floor> spawnTiles;


	// Toggle debugging / testing / development code on / off
	private boolean testing;



	/**
	 * The Constructor receives a varying length array of strings, each String is the path for a file that contains data for individual rooms
	 * The NewGame Object extracts that information, and constructs the neccesary game objects relative to the information.
	 * @param gameRooms
	 */
	public NewGame(String [] gameRooms){
		if(gameRooms.length == 0 ) throw new InvalidParameterException("No room file paths were given for game construction, "
				+ "Game start terminated");

		testing = true;


		roomsInGame = new ArrayList<Room>();

		spawnTiles = new ArrayList<Floor>();

		for(String path : gameRooms){
			loadRoom(path);
		}

		// The start room should always be the first argument provided, it is the room given to the character constructor as their initial 'currentRoom'.
		Room startRoom = roomsInGame.get(0);
		if(startRoom == null) System.out.println("No room provided for characters to spawn in");


		// Temporary list of String - mimics the function of a list containing clients usernames - for now has one name in it, so one character is created
		List<String> temp = new ArrayList<String>();
		temp.add("Ryan Griffin");


		// Attempt to populate the list of playing characters, with given parameter - client username
		activeCharacters = createCharacters(temp,startRoom);		// passed to create characters method
		if(activeCharacters==null) System.out.println("Could not create characters for game, no characters were created");


		if(testing) System.out.println("Number of spawn tiles: " + spawnTiles.size());
	}


	/**
	 * All clients playing the game must provide a user name as a string to have a GameCharacter constructed for them
	 * @param clientStrings
	 * @return
	 */
	private List<GameCharacter> createCharacters(List<String> clientStrings, Room startRoom) {

		List<GameCharacter> characters = new ArrayList<GameCharacter>();
		for(String s : clientStrings){
			Floor spawnXY = spawnXY();
			if(spawnXY==null) throw new UnsupportedOperationException("There is not enough spawn tiles in this room, for "
					+ " the number of provided users - game start terminated");

			GameCharacter player = new GameCharacter(s,spawnXY,startRoom,this);
			characters.add(player);

		}
		if(characters.size()>0)return characters;
		return null;
	}


	private Floor spawnXY(){
		if(spawnTiles.size()<=0)return null;

		Floor spawnTile =  spawnTiles.get(0);
		spawnTiles.remove(0);
		Collections.shuffle(spawnTiles);
		return spawnTile;

	}




	/**
	 * Given a path for a room file, construct a Room and its associated tiles and in room objects
	 * @param roomPath
	 */
	private void loadRoom(String roomPath){

		// Item collections are created locally - each room will have a different set of items
		List<Item> roomItems = new ArrayList<Item>();


		Room room = loadRoomData(roomPath,roomItems);


		if(room==null) System.out.println("Could not load room data from file for room: " + roomPath);
		else{
			if(testing){
				System.out.println("Room data loaded");
			}
		}




		// Rooms hold information for each of the tiles, and each piece of furniture they contain


		roomsInGame.add(room);

		if(testing){
			printRoomData(room);
		}





	}




	/**
	 * Given a String parameter - data from a file (file path is parameter) is loaded and constructs collections of room attributes
	 * Given an empty collection that allows Items, roomItems will be contain all items in the room, after they have been read from the room file data
	 * @param roomPath
	 * @param roomItems
	 * @return
	 */
	private Room loadRoomData(String roomPath, List<Item> roomItems) {
		// Tile count provided per room - debugging
		int tilesLoaded = 0;

		// roomNumber is an index to uniquely identify any room in the game.
		int roomNumber;

		// Tiles that the room contains
		List<Tile2D> roomTiles = new ArrayList<Tile2D>();


		// Rooms will contain information about all of the following they contain;
		List<Door> roomDoors = new ArrayList<Door>();
		List<Floor> roomFloors = new ArrayList<Floor>();
		List<Floor> roomSpawns = new ArrayList<Floor>();
		List<Wall> roomWalls = new ArrayList<Wall>();


		try {
			File roomFile = new File(roomPath);
			FileReader fileReader = new FileReader(roomFile);
			Scanner scan = new Scanner(fileReader);


			int row = 0;
			int tile = 0;
			String tileString;

			// first line of room data is an integer, it specifies the room number
			roomNumber = scan.nextInt();

			while(scan.hasNext()){
				tileString = scan.next();


				// Case : End of room data file, exit loading.
				// Case : Room data file empty, exit loading - display warning.
				if(tileString==null){
					if(tilesLoaded == 0) System.out.println("empty file - no tiles were data loaded for room :" + roomPath);
					break;
				}

				// Case : Scan information  for entire room finished, begin scanning room object data. Room Data terminated with 'ER'.
				else if(tileString.equals("ER")){
					scanRoomObjects(scan, roomItems,roomTiles);

				}

				// Case : Scan information  for Object data finished, begin scanning room Door data. object Data terminated with 'EI'.
				else if(tileString.equals("EI")){
					scanRoomDoors(scan,roomTiles,roomDoors);

				}

				// Case : Scan information  for the entire file has finished (three parts) - information loaded for room structure, items and doors. Close resources.
				else if(tileString.equals("EF")){
					scan.close();
					try {
						fileReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				// Case : Scan line for room data finished, begin scanning next line of room data. Lines of room data terminated with 'E' Character
				else if(tileString.equals("E")){
					row++;
					tile = 0;
					continue;
				}

				// Case : Valid Room data, extract and construct neccesary object
				else{


					// Case : tile is wall
					if(tileString.equals("W")){
						Tile2D wall = new Wall(tile,row,tileString);
						roomTiles.add(wall);
						roomWalls.add((Wall) wall);
					}

					// Case : tile is Door
					else if(tileString.equals("D")){
						Tile2D door = new Door(tile,row,tileString);
						roomTiles.add(door);
						roomDoors.add((Door) door);
					}

					// Case : tile is Floor
					else if(tileString.equals("F")){
						Tile2D floor = new Floor(tile,row,tileString,false);
						roomTiles.add(floor);
						roomFloors.add((Floor) floor);
					}

					// Case : tile is spawn point
					else if (tileString.equals("S")){
						Tile2D spawn = new Floor(tile,row,tileString,true);

						spawnTiles.add((Floor) spawn);

						roomTiles.add(spawn);
						roomSpawns.add((Floor) spawn);
						roomFloors.add((Floor) spawn);
					}
					tile++;
					tilesLoaded ++;

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally{
			if(testing){
				System.out.println("Tiles loaded from room file: " + tilesLoaded);
			}
		}

		// Finished room - contains info: roomNumber and the items and tiles it possess.
		Room room = new Room(roomNumber,roomTiles,roomItems);
		room.setDoors(roomDoors);
		room.setFloors(roomFloors);
		room.setSpawns(roomSpawns);
		room.setWalls(roomWalls);


		// Bidirectional accessibility - each of the tiles in a room will know which room they are a part of.
		for(Tile2D tile : roomTiles){
			tile.setRoom(room);
		}

		return room;
	}


	/**
	 * Control only passed to this method from loadRoomData().
	 * loadRoomData calls this method when loading has finished with the first part of the file - the second part of the file is loaded here.
	 * Section 1 and 2 of the File are differentiated with the string 'ER'.
	 * Information in the second part of the file represents objects in the room, they are encoded in the file as follows:
	 *
	 * ITEMTYPE		XPOS	YPOS	ITEMWEIGHT MOVABLE
	 *
	 * Item types can be as follows:
	 * S - Sofa
	 * K - Key
	 * C - Container
	 *
	 * XPOS and YPOS must be within the bounds of the rooms walls.
	 * ITEMWEIGHT can be:
	 * 0 - irrelevant
	 * 1+ - represents depth of object in stacking - objects with weight of 1 will be placed under objects with weight 2+. Likewise - objects with weight 4 will be placed on top of objects
	 * with weight 3 and less.
	 *
	 * Movable - either true or false.
	 *
	 * information is space separated.
	 *
	 * Data loaded from this method is typically added to the List<? extends Item> roomItems collection, which is given to the room constructor that the items belong to.
	 *
	 * NB: this method does not deal with doors in the room - they are constructed in  scanRoomDoors().
	 *
	 * @param s - scan data from room file
	 * @param roomItems - an empty collection to add the Room item information to.
	 * @param roomTiles	- allow for Bidirectional reachability between Items and their associated tiles.
	 */

	private void scanRoomObjects(Scanner s, List<Item> roomItems, List<Tile2D> roomTiles){
		String objectString;

		while(s.hasNext()){
			objectString = s.next();
			if(objectString == null) return;

			// Case : Sofa  - furniture Obj
			if(objectString.equals("S")){
				int xPos = s.nextInt();
				int yPos = s.nextInt();
				int weight = s.nextInt();
				String movable = s.next();


				// Get Objects respective tile on board - associate them.
				Tile2D itemLocation = null;
				for(Tile2D tile : roomTiles){
					if((tile.getXPos()==xPos)&&(tile.getYPos()==yPos)){
						itemLocation = tile;
					}
				}

				// Check - is Items given location within the bounds of the rooms walls, and also not located on a wall or a door
				if(!(itemLocation instanceof Floor)) System.out.println("Item provided in room data has invalid location coordinates");
				else{
					Furniture sofa = new Furniture("Sofa",itemLocation,weight,movable);
					roomItems.add(sofa);
					itemLocation.addItem(sofa);
				}
			}

			// Case : Key - itemObject Obj
			else if(objectString.equals("K")){
				int xPos = s.nextInt();
				int yPos = s.nextInt();
				int weight = s.nextInt();
				String movable = s.next();


				// Get Objects respective tile on board - associate them.
				Tile2D itemLocation = null;
				for(Tile2D tile : roomTiles){
					if((tile.getXPos()==xPos)&&(tile.getYPos()==yPos)){
						itemLocation = tile;
					}
				}

				// Check - is Items given location within the bounds of the rooms walls, and also not located on a wall.
				if(!(itemLocation instanceof Floor)) System.out.println("Item provided in room data has invalid location coordinates");
				else{
					ItemObject key = new ItemObject("Key",itemLocation,weight,movable);
					roomItems.add(key);
					itemLocation.addItem(key);
				}
			}

			// Case : Container	container Obj
			else if(objectString.equals("C")){
				int xPos = s.nextInt();
				int yPos = s.nextInt();
				int weight = s.nextInt();
				String movable = s.next();


				// Get Objects respective tile on board - associate them.
				Tile2D itemLocation = null;
				for(Tile2D tile : roomTiles){
					if((tile.getXPos()==xPos)&&(tile.getYPos()==yPos)){
						itemLocation = tile;
					}
				}

				// Check - is Items given location within the bounds of the rooms walls, and also not located on a wall.
				if(!(itemLocation instanceof Floor)) System.out.println("Item provided in room data has invalid location coordinates");
				else{
					Container container = new Container("container",itemLocation,weight,movable);
					roomItems.add(container);
					itemLocation.addItem(container);
				}
			}
		}
	}

	/**
	 * Control only passed to this method from loadRoomData().
	 * loadRoomData calls this method when loading has finished with the second part of the file - the third part of the file is loaded here.
	 * Section 2 and 3 of the File are differentiated with the string 'EI'.
	 * Information in the third part of the file represents doors in the room, they are encoded in the file as follows:
	 *
	 * XPOS		YPOS	TO_ROOM_INDEX	TO_ROOM_XPOS TO_ROOM_YPOS
	 *
	 * XPOS and YPOS must be within the bounds of the room.
	 * TO_ROOM_INDEX must be a valid room index.
	 * TO_ROOM_XPOS and TO_ROOM_YPOS must be within the bounds of the to room.

	 * information is space separated.
	 *
	 *
	 * @param scan - scan data from part three of the file, pertaining to doors associated with this room
	 * @param roomtiles	- A list of room tiles that the room contains
	 * @param roomDoors - to check that the door in part three of the file matches a room in part one.
	 */
	private void scanRoomDoors(Scanner scan, List<Tile2D> roomtiles, List<Door> roomDoors){
		int xPos = scan.nextInt();
		int yPos = scan.nextInt();
		int toRoomIndex = scan.nextInt();
		int toRoomXPos = scan.nextInt();
		int toRoomYPos = scan.nextInt();

		Door door = null;
		for(Door d : roomDoors){
			if((d.getXPos()==xPos)&&(d.getYPos()== yPos)){
				door = d;
			}
		}
		if(door == null) System.out.println("door provided is not contained within the structure of the given room");
		else{
			door.setToRoomIndex(toRoomIndex);
			door.setToRoomX(toRoomXPos);
			door.setToRoomY(toRoomYPos);
		}
	}


	/**
	 * Debugging method to ensure room data is loaded correctly
	 * The printed information should closely resemble the construct of the in game room.
	 * @param room
	 */
	private void printRoomData(Room room) {
		String print = "\n";


		int width = -1;
		for(Tile2D tile : room.getTiles()){
			if(tile.getXPos()> width){
				width = tile.getXPos();
			}
		}


		for(Tile2D tile : room.getTiles()){
			if(width == tile.getXPos()){
				print += tile.getType();
				print += "\n";

			}
			else{
				print += tile.getType();
			}
		}

		System.out.println(print);


	}

	public List<Room> getRoomsInGame() {
		return roomsInGame;
	}


	public void setRoomsInGame(List<Room> roomsInGame) {
		this.roomsInGame = roomsInGame;
	}


	public List<GameCharacter> getActiveCharacters() {
		return activeCharacters;
	}


	public void setActiveCharacters(List<GameCharacter> activeCharacters) {
		this.activeCharacters = activeCharacters;
	}


	public List<Floor> getSpawnTiles() {
		return spawnTiles;
	}


	public void setSpawnTiles(List<Floor> spawnTiles) {
		this.spawnTiles = spawnTiles;
	}

	public static void main(String[] args){
		new NewGame(args);
	}
}
