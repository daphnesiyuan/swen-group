package dataStorage;

import java.util.List;

import org.jdom2.Element;

import gameLogic.Avatar;
import gameLogic.Cell;
import gameLogic.Charger;
import gameLogic.Column;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Game;
import gameLogic.Item;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Tree;
import gameLogic.Wall;

/**
 * A class that acts as a parser for the XML saving process.
 * The main purpose of this class is to parse the seperate parts
 * of the current game state and return an element whether the
 * parser was successful or not.
 *
 * @author caskeyanto
 *
 */

public class XMLSaveParser {

	XMLSaver saver;

	public XMLSaveParser(XMLSaver x){
		saver = x;
	}

	/**
	 * Parses a room and writes all the concerned details to an XML file
	 *
	private Tile2D[][] tiles;
	private List<Door> doors;
	private List<Floor> floors;
	private List<Wall> walls;
	private List<Column> columns;


	private List <Item> items;
	private List<Avatar> avatars;

	private String roomPlace;
	 *
	 * @param room
	 * @return Element
	 */

	public Element parseRoom(Room room){
		Element e = new Element("Room");		//room


		Element doors = new Element("doors");		//doors			//
		Element floors = new Element("floors");		//floors		//TILES
		Element walls = new Element("walls");		//walls			//

		Element avatars = new Element("avatars");		//characters
		Element items = new Element("items");			//items
		Element others = new Element("other_tiles");	//Other tiles (chargers, trees, etc.)

		e.addContent(new Element("roomPlace").setText(room.getRoomPlace()));		//room place

		//CHARACTERS
		if(! room.getAvatars().isEmpty()){
			for(Avatar a: room.getAvatars()){
				avatars.addContent(parseAvatar(a));
			}
			e.addContent(avatars);
		}

		//TILES
		if(room.getTiles()!= null &&!(room.getTiles().length ==0)){
				Tile2D[][] t = room.getTiles();

				for(int i = 0; i<t.length;i++){
					for(int j = 0;j<t[i].length;j++){
						Tile2D thisTile = t[i][j];
						if(thisTile instanceof Floor){
							floors.addContent(parseFloor((Floor)thisTile));
						}
						else if(thisTile instanceof Wall){
							walls.addContent(parseWall((Wall)thisTile));
						}
						else if(thisTile instanceof Door){
							doors.addContent(parseDoor((Door)thisTile));
						}
						else{
							others.addContent(parseOtherTile2D(thisTile));
						}
					}

				}
				e.addContent(floors);
				e.addContent(walls);
				e.addContent(doors);
				e.addContent(others);
		}
		else{return null;}//IMPOSSIBLE TO HAVE ROOM W.O. floor
		return e;
	}

	/**
	 * Parses an avatar in this order and returns an element
	 *
	 *
	private Game.Facing facing;
	private List <Item> Inventory;


	private Tile2D currentTile;
	private Room currentRoom;

	private String playerName;

	private Cell battery;

	// Avatars coordinates relative to the room - global.
	private double globalXPos, globalYPos;

	// Avatars coordinates relative to the tile - local.
	private double tileXPos, tileYPos;
	 * This is called in parseTile(Floor floor)
	 *
	 * @param avatar
	 * @return Element
	 */

