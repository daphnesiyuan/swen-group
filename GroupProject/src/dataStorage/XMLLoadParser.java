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
			List roomList = fullList.subList(0, fullList.size()-2); //The rooms go from 0-number of values -2 (being score and ais)
			Element scoresElement = (Element) fullList.get(fullList.size()-2);		// scores is the second to last element in the list
			Element aiElement = (Element) fullList.get(fullList.size()-1);		//AI is the last element in the list

			parseRooms(roomList);
			parseScores(scoresElement);
			parseAI(aiElement);

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		System.out.println("Finished saving???");
		return game;
	}

	public void parseRooms(List roomList) {

		for (Object e : roomList) {
			Tile2D[][] tiles = new Tile2D[1000][1000];
			Room r = new Room(tiles, null);
			Element ele = (Element) e;
			String roomPlace = ele.getChildText("roomPlace");
			Room room = parseBasicTile(ele, r);
			roomsInGame.add(room);
		} // Iterates through all the room elements, creates rooms with their
			// place name, and all the walls and floors
		for (Object e : roomList) {

		} // Iterates through all the room elements, creates door tiles and
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

		Tile2D tile = new Tile2D(-9999, 0);
		for (Object t : basicTiles) {
			Element ele = (Element) t;
			int xPos = Integer.parseInt(ele.getChildText("xPos"));
			int yPos = Integer.parseInt(ele.getChildText("yPos"));

			if (ele.getName().equals("Wall")) {
				tile = new Wall(xPos, yPos);
			} else if (ele.getName().equals("Floor")) {
				tile = new Floor(xPos, yPos);
			}
			if (tile.getxPos() != -1000) {
				tile.setRoom(r);
				r.getTiles()[xPos][yPos] = tile;
			}
			else {
				System.out.println("Error in parse basic tile");
				return null;
			}
		}

		return r;
	}

	/**
	 * Takes an element and parses this information converting it to
	 * Score objects and adding it to the game.
	 *
	 * @param scores an Element representing the score in the game
	 */

	public void parseScores(Element scores){}

	/**
	 * Takes an element that represents the AIs in the loaded game, and
	 * then creates AIs according to this information.
	 *
	 * @param ai an Element that stores information about the AI in the game
	 */

	public void parseAI(Element ai){}

	/**
	 * goes through the xml representation element (e) of an avatar and creates
	 * a new avatar according to the information given.
	 *
	 * @param e
	 */

	public void parseAvatar(Element e, Room r) {
		String faceString = e.getChildText("facing");
		String playerName = e.getChildText("playerName");
		Element inventory = e.getChild("inventory");
		List<Item> playerInventory = new ArrayList<Item>();
		if (inventory.getChildren().size() != 0) {
			for (int i = 0; i < inventory.getChildren().size(); i++) {
				// TODO: add item to player inventory
			}
		}
		Element cellEle = e.getChild("cell");
		Cell cell = parseCell(cellEle);
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		Avatar a = new Avatar(playerName, r.getTiles()[xPos][yPos], r);
		// a.setCell(cell);
		a.setInventory(playerInventory);
	}

	/**
	 *
	 *
	 * @param e
	 *            Element that represents the item
	 * @param r
	 *            Roon that contains the item
	 */

	public Item parseItem(Element e, Room r) {
		return null;
	}

	/**
	 * A method that is given a Element and a Room object. It figures out
	 * which type of "other" tile to make and then creates and puts the item in the room
	 *
	 * @param e
	 *            Element that represents the other tile
	 * @param r
	 *            Room that contains the other tile
	 */

	public void parseOtherTile(Element e, Room r) {
		String type = e.getName();
		int xPos = Integer.parseInt(e.getChildText("xPos"));
		int yPos = Integer.parseInt(e.getChildText("yPos"));
		if (type.equals("Tree")) {
			Tree t = new Tree(xPos, yPos);
			r.getTrees().add(t);
		} else if (type.equals("Column")) {
			Column c = new Column(xPos, yPos);
			r.getColumns().add(c);
		} else if (type.equals("Charger")) {
			Charger c = new Charger(xPos, yPos);
			r.getChargers().add(c);
		}
	}

	/**
	 * A helper method only called in parse avatar that creates
	 * a cell object based on an Element representing this cell.
	 * It then returns this object.
	 *
	 * @param e Element representing the cell
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