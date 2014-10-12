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

import javax.swing.text.Document;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLLoadParser {

	File file;
	Game game;
	Tile2D[][] tiles;

	public XMLLoadParser(File f){
		file = f;

		game = new Game(true);
	}

	public Game loadGame(){
		SAXBuilder builder = new SAXBuilder();
		try{
			Document doc = (Document) builder.build(file);
			Element rootNode = (Element) doc.getDefaultRootElement();		//Should be "game"
			List list = rootNode.getChildren();		//a list of rooms

			for(int i = 0; i<list.size();i++){
				Element node = (Element) list.get(i);		//room at i
				parseRoom(node);		//will proceed to go through all the rooms in a game and parse the deetz
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }

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

		String roomPlace = e.getChildText("roomPlace");

		List avatars = e.getChildren("characters");
		List floors = e.getChildren("floors");
		List walls = e.getChildren("walls");
		List doors = e.getChildren("doors");
		List items = e.getChildren("items");
		List otherTiles = e.getChildren("other_tiles");
		Room room = new Room(tiles, doors);

		//floors
		for(int i = 0; i<floors.size();i++){
			parseFloor((Element)floors.get(i),room);
		}
		//walls
		for(int i = 0; i<walls.size();i++){
			parseWall((Element)walls.get(i),room);
		}
		//doors
		for(int i = 0; i<doors.size();i++){
			parseDoor((Element)doors.get(i),room);
		}
		//characters
		for(int i = 0; i<avatars.size();i++){
			parseAvatar((Element)avatars.get(i),room);
		}
		//items
		for(int i = 0; i<items.size();i++){
			parseItem((Element)items.get(i),room);
		}
		//other tiles
		for(int i = 0; i<otherTiles.size();i++){
			parseOtherTile((Element)otherTiles.get(i),room);
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
		Element inventory = e.getChild("inventory");//WAITING FOR ITEMS TO BE IMPLEMENTED
		List<Item> playerInventory = new ArrayList<Item>();
		if (inventory.getChildren().size() !=0) {
			for(int i = 0; i<inventory.getChildren().size();i++){
				//add item to player inventory
			}
		}
//		double tileXPos = Double.parseDouble(e.getChildText("tileXPos"));
//		double tileYPos = Double.parseDouble(e.getChildText("tileXPos"));
//		double globalXPos = Double.parseDouble(e.getChildText("globalXPos"));
//		double globalYPos = Double.parseDouble(e.getChildText("globalYPos"));
		Cell cell = parseCell(e.getChild("cell"), r);
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		Avatar a = new Avatar(playerName, r.getTiles()[xPos][yPos], r);
		a.setCell(cell);
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
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		int roomNumber = Integer.parseInt(e.getChildText("roomNumber"));//maybe will not need?
		String characterOnTile = e.getChildText("characterOnTile");
		List items = e.getChildren("itemsOnTile");
		//bringing in the xml info
		Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Floor f = new Floor(xPos,yPos);
		f.setRoom(r);
		f.setAvatarOnTile(a);
		//f.setType(type);
		//WAITING FOR ITEMS TO BE IMPLEMENTED

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
		int roomNumber = Integer.parseInt(e.getChildText("roomNumber"));//maybe will not need?
		String characterOnTile = e.getChildText("characterOnTile");
		List items = e.getChildren("itemsOnTile");
		//bringing in the xml info
		Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Wall w = new Wall(xPos,yPos);
		w.setRoom(r);
		w.setAvatarOnTile(a);
		//w.setType(type);
		//WAITING FOR ITEMS TO BE IMPLEMENTED

		//setting the floor tile
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
		 *. toRoomIndex
		 *. toRoomXPos
		 *. toRoomYPos
		 * locked
		 *. xPos
		 *. yPos
		 *. type
		 *. itemsOnTile
		 *. avatarOnTile
		 */
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		int roomNumber = Integer.parseInt(e.getChildText("roomNumber"));//maybe will not need?
		String characterOnTile = e.getChildText("characterOnTile");
		List items = e.getChildren("itemsOnTile");
//		List colors = e.getChildren("color");
//		int red = (Integer) colors.get(0);
//		int green = (Integer) colors.get(1);
//		int blue = (Integer) colors.get(2);
		//bringing in the xml info
		Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Door d = new Door(xPos,yPos);
		d.setRoom(r);
		d.setAvatarOnTile(a);
		//WAITING FOR ITEMS TO BE IMPLEMENTED
//		Color c = new Color(red,green,blue);
//		d.setColor(c);
		//setting the floor tile
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
		Double batteryLife = Double.parseDouble(e.getChildText("batteryLife"));
		boolean charging = Boolean.parseBoolean(e.getChildText("charging"));
		Cell c = new Cell(null);
		c.setBatteryLife(batteryLife);
		c.setCharging(charging);
	}
}