package dataStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import networking.AI;

import org.jdom2.Element;

import gameLogic.Avatar;
import gameLogic.Box;
import gameLogic.Cell;
import gameLogic.Charger;
import gameLogic.Column;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Game;
import gameLogic.GreenDoor;
import gameLogic.GreenKey;
import gameLogic.Item;
import gameLogic.Key;
import gameLogic.Light;
import gameLogic.PurpleDoor;
import gameLogic.PurpleKey;
import gameLogic.RedDoor;
import gameLogic.RedKey;
import gameLogic.Room;
import gameLogic.Score;
import gameLogic.Shoes;
import gameLogic.Tile2D;
import gameLogic.Tree;
import gameLogic.Wall;
import gameLogic.YellowDoor;
import gameLogic.YellowKey;

/**
 * A class that acts as a parser for the XML saving process.
 * The main purpose of this class is to parse the seperate parts
 * of the current game state and return an element whether the
 * parser was successful or not.
 *
 * @author Antonia Caskey
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


		Element doors = new Element("doors");		//doors
		Element floors = new Element("floors");		//floors
		Element walls = new Element("walls");		//walls
		Element others = new Element("other_tiles");	//Other tiles
		Element avatars = new Element("avatars");		//characters
		Element items = new Element("items");			//items
		e.addContent(new Element("roomPlace").setText(room.getRoomPlace()));		//room place

		//CHARACTERS
		if(! room.getAvatars().isEmpty()){
			for(Avatar a: room.getAvatars()){
				avatars.addContent(parseAvatar(a));
			}
		}

		if(! room.getItems().isEmpty()){
			for(Item i: room.getItems()){
				items.addContent(parseItem(i));
			}

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
				e.addContent(avatars);
				e.addContent(items);
				e.addContent(floors);
				e.addContent(walls);
				e.addContent(doors);
				e.addContent(others);
		}
		else{return null;}//IMPOSSIBLE TO HAVE ROOM W.O. floor
		return e;
	}

	/**
	 * Is given an Avatar object and parses all the vital information
	 * from the object into an XML element. Then the Element is returned.
	 *
	 * @param avatar
	 * @return Element
	 */

	public Element parseAvatar(Avatar avatar){
		Element e = new Element("Avatar");
		Element cell = new Element("cell");
		Element startTile = new Element("startTile");
		Element tile = new Element("tile");
		Element inventory = new Element("inventory");		//Inventory

		Element score = new Element("score").setText(Integer.toString(avatar.getScore()));
		Element startRoom = new Element("startRoom").setText(avatar.getStartTile().getRoom().getRoomPlace());
		//doesn't need last hit becasuse cannot load a game with other avatars.
		e.addContent(new Element("playerName").setText(avatar.getPlayerName()));		//player name
		e.addContent(new Element("facing").setText(avatar.getFacing().name()));			//Facing
		if(!avatar.getInventory().isEmpty()){
			for(Item i: avatar.getInventory()){		//iterate through list
				inventory.addContent(parseItem(i));
			}
		}

		tile.addContent(new Element("xPos").setText(Integer.toString(avatar.getCurrentTile().getxPos())));
		tile.addContent(new Element("yPos").setText(Integer.toString(avatar.getCurrentTile().getyPos())));
		startTile.addContent(new Element("startTileXPos").setText(Double.toString(avatar.getStartTile().getxPos())));
		startTile.addContent(new Element("startTileYPos").setText(Double.toString(avatar.getStartTile().getyPos())));
		cell = parseCell(avatar.getCell());

		e.addContent(cell);
		e.addContent(tile);
		e.addContent(startTile);
		e.addContent(inventory);
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

		return e;
	}

	/**
	 * Parse the details of a wall that is given as a parameter and
	 * returns a xml Element that represents the wall
	 *
	 * Does not record the items on the tile
	 * very similar to parseTiles
	 *
	 * @param wall
	 * @return Element
	 */

	public Element parseWall(Wall wall){

		Element e = new Element("Wall");
		e.addContent(new Element("xPos").setText(Integer.toString(wall.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(wall.getyPos())));
		return e;
	}

	/**
	 * Takes a Door object as a parameter and returns an XML Element
	 * representing this door.
	 *
	 * NOTE TO BE REMOVED: Does not parse a full Avatar object when an avatar is on it.
	 * Must look through all tiles and see if there is an avatar created with a matching name
	 *
	 * @param door
	 * @return Element
	 */

	public Element parseDoor(Door door){

		Element e = new Element("Door");
		if(door instanceof RedDoor){
			e.addContent("color").setText("red");
		}
		if(door instanceof YellowDoor){
			e.addContent("color").setText("yellow");
		}
		if(door instanceof PurpleDoor){
			e.addContent("color").setText("purple");
		}
		if(door instanceof GreenDoor){
			e.addContent("color").setText("green");
		}
		e.addContent(new Element("xPos").setText(Integer.toString(door.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(door.getyPos())));
		e.addContent(new Element("toRoom").setText(door.getToRoom().getRoomPlace()));
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
		else {
			e = new Element("Tree");
			}

		e.addContent(new Element("xPos").setText(Integer.toString(tile.getxPos())));
		e.addContent(new Element("yPos").setText(Integer.toString(tile.getyPos())));

		return e;
	}

	/**
	 * Takes a Score object that stores a map of all the players
	 * and their scores, and converts it to an XML element.
	 *
	 * @param score the score of the current game
	 * @return an element representing the map of scores
	 */

	public Element parseScore(Score score){

		Element e = new Element("score");

		Set<Entry<String,Integer>> scoreSet = score.getScore().entrySet();

		for(Entry<String, Integer> entry : scoreSet){
			Element sc = new Element(entry.getKey()).setText(Integer.toString(entry.getValue()));
			e.addContent(sc);
		}

		return e;
	}

	/**
	 * Takes any object that extends the abstract class Item and
	 * discerns what type of Item it is, then returns an appropriately named
	 * element.
	 *
	 * @param item Item object from game
	 * @return Element representing the Item in XML
	 */
	public Element parseItem(Item item){
		Element e = null;

		if(item instanceof Key){
			e = new Element("key");
			if(item instanceof RedKey){
				e.addContent("color").setText("red");
			}
			else if(item instanceof GreenKey){
				e.addContent("color").setText("green");
			}
			else if(item instanceof PurpleKey){
				e.addContent("color").setText("purple");
			}
			else if(item instanceof YellowKey){
				e.addContent("color").setText("yellow");
			}

		}
		else if(item instanceof Light){
			System.out.println("Here2");
			e = new Element("light");
		}
		else if(item instanceof Shoes){
			e = new Element("shoes");
		}
		else if(item instanceof Box){
			e = new Element("box");
			for(Item i : ((Box) item).getContains()){
				e.addContent(new Element("items").setContent(parseItem(i)));
			}
		}
		Element tile = new Element("tile");
		Element startTile = new Element("startTile");

		tile.addContent(new Element("xPos")).setText(Integer.toString((item.getTile().getxPos())));
		tile.addContent(new Element("yPos")).setText(Integer.toString((item.getTile().getyPos())));

		tile.addContent(new Element("xPos")).setText(Integer.toString((item).getStartTile().getxPos()));
		tile.addContent(new Element("yPos")).setText(Integer.toString((item).getStartTile().getyPos()));
		e.addContent(startTile);
		e.addContent(tile);

		return e;
	}

	public Element parseAI(AI ai){
		Element e = new Element("AI");
			e.addContent(new Element("room").setText(ai.getRoom().getRoomPlace()));
		return e;
	}

}
