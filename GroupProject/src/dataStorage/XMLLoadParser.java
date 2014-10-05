package dataStorage;

import gameLogic.Avatar;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Game;
import gameLogic.Item;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Wall;

import java.io.File;
import java.io.IOException;
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

		game = new Game();
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


		int roomNum = Integer.parseInt(e.getChildText("roomNumber"));
		List avatars = e.getChildren("characters");
		List floors = e.getChildren("floors");
		List walls = e.getChildren("walls");
		List doors = e.getChildren("doors");
		Room room = new Room(roomNum,null,null);

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
		int roomNum = Integer.parseInt(e.getChildText("currentRoom"));
		String playerName = e.getChildText("playerName");
		//WAITING FOR ITEMS TO BE IMPLEMENTED
		
		
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
		 * isSpawn
		 * type
		 * items on tile
		 * characters on tile
		 */
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		int roomNumber = Integer.parseInt(e.getChildText("roomNumber"));//maybe will not need?
		boolean isSpawn = Boolean.parseBoolean(e.getChildText("isSpawn"));
		String type = e.getChildText("type");
		String characterOnTile = e.getChildText("characterOnTile");
		List items = e.getChildren("itemsOnTile");
		//bringing in the xml info
		Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Floor f = new Floor(xPos,yPos,isSpawn);
		f.setRoom(r);
		f.setAvatarOnTile(a);
		f.setType(type);
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
		String type = e.getChildText("type");
		int roomNumber = Integer.parseInt(e.getChildText("roomNumber"));//maybe will not need?
		String characterOnTile = e.getChildText("characterOnTile");
		List items = e.getChildren("itemsOnTile");
		//bringing in the xml info
		Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Wall w = new Wall(xPos,yPos);
		w.setRoom(r);
		w.setAvatarOnTile(a);
		w.setType(type);
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
		String type = e.getChildText("type");
		int roomNumber = Integer.parseInt(e.getChildText("roomNumber"));//maybe will not need?
		String characterOnTile = e.getChildText("characterOnTile");
		List items = e.getChildren("itemsOnTile");
		int toRoomIndex = Integer.parseInt(e.getChildText("toRoomIndex"));
		int toRoomXPos = Integer.parseInt(e.getChildText("toRoomXPos"));
		int toRoomYPos = Integer.parseInt(e.getChildText("toRoomYPos"));
		boolean locked = Boolean.parseBoolean(e.getChildText("locked"));

		//bringing in the xml info
		Avatar a = game.getAvatar(characterOnTile);
		//making necessary adjustments and calculations
		Door d = new Door(xPos,yPos);
		d.setRoom(r);
		d.setAvatarOnTile(a);
		d.setType(type);
		//WAITING FOR ITEMS TO BE IMPLEMENTED
		d.setToRoomIndex(toRoomIndex);
		d.setToRoomX(toRoomXPos);
		d.setToRoomY(toRoomYPos);
		d.setLocked(locked);
		//setting the floor tile
		tiles[xPos][yPos] = d;
	}


}