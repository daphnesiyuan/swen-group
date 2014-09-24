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

import rendering.Drawing;



/**
 * NewGame class is used when starting a new game.
 * Room data arguments must be provided when constructing a new NewGame object, so that the class can create and populate game attributes, including rooms, tiles, characters and items.
 *
 * @author Ryan Griffin
 */
public class NewGame {
	private List<Room> roomsInGame;
	private List<GameCharacter> activeCharacters;
	private List<Floor> spawnTiles;

	private Room startRoom;

	private boolean testing;

	private Game game;

	public NewGame(Game g){
		game = g;


		roomsInGame = new ArrayList<Room>();
		spawnTiles = new ArrayList<Floor>();


		String roomPath = "/GroupProject/src/gameLogic/gameState/basic_room";

		// for each different room path
		roomsInGame.add(loadRoomData(roomPath));

		activeCharacters = createCharacters();

		game.setRoomsInGame(roomsInGame);
		game.setActiveCharacters(activeCharacters);


	}


	private List<GameCharacter> createCharacters() {

		List<String> clientStrings = new ArrayList<String>();
		clientStrings.add("Ryan Griffin");

		List<GameCharacter> characters = new ArrayList<GameCharacter>();

		for(String s : clientStrings){
			Floor spawnXY = spawnXY();
			GameCharacter player = new GameCharacter(s,spawnXY,game);
			characters.add(player);
		}

		return characters;

	}


	private Floor spawnXY(){
		if(spawnTiles.size()<=0)return null;

		Floor spawnTile =  spawnTiles.get(0);
		spawnTiles.remove(0);
		Collections.shuffle(spawnTiles);
		return spawnTile;

	}


