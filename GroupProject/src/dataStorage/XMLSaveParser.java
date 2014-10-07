package dataStorage;

import java.util.List;

import org.jdom2.Element;

import gameLogic.Avatar;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Game;
import gameLogic.Item;
import gameLogic.Room;
import gameLogic.Tile2D;
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

/*
 * CODING NOTES
 * Not sure so far if I need to remove the duplicate parsings (items etc)
 * Will make a decision when writing loading
 *
 */

public class XMLSaveParser {

	XMLSaver saver;

	public XMLSaveParser(XMLSaver x){
		saver = x;
	}

	/**
	 *
	int roomNumber;
	Tile2D[][] tiles;
	List <? extends Item> items;
	List<Avatar> characters;
	List<Door> doors;
	List<Floor> floors;
	List<Floor> spawns;
	List<Wall> walls;
	 *
	 * @param room
	 * @return Element
	 */

	public Element parseRoom(Room room){
		Element e = new Element("Room");
		e.addContent(new Element("roomNumber").setText(Integer.toString(room.getRoomNumber())));	//ROOM NUMBER
		//Element tiles = new Element("Tiles2D");//floors//maybe do need to parse 2d tiles D:
		//Element items = new Element("items");//items
		Element characters = new Element("characters");//characters
		Element doors = new Element("doors");//doors
		Element floors = new Element("floors");//floors
		//Element spawns = new Element("spawn");//spawn		//dupe with floors
		Element walls = new Element("walls");//walls

		//CHARACTERS
		if(! room.getAvatars().isEmpty()){
			for(Avatar a: room.getAvatars()){
				characters.addContent(parseAvatar(a));
			}
			e.addContent(characters);
		}
		else{characters.addContent(new Element("NULL"));}//add a NULL string

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
					}

				}
				e.addContent(floors);
				e.addContent(walls);
				e.addContent(doors);
		}
		else{return null;}//IMPOSSIBLE TO HAVE ROOM W.O. floor
		return e;
	}

	/**
	 * Parses an avatar in this order and returns an element
	 *
	 *
	 * Game.Facing facing;
	 *
	 * Room currentRoom;
	 *   X excluded because parse avatar is called in parse tile                          Tile2D currentTile; // current Tile the character is standing on
	 * String playerName;
	 * List <Item> Inventory;
	 *
	 * This is called in parseTile(Floor floor)
	 *
	 * @param avatar
	 * @return Element
	 */

	public Element parseAvatar(Avatar avatar){
		Element e = new Element("Avatar");
		e.addContent(new Element("facing").setText(avatar.getFacing().name()));
		e.addContent(new Element("currentRoom").setText(Integer.toString(avatar.getCurrentRoom().getRoomNumber())));		//ROOM NUMBER
		e.addContent(new Element("playerName").setText(avatar.getPlayerName()));		//add simple fields

		Element inventory = new Element("inventory");		//creating new element for list of items on tile

		if(!avatar.getInventory().isEmpty()){
			for(Item i: avatar.getInventory()){		//iterate through list
				inventory.addContent(new Element("item").setText(i.getDescription()));//add item to inventory element
			}
		}
		else{
			inventory.addContent(new Element("NULL"));//add a NULL string
		}
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
		//e.addContent(new Element("type").setText(floor.getType()));
		e.addContent(new Element("room").setText(Integer.toString(floor.getRoom().getRoomNumber()))); 		//ROOM NUMBER
		//e.addContent(new Element("isSpawn").setText(Boolean.toString(floor.isSpawn())));		//Adding fields
		if(!(floor.getAvatar()==null))e.addContent(new Element("characterOnTile").setText(floor.getAvatar().getPlayerName()));
		else e.addContent(new Element("characterOnTile").setText("NULL"));
		Element itemsOnTile = new Element("itemsOnTile");		//creating new element for list of items on tile

		if(!floor.getItems().isEmpty()){
			for(Item i: floor.getItems()){		//iterate through list
				itemsOnTile.addContent(new Element("item").setText(i.getDescription()));//add item to itemsOnTile element

			}
		}
		else{
			itemsOnTile.addContent(new Element("NULL"));//add a NULL string
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
		e.addContent(new Element("room").setText(Integer.toString(wall.getRoom().getRoomNumber()))); 		//ROOM NUMBER
		if(!(wall.getAvatar() == null))e.addContent(new Element("characterOnTile").setText(wall.getAvatar().getPlayerName()));
		else e.addContent(new Element("characterOnTile").setText("NULL"));
		Element itemsOnTile = new Element("itemsOnTile");		//creating new element for list of items on tile
		if(!wall.getItems().isEmpty()){
			for(Item i: wall.getItems()){		//iterate through list
				itemsOnTile.addContent(new Element("item").setText(i.getDescription()));//add item to itemsOnTile element

			}
		}
		else{
			itemsOnTile.addContent(new Element("NULL"));//add a NULL string
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
		//e.addContent(new Element("type").setText(door.getType()));
		e.addContent(new Element("room").setText(Integer.toString(door.getRoom().getRoomNumber())));			//JUST THE ROOM NUMBER
		e.addContent(new Element("toRoomIndex").setText(Integer.toString(door.getToRoomIndex())));

		Element color = new Element("color");
		color.addContent(new Element("Red").setText(Integer.toString((door.getColor().getRed()))));
		color.addContent(new Element("Green").setText(Integer.toString((door.getColor().getGreen()))));
		color.addContent(new Element("Blue").setText(Integer.toString((door.getColor().getBlue()))));

		e.addContent(color);
		//e.addContent(new Element("toRoomXPos").setText(Integer.toString(door.getToRoomXPos())));
		//e.addContent(new Element("toRoomYPos").setText(Integer.toString(door.getToRoomYPos())));
		//e.addContent(new Element("locked").setText(Boolean.toString(door.getLocked())));

		if(!(door.getAvatar()==null))e.addContent(new Element("characterOnTile").setText(door.getAvatar().getPlayerName()));
		else e.addContent(new Element("characterOnTile").setText("NULL"));

		Element itemsOnTile = new Element("itemsOnTile");		//creating new element for list of items on tile

		if(!door.getItems().isEmpty()){
			for(Item i: door.getItems()){		//iterate through list
				itemsOnTile.addContent(new Element("item").setText(i.getDescription()));//add item to itemsOnTile element

			}
		}
		else{
			itemsOnTile.addContent(new Element("NULL"));//add a NULL string
		}
		e.addContent(itemsOnTile);

		return e;
	}

}
