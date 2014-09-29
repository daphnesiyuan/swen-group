package rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import networking.ChatMessage;
import GUI.DrawingPanel;
import gameLogic.Avatar;
import gameLogic.Item;
import gameLogic.Room;
import gameLogic.Tile2D;

/**
 * This class will draw the location and everything in it.
 * It will also draw the inventory and compas.
 * @author northleon
 *
 */
public class DrawWorld {

	Avatar character; // the main player

	double scale;
	int width;
	int height;
	Point offset = new Point(600,600);
	JPanel panel;
	boolean rotated90 = false; // used as a cheap way to show room rotation by flipping the images horizontally.
	Map<String, Integer> directionMap = new HashMap<String, Integer>();

	public DrawWorld(Avatar character, Rendering rendering){
		this.character = character;
		this.panel = rendering;
		directionMap.put("north", 0);
		directionMap.put("west", 1);
		directionMap.put("south", 2);
		directionMap.put("east", 3);
	}

	//This constructor is only for testing
	public DrawWorld(Avatar character, DrawingPanel rendering){
		this.character = character;
		this.panel = rendering;
		directionMap.put("north", 0);
		directionMap.put("west", 1);
		directionMap.put("south", 2);
		directionMap.put("east", 3);
	}


	/**
	 * This method will be call externally from the UI to draw everything gameplay related
	 * @param Graphics g
	 * @param Room room
	 * @param Avatar character
	 * @param String direction
	 */
	public void redraw(Graphics g, Room room, String direction){

		//set scaling based on frame size
		scale = 50 * (panel.getWidth()/1280.0);
		width =(int) (1 * scale);
		height = width;



		//set offset based on character position.
		//This doesn't really work very well because the tiles x
		//and y will not change with my rotate. Will fix later.

		System.out.println(offset.toString());

		Point temp = twoDToIso(new Point(character.getCurrentTile().getXPos()
				* height, character.getCurrentTile().getYPos() * width));
		System.out.println("temp: "+temp.toString());
		System.out.println(character.getCurrentTile().getXPos());
		System.out.println(height);
		System.out.println(character.getCurrentTile().getYPos());
		System.out.println(width);
		offset.x = (panel.getWidth() / 2) + temp.x; // need to get rid of magic
													// numbers
		offset.y = (panel.getHeight()/2) - temp.y;

		System.out.println(offset.toString());



		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		drawLocation(g, room, direction);
//		drawInventory(g);
//		drawCompas(g);

	}



	/**
	 * Gets tiles from the room provided. Rotates them to the direction
	 * provided. Draws the tiles to the Graphics provided using the placeTile()
	 * method.
	 * @param Graphics g
	 * @param Room room
	 * @param String direction
	 */
	private void drawLocation(Graphics g, Room room, String direction) {
		// TODO Auto-generated method stub
		Tile2D[][] tiles = room.getTiles().clone();

		//rotate the game the correct number of tiles
		for (int i = 0; i < directionMap.get(direction); i++){
			tiles = rotate90(tiles);
		}

		//Temporary code here, this sets the rotated90 boolean which is used to flip images
		if(directionMap.get(direction) == 1 ||directionMap.get(direction) == 3){
			rotated90 = true;
		}
		else{
			rotated90 = false;
		}

		for(int i = 0; i < tiles.length; i++){
			for (int j = 0; j < tiles[i].length; j++) {
				int x = i * width;
				int y = j * height;
				placeTile(twoDToIso(new Point(x,y)),tiles[i][j],g);
			}
		}
	}

	/**
	 * Rotates the given 2d array 90 degrees
	 * @param Tile2D[][] tiles
	 * @return Tile2D[][] newTiles
	 */
	private Tile2D[][] rotate90(Tile2D[][] tiles) {
		// TODO Auto-generated method stub
	    int width = tiles.length;
	    int height = tiles[0].length;
	    Tile2D[][] newTiles = new Tile2D[height][width];
	    for (int i = 0; i < height; ++i) {
	        for (int j = 0; j < width; ++j) {
	            newTiles[i][j] = tiles[width - j - 1][i];
	        }
	    }
	    return newTiles;
	}

	/**
	 * Takes the name of the class and gets drawObject(...) to draw it.
	 * @param Point pt
	 * @param String tileName
	 * @param Graphics g
	 */
	private void placeTile(Point pt, Tile2D tile, Graphics g) {
		String tileName = tile.getClass().getName();
		java.net.URL imageURL = Rendering.class.getResource(tileName+".png");
		drawObject(g, pt, imageURL);

		drawItems(g, pt, tile);
		drawCharacter(g, pt, tile);
	}

	/**
	 * Generic drawing method that gets called to draw Tile2D, GameCharacter, Item
	 * @param Graphics g
	 * @param Point pt
	 * @param java.net.URL imageURL
	 */
	private void drawObject(Graphics g, Point pt, java.net.URL imageURL){
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (rotated90){
			// Flip the image horizontally
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-img.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter((BufferedImage) img, null);
		}
		int imgHeight = ((int) img.getHeight(null)/20);

		g.drawImage(img, offset.x+pt.x - width, offset.y+pt.y - ((width*imgHeight)),width*2, height*imgHeight, null);
	}

	/**
	 * Takes the name of the class and gets drawObject(...) to draw it.
	 * @param Point pt
	 * @param Tile2D tile
	 * @param Graphics g
	 */
	private void drawCharacter(Graphics g, Point pt, Tile2D tile) {
		// this if statement is only used until ryan finished the getCharacter() method on Tile2D class
		String characterName = null;
		if (character.getCurrentTile().getXPos() == tile.getXPos() &&
				character.getCurrentTile().getYPos() == tile.getYPos()){
			characterName = character.getClass().getName();
		} else{
			return;
		}

		//if (tile.getCharacter == null){return;}
		//String characterName = tile.getCharacter.getClass().getName();
		java.net.URL imageURL = Rendering.class.getResource(characterName+".png");
		drawObject(g, pt, imageURL);
	}

	/**
	 * Takes the name of the class and gets drawObject(...) to draw it.
	 * @param Point pt
	 * @param Tile2D tile
	 * @param Graphics g
	 */
	private void drawItems(Graphics g, Point pt, Tile2D tile) {
//		Currently this method does not work because tile.getItem()
//		throws an out of bounds exception

//
//		Item tempItem = tile.getItem() ;
//		while (tempItem != null){
//			System.out.println(tempItem.toString());
//			tempItem = tile.getItem();
//		}


		Item tempItem = tile.getTopItem() ;
		while (tempItem != null){
			System.out.println(tempItem.toString());
			tempItem = tile.getTopItem();
		}


	}

	public void setCharacter(Avatar character){
		this.character = character;
	}

	/**
	 * converts the coordinates of a 2d array to isometric
	 * @param Point point
	 * @return Point tempPt
	 */
	private Point twoDToIso(Point point) {
		  Point tempPt = new Point(0,0);
		  tempPt.x = point.x - point.y;
		  tempPt.y = (point.x + point.y) / 2;
		  return(tempPt);
	}
}