	private Room loadRoomData(String roomPath) {
		List<Item> roomItems = new ArrayList<Item>();

		// roomNumber is an index to uniquely identify any room in the game.
		int roomNumber;


		// variables to use when loading data from files and arrays are being popualted, will determine height and width of room, as well as size of tile array
		int rowInc = 0;
		int rowLength = 0;
		int colLength = 0;



		// Initial Buffer arrays to hold data loaded from file, the data will be swapped to more appropriately sized array
		Tile2D[][] tileBuffer = new Tile2D[1024][1024];


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
			int col = 0;
			String tileString;

			// first line of room data is an integer, it specifies the room number
			roomNumber = scan.nextInt();

			while(scan.hasNext()){
				tileString = scan.next();


				// Case : End of room data file, exit loading.
				// Case : Room data file empty, exit loading - display warning.
				if(tileString==null){
					break;
				}

				// Case : Scan information  for entire room finished, begin scanning room object data. Room Data terminated with 'ER'.
				else if(tileString.equals("ER")){
					scanRoomObjects(scan, tileBuffer, roomItems);
				}

				// Case : Scan information  for Object data finished, begin scanning room Door data. object Data terminated with 'EI'.
				else if(tileString.equals("EI")){
					scanRoomDoors(scan,tileBuffer,roomDoors);
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
					col = 0;
					colLength++;
					rowLength = rowInc;
					continue;
				}

				// Case : Valid Room data, extract and construct neccesary object
				else{


					// Case : tile is wall
					if(tileString.equals("W")){
						Tile2D wall = new Wall(col,row,tileString);
						tileBuffer[row][col] = wall;
						roomWalls.add((Wall) wall);
						rowInc++;
					}

					// Case : tile is Door
					else if(tileString.equals("D")){
						Tile2D door = new Door(col,row,tileString);
						tileBuffer[row][col] = door;
						roomDoors.add((Door) door);
						rowInc++;
					}

					// Case : tile is Floor
					else if(tileString.equals("F")){
						Tile2D floor = new Floor(col,row,tileString,false);
						tileBuffer[row][col] = floor;
						roomFloors.add((Floor) floor);
						rowInc++;
					}

					// Case : tile is spawn point
					else if (tileString.equals("S")){
						Tile2D spawn = new Floor(col,row,tileString,true);

						spawnTiles.add((Floor) spawn);

						tileBuffer[row][col] = spawn;
						roomSpawns.add((Floor) spawn);
						roomFloors.add((Floor) spawn);
						rowInc++;
					}

					col++;

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}


		// create an appropriately sized array, populate from buffer
		Tile2D[][] roomTiles = new Tile2D[rowLength][colLength];
		for(int i = 0; i<rowLength; i++){
			for(int j = 0; j<colLength; j++){
				if(tileBuffer[i][j]!=null){
					roomTiles[i][i] = tileBuffer[i][j];
				}
			}
		}

		// Finished room - contains info: roomNumber and the items and tiles it possess.
		Room room = new Room(roomNumber,roomTiles,roomItems);

		room.setDoors(roomDoors);
		room.setFloors(roomFloors);
		room.setSpawns(roomSpawns);
		room.setWalls(roomWalls);


		// Bidirectional accessibility - each of the tiles in a room will know which room they are a part of.
		for(int i = 0; i<rowLength; i++){
			for(int j = 0; j<colLength; j++){
				if(roomTiles[i][j]!=null){
					roomTiles[i][i].setRoom(room);
				}
			}
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
	 * @param scan - scan data from room file
	 * @param roomItems - an empty collection to add the Room item information to.
	 * @param roomTiles	- allow for Bidirectional reachability between Items and their associated tiles.
	 */

	private void scanRoomObjects(Scanner scan, Tile2D[][] roomTiles, List<Item> roomItems){
		String objectString;

		while(scan.hasNext()){
			objectString = scan.next();
			if(objectString == null) return;

			// Case : Sofa  - furniture Obj
			if(objectString.equals("S")){
				int xPos = scan.nextInt();
				int yPos = scan.nextInt();
				int weight = scan.nextInt();
				String movable = scan.next();


				// Get Objects respective tile on board - associate them.
				Tile2D itemLocation = roomTiles[xPos][yPos];


				// Check - is Items given location within the bounds of the rooms walls, and also not located on a wall or a door
				if((!(itemLocation instanceof Floor))|| (itemLocation == null) ){
					System.out.println("Item provided in room data has invalid location coordinates");
					scan.nextLine();
					continue;
				}
				else{
					Furniture sofa = new Furniture("Sofa",itemLocation,weight,movable);
					roomItems.add(sofa);
					itemLocation.addItem(sofa);
				}
			}

			// Case : Key - itemObject Obj
			else if(objectString.equals("K")){
				int xPos = scan.nextInt();
				int yPos = scan.nextInt();
				int weight = scan.nextInt();
				String movable = scan.next();


				// Get Objects respective tile on board - associate them.
				Tile2D itemLocation = roomTiles[xPos][yPos];

				// Check - is Items given location within the bounds of the rooms walls, and also not located on a wall.
				if((!(itemLocation instanceof Floor))|| (itemLocation == null)){
					System.out.println("Item provided in room data has invalid location coordinates");
					scan.nextLine();
					continue;
				}
				else{
					ItemObject key = new ItemObject("Key",itemLocation,weight,movable);
					roomItems.add(key);
					itemLocation.addItem(key);
				}
			}

			// Case : Container	container Obj
			else if(objectString.equals("C")){
				int xPos = scan.nextInt();
				int yPos = scan.nextInt();
				int weight = scan.nextInt();
				String movable = scan.next();


				// Get Objects respective tile on board - associate them.
				Tile2D itemLocation = roomTiles[xPos][yPos];

				// Check - is Items given location within the bounds of the rooms walls, and also not located on a wall.
				if((!(itemLocation instanceof Floor))||(itemLocation == null)){
					System.out.println("Item provided in room data has invalid location coordinates");
					scan.nextLine();
					continue;
				}
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
	private void scanRoomDoors(Scanner scan, Tile2D[][] roomtiles, List<Door> roomDoors){
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


}
