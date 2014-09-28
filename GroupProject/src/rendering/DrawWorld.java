package rendering;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import GUI.DrawingPanel;
import gameLogic.entity.GameCharacter;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.physical.Item;

/**
 * This class will draw the location and everything in it.
 * It will also draw the inventory and compas.
 * @author northleon
 *
 */
public class DrawWorld {

	GameCharacter character; // the main player

	double scale;
	int width;
	int height;
	Point offset = new Point(600,150);
	JPanel panel;
	boolean rotated90 = false; // used as a cheap way to show room rotation by flipping the images horizontally.
	Map<String, Integer> directionMap = new HashMap<String, Integer>();

	public DrawWorld(GameCharacter character, Rendering rendering){
		this.character = character;
		this.panel = rendering;
		directionMap.put("north", 0);
		directionMap.put("west", 1);
		directionMap.put("south", 2);
		directionMap.put("east", 3);
	}

	//This constructor is only for testing
	public DrawWorld(GameCharacter character, DrawingPanel rendering){
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
	 * @param GameCharacter character
	 * @param String direction
	 */
	public void redraw(Graphics g, Room room, GameCharacter character, String direction){

		//set offset based on character position.
		//This doesn't really work very well because the tiles x
		//and y will not change with my rotate. Will fix later.
		if (character != null){
			Point temp = twoDToIso(new Point(character.getCurrentTile().getXPos()*height, character.getCurrentTile().getYPos()*width));
			offset.x = (panel.getWidth()/2)+ temp.x;  //will get rid of magic numbers
			offset.y = (panel.getHeight()/4) + temp.y;
		}

		//set scaling based on frame size
		scale = 50 * (panel.getWidth()/1280.0);
		width =(int) (1 * scale);
		height = width;

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
				//String tileName = tiles[i][j].getClass().getName();
				placeTile(twoDToIso(new Point(x,y)),tiles[i][j],g);
			}
		}
	}

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
	 * Takes the name of the class and draws that image that is stored to the
	 * graphics at the point provided
	 * @param Point pt
	 * @param String tileName
	 * @param Graphics g
	 */
	private void placeTile(Point pt, Tile2D tile, Graphics g) {
		String tileName = tile.getClass().getName();
		java.net.URL imageURL = Rendering.class.getResource(tileName+".png");

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

		drawItems(g, pt, tile);
	}

	private void drawItems(Graphics g, Point pt, Tile2D tile) {
		//Item tempItem = tile.getItem() ;
		//while (tile.getItem() !=

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


	/**
	 * Takes a 2d array and will rotate it by 90 degree segments as dictated by the string direction.
	 * The actual rotation is handled by rotateHelper(...).
	 * North - tiles should not be rotated.
	 * West - tiles will be rotated 90 degrees
	 * South - tiles will be rotated 180 degrees
	 * East - tiles will be rotated 270 degrees
	 * @param Tile2D[][] tiles
	 * @param String direction
	 * @return Tile2d[][] tiles
	 */
	private Tile2D[][] rotate2DArray(Tile2D[][] tiles, String direction) {
		int rotations = directionMap.get(direction);
		Tile2D[][] newTiles = tiles.clone();


		if (direction == null || direction.equalsIgnoreCase("north")){
			return newTiles;
		}
//		else{
//			if (direction.equalsIgnoreCase("west")){
//				newTiles = rotateHelper(tiles, 1);
//				System.out.println("west");
//			}
//			if (direction.equalsIgnoreCase("south")){
//				newTiles = rotateHelper(tiles, 2);
//				System.out.println("south");
//			}
//			if (direction.equalsIgnoreCase("east")){
//				newTiles = rotateHelper(tiles, 3);
//				System.out.println("east");
//			}
//		}


		return newTiles;
	}




}