	public Element parseAvatar(Avatar avatar){
		Element e = new Element("Avatar");
		e.addContent(new Element("facing").setText(avatar.getFacing().name()));			//Facing
		Element inventory = new Element("inventory");		//Inventory
		if(!avatar.getInventory().isEmpty()){
			for(Item i: avatar.getInventory()){		//iterate through list
				inventory.addContent(new Element("item").setText(i.getDescription()));//add item to inventory element
			}
		}
		e.addContent(inventory);
		e.addContent(new Element("xPos").setText(Integer.toString(avatar.getCurrentTile().getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(avatar.getCurrentTile().getyPos())));
		e.addContent(new Element("playerName").setText(avatar.getPlayerName()));		//player name
		Element cell = new Element("cell");
		cell = parseCell(avatar.getCell());
		e.addContent(cell);

		e.addContent(new Element("globalXPos").setText(Double.toString(avatar.getGlobalXPos())));
		e.addContent(new Element("globalYPos").setText(Double.toString(avatar.getGlobalYPos())));
		e.addContent(new Element("tileXPos").setText(Double.toString(avatar.getTileXPos())));
		e.addContent(new Element("tileYPos").setText(Double.toString(avatar.getTileYPos())));

		return e;
	}

/**
 * Parses a tile in this order and returns an element
 *
 * 	int xPos;
	int yPos;
	String type;
	Room room;
	boolean isSpawn;
	List <Item> itemsOnTile;
	List <Avatar> charactersOnTile;

 *
 */

	public Element parseFloor(Floor floor){
		Element e = new Element("Floor");
		e.addContent(new Element("xPos").setText(Integer.toString(floor.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(floor.getyPos())));

		if(!(floor.getAvatar()==null))e.addContent(new Element("characterOnTile").setText(floor.getAvatar().getPlayerName()));

		Element itemsOnTile = new Element("itemsOnTile");		//creating new element for list of items on tile
		if(!floor.getItems().isEmpty()){
			for(Item i: floor.getItems())itemsOnTile.addContent(new Element("item").setText(i.getDescription()));//add item to itemsOnTile element
		}
		e.addContent(itemsOnTile);

		return e;
	}

	/**
	 * 	 * Parse the details of a wall
	 * very similar to parseTiles
	 *
	 * @param wall
	 * @return Element
	 */

	public Element parseWall(Wall wall){
		Element e = new Element("Wall");
		e.addContent(new Element("xPos").setText(Integer.toString(wall.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(wall.getyPos())));
		//e.addContent(new Element("type").setText(wall.getType()));
		//e.addContent(new Element("room").setText(Integer.toString(wall.getRoom().getRoomNumber()))); 		//ROOM NUMBER
		if(!(wall.getAvatar() == null))e.addContent(new Element("characterOnTile").setText(wall.getAvatar().getPlayerName()));
		Element itemsOnTile = new Element("itemsOnTile");		//creating new element for list of items on tile
		if(!wall.getItems().isEmpty()){
			for(Item i: wall.getItems()){		//iterate through list
				itemsOnTile.addContent(new Element("item").setText(i.getDescription()));//add item to itemsOnTile element
			}
		}
		e.addContent(itemsOnTile);

		return e;
	}

	/**
	 * 	private Room room;
		private int toRoomIndex;
		private int toRoomXPos;
		private int toRoomYPos;
		private boolean locked;
		//private List<Key> unlockKeys;		//WILL BE IMPLEMENTED LATER
	 *
	 *
	 * @param door
	 * @return Element
	 */

	public Element parseDoor(Door door){
		Element e = new Element("Door");
		e.addContent(new Element("xPos").setText(Integer.toString(door.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(door.getyPos())));
		e.addContent(new Element("toRoom").setText(door.getToRoom().getRoomPlace()));
//		Element color = new Element("color");
//		color.addContent(new Element("Red").setText(Integer.toString((door.getColor().getRed()))));
//		color.addContent(new Element("Green").setText(Integer.toString((door.getColor().getGreen()))));
//		color.addContent(new Element("Blue").setText(Integer.toString((door.getColor().getBlue()))));
//		e.addContent(color);



		if(!(door.getAvatar()==null))e.addContent(new Element("characterOnTile").setText(door.getAvatar().getPlayerName()));

		Element itemsOnTile = new Element("itemsOnTile");		//creating new element for list of items on tile

		if(!door.getItems().isEmpty()){
			for(Item i: door.getItems()){		//iterate through list
				itemsOnTile.addContent(new Element("item").setText(i.getDescription()));//add item to itemsOnTile element

			}
		}
		e.addContent(itemsOnTile);

		return e;
	}

	/**
	 * Takes a passed Cell object and creates an xml element that represents it
	 * 	private double batteryLife;
		Avatar avatar;
		boolean charging;
	 *
	 * @param cell
	 * @return Element representing the parsed cell
	 */
	public Element parseCell(Cell cell){
		Element e = new Element("cell");
		e.addContent(new Element("batteryLife").setText(Integer.toString(cell.getBatteryLife())));
		e.addContent(new Element("charging").setText(Boolean.toString(cell.isCharging())));
		return e;
	}
	/**
	 * Takes either a charger, column, or tree as a parameter and creates an xml element that represents it
	 * 	private int xPos;
		private int yPos;
		private Room room;
	 * @param column
	 * @return Element representing the parsed column
	 */

	public Element parseOtherTile2D(Tile2D tile){
		Element e;
		if(tile instanceof Charger){
			e = new Element("Charger");
		}
		else if(tile instanceof Column){
			e = new Element("Column");
		}
		else { e = new Element("Tree");}//instanceof tree
		e.addContent(new Element("xPos").setText(Integer.toString(tile.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(tile.getyPos())));


		return e;

	}

}
