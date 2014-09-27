package rendering;

import java.awt.Color;
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

/**
 * This class will draw the location and everything in it.
 * It will also draw the inventory and compas.
 * @author northleon
 *
 */
public class DrawWorld {

	GameCharacter character; // the main player

	int scale = 40;
	int width = 1 * scale;
	int height = width;
	Point offset = new Point(350,100);
	JPanel panel;
	boolean rotated90; // used as a cheap way to show room rotation by flipping the images horizontally.
	Map<Integer, String> directionMap = new HashMap<Integer, String>();

	public DrawWorld(GameCharacter character, DrawingPanel rendering){
		this.character = character;
		this.panel = rendering;
		directionMap.put(0, "north");
		directionMap.put(1, "west");
		directionMap.put(2, "south");
		directionMap.put(3, "east");
	}


	/**
	 * This method will be call externally from the UI to draw everything gameplay related
	 * @param Graphics g
	 * @param Room room
	 * @param GameCharacter character
	 * @param String direction
	 */
	public void redraw(Graphics g, Room room, GameCharacter character, String direction){
		System.out.println("redraw");
		System.out.println(direction);
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
		Tile2D[][] tiles = rotate2DArray(room.getTiles(), direction);

		for(int i = 0; i < tiles.length; i++){
			for (int j = 0; j < tiles[i].length; j++) {
				int x = i * width;
				int y = j * height;
				String tileName = tiles[i][j].getClass().getName();
				placeTile(twoDToIso(new Point(x,y)),tileName,g);
			}
		}
	}

	/**
	 * Takes the name of the class and draws that image that is stored to the
	 * graphics at the point provided
	 * @param Point pt
	 * @param String tileName
	 * @param Graphics g
	 */
	private void placeTile(Point pt, String tileName, Graphics g) {
		java.net.URL imageURL = Rendering.class.getResource(tileName+".png");

		Image img = null;
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


	/**
	 * Only called from rotate2DArray(). Uses an algorithm to rotate the given
	 * 2D array the given number of times.
	 * @param Tile2D[][] tiles
	 * @param String direction
	 * @return Tile2d[][] tiles
	 */
	private Tile2D[][] rotateHelper(Tile2D[][] tiles, int numRotations) {
	    int w = tiles.length;
	    int h = tiles[0].length;
	    Tile2D[][] newTiles = new Tile2D[h][w]; // new array to return
		for (int k = 0; k < numRotations; k++) { // for the number of rotations
			for (int i = 0; i < h; ++i) { // iterate over the array
				for (int j = 0; j < w; ++j) { // iterate over the array
					newTiles[i][j] = tiles[w - j - 1][i]; //Formulea for the rotation.
					System.out.println(i+" "+j+" "+(w - j - 1)+" "+i);
				}
			}
			rotated90 = !rotated90; //
	    }
	    return newTiles;
	}


}
