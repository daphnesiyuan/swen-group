package dataStorage;

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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import networking.AI;
import networking.RandomAI;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLLoadParser {

	File file;
	Game game;
	List<Room> roomsInGame;

	public XMLLoadParser(File f) {
		file = f;
		roomsInGame = new ArrayList<Room>();
		game = new Game(true);
	}

	public Game loadGame() {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = (Document) builder.build(file);
			Element rootNode = doc.getRootElement(); // Should be "game"
			List fullList = rootNode.getChildren();
			List roomList = fullList.subList(0, fullList.size() - 2);
			Element scoresElement = (Element) fullList.get(fullList.size() - 2);
			Element aiElement = (Element) fullList.get(fullList.size() - 1);

			parseRooms(roomList);
			parseScores(scoresElement);
			parseAI(aiElement);

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		return game;
	}

	public void parseRooms(List roomList) {
		for (Object e : roomList) {
			Element ele = (Element) e;
			String roomPlace = ele.getChildText("roomPlace");
			Tile2D[][] tiles = new Tile2D[15][15];
			System.out.println("RoomPlace: "+ roomPlace);
			if(!roomPlace.equals("arena")){
				System.out.println("inside");
				tiles = new Tile2D[7][7];}
			System.out.println("Outside");

			Room r = new Room(tiles, null);
			r.setRoomPlace(roomPlace);
			Room room = parseBasicTile(ele, r);
			room.setTiles(tiles);
			roomsInGame.add(room);

		} // Iterates through all the room elements, creates rooms with their
			// place name, and all the walls and floors
		// The room in roomsInGame is at the same index as it is in roomList
		for (int i = 0; i < roomsInGame.size(); i++) {
			parseDoor((Element) roomList.get(i), roomsInGame.get(i));
			parseOtherTile((Element) roomList.get(i), roomsInGame.get(i));
		}
		for (Room r : roomsInGame) {
			game.addRoom(r);
			System.out.println(r.getRoomPlace());
			for (int i = 0; i < r.getTiles().length; i++) {
				System.out.println();
				for (int j = 0; j < r.getTiles().length; j++) {

					System.out.print(r.getTiles()[i][j].getxPos() + " ");
				}
			}
		}


		// Iterates through all the room elements, creates door tiles and
		// other tiles

	}

	/**
	 * Goes through the xml representation element (e) of a wall and floor tile
	 * and creates a new wall object, adding to the game
	 *
	 *
	 * @param e
	 */

	public Room parseBasicTile(Element e, Room r) {

		Element floors = e.getChild("floors");
		List floorList = floors.getChildren();
		Element walls = e.getChild("walls");
		List wallList = walls.getChildren();

		List basicTiles = new ArrayList();
		basicTiles.addAll(floorList);
		basicTiles.addAll(wallList);
		Tile2D tile = new Tile2D(-1000, 0);
		for (Object t : basicTiles) {
			Element ele = (Element) t;

			int xPos = Integer.parseInt(ele.getChildText("xPos"));
			int yPos = Integer.parseInt(ele.getChildText("yPos"));

			if (ele.getName().equals("Wall")) {
				tile = new Wall(xPos, yPos);
				r.tiles2DSet(yPos, xPos, tile);
			} else if (ele.getName().equals("Floor")) {
				tile = new Floor(xPos, yPos);
				r.tiles2DSet(yPos, xPos, tile);
			}
		}

		return r;
	}

	public void parseDoor(Element e, Room r) {
		Element doors = e.getChild("doors");
		List doorList = doors.getChildren();
		for (Object t : doorList) {
			Element ele = (Element) t;
			String color = ele.getChildText("color");
			int xPos = Integer.parseInt(ele.getChildText("xPos"));
			int yPos = Integer.parseInt(ele.getChildText("yPos"));
			String toRoomPlace = ele.getChildText("toRoom");
			Door d = null;
			if(color == "red"){d = new RedDoor(xPos, yPos);}
			else if(color == "yellow"){d = new YellowDoor(xPos, yPos);}
			else if(color == "purple"){d = new PurpleDoor(xPos, yPos);}
			else if(color == "green"){d = new GreenDoor(xPos, yPos);}

			d.setToRoom(game.getRoomByName(toRoomPlace));
			r.tiles2DSet(yPos, xPos, d);

		}
	}

	/**
	 * Takes an element and parses this information converting it to Score
	 * objects and adding it to the game.
	 *
	 * @param scores
	 *            an Element representing the score in the game
	 */

	/**
	 * A method that is given a Element and a Room object. It figures out which
	 * type of "other" tile to make and then creates and puts the item in the
	 * room
	 *
	 * @param e
	 *            Element that represents the other tile
	 * @param r
	 *            Room that contains the other tile
	 */

	public void parseOtherTile(Element e, Room r) {
		Element otherTiles = e.getChild("other_tiles");
		List otList = otherTiles.getChildren();
		for (Object t : otList) {
			Element ele = (Element) t;
			String type = ele.getName();
			int xPos = Integer.parseInt(ele.getChildText("xPos"));
			int yPos = Integer.parseInt(ele.getChildText("yPos"));
			if (type.equals("Tree")) {
				Tree tr = new Tree(xPos, yPos);
				r.getTrees().add(tr);
				r.tiles2DSet(yPos, xPos, tr);
			} else if (type.equals("Column")) {
				Column c = new Column(xPos, yPos);
				r.getColumns().add(c);
				r.tiles2DSet(yPos, xPos, c);
			} else if (type.equals("Charger")) {
				Charger c = new Charger(xPos, yPos);
				r.getChargers().add(c);
				r.tiles2DSet(yPos, xPos, c);
			}
		}
	}

	/**
	 *
	 *
	 * @param e
	 *            Element that represents the item
	 * @param r
	 *            Roon that contains the item
	 */

	public void parseItem(Element e, Room room, Avatar avatar) {
		Item item = null;
		Element tile = e.getChild("tile");
		Element startTile = e.getChild("startTile");
		int xPos = Integer.parseInt(tile.getChild("xPos").getText());
		int yPos = Integer.parseInt(tile.getChild("yPos").getText());
		int startXPos = Integer.parseInt(startTile.getChild("xPos").getText());
		int startYPos = Integer.parseInt(startTile.getChild("yPos").getText());

		Tile2D t = null;
		Tile2D sT = room.getTiles()[startYPos][startXPos];
		if (room != null) {
			t = room.getTiles()[yPos][xPos];

		}
		if (e.getName().equals("key")) {
			String color = e.getChild("color").getText();
			if (color.equals("red")) {
				item = new RedKey(t);
			} else if (color.equals("green")) {
				item = new GreenKey(t);
			} else if (color.equals("purple")) {
				item = new PurpleKey(t);
			} else {// yellow
				item = new YellowKey(t);
			}
		}
		if (e.getName().equals("light")) {
			item = new Light(t);
		}
		if (e.getName().equals("shoes")) {
			item = new Shoes(t);
		}
		if (e.getName().equals("box")) {
			item = new Box(t);
		}

		if (room == null) {
			avatar.getInventory().add(item);
		} else if (avatar == null) {
			room.getItems().add(item);
		}
	}

	/**
	 * goes through the xml representation element (e) of an avatar and creates
	 * a new avatar according to the information given.
	 *
	 * @param e
	 */

	// public void parseAvatar(Element e, Room r) {
	// String faceString = e.getChildText("facing");
	// String playerName = e.getChildText("playerName");
	//
	// Element cellEle = e.getChild("cell");
	// Cell cell = parseCell(cellEle);
	// int xPos = Integer.parseInt(e.getChildText("xPos"));
	// int yPos = Integer.parseInt(e.getChildText("yPos"));
	// Avatar a = new Avatar(playerName, r.getTiles()[yPos][xPos], r);
	// Element inventory = e.getChild("inventory");
	// List<Item> playerInventory = new ArrayList<Item>();
	// if (inventory.getChildren().size() != 0) {
	// for (int i = 0; i < inventory.getChildren().size(); i++) {
	// parseItem(inventory.getChildren().get(i), null, a);
	// }
	// }
	// a.setCell(cell);
	// a.setInventory(playerInventory);
	// }

	public void parseScores(Element scores) {
		Score score = new Score();
		Map<String, Integer> scoreMap = new HashMap<String, Integer>();
		List allScores = scores.getChildren();
		for (int i = 0; i < allScores.size(); i++) {
			Element current = (Element) allScores.get(i);
			String name = current.getName();
			int value = Integer.parseInt(current.getText());
			scoreMap.put(name, value);
		}
		score.setScores(scoreMap);
		game.setScore(score);
	}

	/**
	 * Takes an element that represents the AIs in the loaded game, and then
	 * creates AIs according to this information.
	 *
	 * @param ai
	 *            an Element that stores information about the AI in the game
	 */

	public void parseAI(Element ai) {
		List aiList = ai.getChildren();
		for (int i = 0; i < aiList.size(); i++) {
			Element ele = (Element) aiList.get(i);
			RandomAI a = new RandomAI((game.getRoomByName(ele.getText())), "ai"
					+ i);
		}
	}

	/**
	 * A helper method only called in parse avatar that creates a cell object
	 * based on an Element representing this cell. It then returns this object.
	 *
	 * @param e
	 *            Element representing the cell
	 * @return a Cell object
	 */

	public Cell parseCell(Element e) {
		int batteryLife = Integer.parseInt(e.getChildText("batteryLife"));
		boolean charging = Boolean.parseBoolean(e.getChildText("charging"));
		Cell c = new Cell(null);
		c.setBatteryLife(batteryLife);
		c.setCharging(charging);
		return c;
	}
}