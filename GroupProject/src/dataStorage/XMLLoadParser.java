package dataStorage;

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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLLoadParser {

	File file;
	Game game;
	Tile2D[][] tiles =new Tile2D[1000][1000];

	public XMLLoadParser(File f){
		file = f;

		game = new Game(true);
	}

	public Game loadGame(){
		SAXBuilder builder = new SAXBuilder();
		try{
			Document doc = (Document) builder.build(file);
			Element rootNode = doc.getRootElement();		//Should be "game"
			//System.out.println("A node of the root is: "+rootNode.toString());
			List list = rootNode.getChildren();		//a list of rooms
			System.out.println(list.toArray().toString());
			for(int i = 0; i<list.size();i++){
				Element node = (Element) list.get(i);		//room at i
			//	System.out.println("A node of the root is: "+node.toString());
				parseRoom(node);		//will proceed to go through all the rooms in a game and parse the deetz
			}
			int i = 0;
			for(Room room: game.getRoomsInGame()){
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }

		System.out.println("Finished saving???");
		return game;
	}

	/**
	 * goes through the xml representation element (e) and creates a new room object
	 * and calls the correct methods to delegate the parsing of the information
	 * held inside the rooms
	 *
	 * @param e
	 */
	public void parseRoom(Element e){
		List avatarList = null;
		List itemList = null;
		List otherTilesList = null;

		String roomPlace = e.getChildText("roomPlace");
		if(e.getChild("avatars")!= null){
			Element avatars = e.getChild("avatars");
			avatarList = avatars.getChildren();
		}
		Element floors = e.getChild("floors");
		List floorList = floors.getChildren();
		Element walls = e.getChild("walls");
		List wallList = walls.getChildren();
		Element doors = e.getChild("doors");
		List doorList = doors.getChildren();
		if(e.getChild("items")!= null){
			Element items = e.getChild("items");
			itemList = new ArrayList();
			itemList = items.getChildren();
		}
		if(e.getChild("other_tiles")!=null){
			Element otherTiles = e.getChild("other_tiles");
			otherTilesList = otherTiles.getChildren();
		}
		Room room = new Room(tiles, null);

		//floors
		for(int i = 0; i<floorList.size();i++){
			parseFloor((Element)floorList.get(i),room);
		}
		//walls
		for(int i = 0; i<wallList.size();i++){
			parseWall((Element)wallList.get(i),room);
		}
		//doors
		for(int i = 0; i<doorList.size();i++){
			parseDoor((Element)doorList.get(i),room);
		}
		//characters
		if(avatarList!=null){
		for(int i = 0; i<avatarList.size();i++){
			parseAvatar((Element)avatarList.get(i),room);

		}
		}
		//items
		if(itemList!=null){
		for(int i = 0; i<itemList.size();i++){
			parseItem((Element)itemList.get(i),room);

		}
		}
		//other tiles
		if(otherTilesList!=null){
		for(int i = 0; i<otherTilesList.size();i++){
			parseOtherTile((Element)otherTilesList.get(i),room);
		}
		}
		room.setTiles(tiles);
		game.addRoom(room);


	}

	/**
	 * goes through the xml representation element (e) of an avatar and creates a
	 * new avatar according to the information given.
	 *
	 * @param e
	 */

	public void parseAvatar(Element e, Room r){
		String faceString = e.getChildText("facing");
		String playerName = e.getChildText("playerName");
		Element inventory = e.getChild("inventory");
		List<Item> playerInventory = new ArrayList<Item>();
		if (inventory.getChildren().size() !=0) {
			for(int i = 0; i<inventory.getChildren().size();i++){
				//TODO: add item to player inventory
			}
		}
		Element cellEle = e.getChild("cell");
		Cell cell = parseCell(cellEle, r);
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		Avatar a = new Avatar(playerName, r.getTiles()[xPos][yPos], r);
		//a.setCell(cell);
		a.setInventory(playerInventory);
	}

	/**
	 * Goes through the xml representation element (e) of a floor tile and creates
	 * a new floor object, adding to the game
	 *
	 *
	 * @param e
	 */

	public void parseFloor(Element e, Room r){
		/*
		 * xPos
		 * yPos
		 * roomnumber
		 * type
		 * items on tile
		 * characters on tile
		 */


//		for(Element ele: e.getChild("Floor").getChildren()){
//			System.out.println(ele);
//
//		}
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		//String characterOnTile = e.getChildText("characterOnTile");
		Element inventory = e.getChild("itemsOnTile");//WAITING FOR ITEMS TO BE IMPLEMENTED
		List<Item> items = new ArrayList<Item>();
		if (inventory.getChildren().size() !=0) {
			for(int i = 0; i<inventory.getChildren().size();i++){
				//TODO: add item to floor tile
			}
		}
		Floor f = new Floor(xPos,yPos);
		f.setRoom(r);
		//System.out.println(characterOnTile);
		//if(characterOnTile!="NULL"){f.setAvatarOnTile(game.getAvatar(characterOnTile));}

		//setting the floor tile
		tiles[xPos][yPos] = f;

	}

	/**
	 * Goes through the xml representation element (e) of a wall tile and creates
	 * a new wall object, adding to the game
	 *
	 *
	 * @param e
	 */

	public void parseWall(Element e, Room r){
		/*
		 * xPos
		 * yPos
		 * type
		 * roomnum
		 * itemsOnTile
		 * characterOnTile // impossible
		 */

		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		List items = e.getChildren("itemsOnTile");
		//bringing in the xml info
		Wall w = new Wall(xPos,yPos);
		w.setRoom(r);
		tiles[xPos][yPos] = w;
	}

	/**
	 * Goes through the xml representation element (e) of a door tile and creates
	 * a new door object, adding to the game
	 *
	 *
	 * @param e
	 */


	public void parseDoor(Element e, Room r){
		/*
		 *. roomNumber
		 * locked
		 *. xPos
		 *. yPos
		 *. type
		 *. itemsOnTile
		 *. avatarOnTile
		 */
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		String roomPlace = e.getChildText("toRoom");//maybe will not need?
		//String characterOnTile = e.getChildText("characterOnTile");
		//Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Door d = new Door(xPos,yPos);
		d.setRoom(r);
		//d.setAvatarOnTile(a);
		tiles[xPos][yPos] = d;
	}

	/**
	 *
	 *
	 * @param e Element that represents the item
	 * @param r Roon that contains the item
	 */

	public Item parseItem(Element e, Room r){
		return null;
	}

	/**
	 *
	 *
	 * @param e Element that represents the other tile
	 * @param r Roon that contains the other tile
	 */

	public void parseOtherTile(Element e, Room r){
		String type = e.getName();
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		if(type.equals("Tree")){
			Tree t = new Tree(xPos, yPos);
			r.getTrees().add(t);
		}
		else if(type.equals("Column")){
			Column c = new Column(xPos, yPos);
			r.getColumns().add(c);
		}
		else if(type.equals("Charger")){
			Charger c = new Charger(xPos, yPos);
			System.out.println("Charger created but cannot add");
		}
	}

	public Cell parseCell(Element e, Room r){
		int batteryLife = Integer.parseInt(e.getChildText("batteryLife"));
		boolean charging = Boolean.parseBoolean(e.getChildText("charging"));
		Cell c = new Cell(null);
		c.setBatteryLife(batteryLife);
		c.setCharging(charging);
		return c;
	}
}