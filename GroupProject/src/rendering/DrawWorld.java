package rendering;

import gameLogic.Avatar;

import gameLogic.Charger;
import gameLogic.Column;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Tree;
import gameLogic.Wall;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JPanel;

import GUI.DrawingPanel;

/**
 * This class will draw the location and everything in it.
 *
 * @author northleon
 *
 */
public class DrawWorld {

	FloatingPointer floatingPointer;

	Avatar character; // the main player

	double scale;
	int width;
	int height;
	Point offset = new Point(690, 220);
	JPanel panel;
	boolean rotated90 = false; // used as a cheap way to show room rotation by
								// flipping the images horizontally.
	boolean back = true;
	Point relativeOffset;
	Map<String, BufferedImage> images;
	String direction;

	public DrawWorld(Avatar character, DrawingPanel rendering) {

		floatingPointer = new FloatingPointer();
		this.character = character;
		this.panel = rendering;

		relativeOffset = new Point(character.getCurrentTile().getxPos(),
				character.getCurrentTile().getxPos());

		images = MakeImageMap.makeMap();

	}

	/**
	 * This method will be call externally from the UI to draw everything
	 * gameplay related
	 *
	 * @param Graphics g
	 * @param Room room
	 * @param Avatar character
	 * @param String direction
	 */
	public void redraw(Graphics g, Room room, String direction, Avatar avatar) {

		this.character= avatar;
		this.direction = direction;

		// set scaling based on frame size
		scale = 80 * (panel.getWidth() / 1280.0);
		width = (int) (1 * scale);
		height = width;

		// set offset based on character position.
		calibrateOffset(direction, room);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		drawLocation(g, room, direction);

	}

	private void calibrateOffset(String direction, Room room) {

		Point tile = null;

		Tile2D[][] tiles= room.getTiles().clone();

		for (int i = 0; i < Direction.get(direction)+3; i++){
			 tiles = rotate90(tiles);
		}
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles.length; j++){
				if(tiles[j][i].getAvatar() != null && tiles[j][i].getAvatar().equals(character)){
					tile = new Point(j,i);
					//System.out.println(i+" "+j);
					break;
				}
			}
		}

//		if(tile == null){
//			System.out.println("\n\n\nDrawWorld.calibrateOffset - tile == null");
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		Point avatarOffset = avatarTilePos(tiles[tile.x][tile.y]);

		tile.x = (tile.x * width);
		tile.y = (tile.y * height);
		tile = twoDToIso(tile);

		tile.x = tile.x + avatarOffset.x;
		tile.y = tile.y + avatarOffset.y;
		tile.x = panel.getWidth() - (tile.x + (panel.getWidth() / 2));
		tile.y = (panel.getHeight()/3) - (tile.y - (panel.getHeight() / 4));

		offset = tile;


	}

	/**
	 * Gets tiles from the room provided. Rotates them to the direction
	 * provided. Draws the tiles to the Graphics provided using the placeTile()
	 * method.
	 *
	 * @param Graphics g
	 * @param Room room
	 * @param String direction
	 */
	private void drawLocation(Graphics g, Room room, String direction) {
		// TODO Auto-generated method stub
		Tile2D[][] tiles = room.getTiles().clone();

		// rotate the game the correct number of tiles
		 for (int i = 0; i < Direction.get(direction)+3; i++){
		 tiles = rotate90(tiles);
		 }

//		 Temporary code here, this sets the rotated90 and back boolean which
//		 is used to flip images
		 if(Direction.get(direction) == 1 ||Direction.get(direction) == 3){
		 rotated90 = true;
		 }
		 else{
		 rotated90 = false;
		 }


		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				int x = i * width;
				int y = j * height;
				Point point = twoDToIso(new Point(x, y));
				drawTile(point, tiles[i][j], g);
				drawItems(g, point, tiles[i][j]);
				drawCharacter(g, point, tiles[i][j]);
			}
		}

	}

	/**
	 * Rotates the given 2d array 90 degrees
	 *
	 * @param Tile2D [][] tiles
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
	 *
	 * @param Point pt
	 * @param String tileName
	 * @param Graphics g
	 */
	private void drawTile(Point pt, Tile2D tile, Graphics g) {

		int tileNum = 0;
		if (rotated90){
			tileNum = 1;
		}

		if (tile instanceof Wall){
			drawObject(g, pt, images.get("Wall"+tileNum));
		}
		else if(tile instanceof Floor){
			//drawObject(g, pt, images.get("Floor"+tileNum));
		}
		else if(tile instanceof Door){
			drawObject(g, pt, images.get("Door"+tileNum));
		}
		else if(tile instanceof Column){
			drawObject(g, pt, images.get("Column"+tileNum));
		}
		else if(tile instanceof Tree){
			drawObject(g, pt, images.get("Tree"+tileNum));
		}
		else if(tile instanceof Charger){
			drawObject(g, pt, images.get("Charger"+tileNum));
		}
	}

	/**
	 * Generic drawing method that gets called to draw Tile2D, GameCharacter,
	 * Item
	 *
	 * @param Graphics g
	 * @param Point pt
	 * @param java.net.URL imageURL
	 */
	private void drawObject(Graphics g, Point pt, BufferedImage img) {
		int imgHeight = ((int) img.getHeight(null) / 250);

		g.drawImage(img, offset.x + pt.x - width, offset.y + pt.y
				- ((width * imgHeight)), width * 2, height * imgHeight, null);
	}

	/**
	 * Takes the name of the class and gets drawObject(...) to draw it.
	 *
	 * @param Point pt
	 * @param Tile2D tile
	 * @param Graphics g
	 */
	private void drawCharacter(Graphics g, Point pt, Tile2D tile) {
		if (tile.getAvatar() == null) return;

		Avatar avatar = tile.getAvatar();

		Point avatarOffset = avatarTilePos(tile);

		pt.y-=(height/2);
		pt.x+=avatarOffset.x;
		pt.y+=avatarOffset.y;

		//cases:
		//avatar is ai
		//avatar is charging
		//avatar is player and is not charging
		if(avatar.getCell().isCharging()){
			drawObject(g,pt,images.get("AvatarA"+avatar.getFacing().toString()+"Charging"+avatar.getSpriteIndex()));
		}
		else if (avatar.getPlayerName().startsWith("ai")){
			drawObject(g,pt,images.get("AvatarB"+avatar.getFacing().toString()+""+avatar.getSpriteIndex()));
		}
		else drawObject(g,pt,images.get("AvatarA"+avatar.getFacing().toString()+""+avatar.getSpriteIndex()));

		//either draw a floating pointer if avatar is current player
		//or draw the name above the avatar
		if (tile.getAvatar().equals(character))
			floatingPointer.reDraw(g, pt, width, height, offset);
		else{
			g.setColor(Color.BLUE);
			g.drawString(avatar.getPlayerName(), pt.x+offset.x, pt.y+offset.y-4);
		}
	}

	public Point avatarTilePos(Tile2D tile){

		double stepSize = width/100.0;
		Avatar avatar = tile.getAvatar();

		Point avatarPoint = new Point((int)avatar.getTileXPos(), (int)avatar.getTileYPos());


		for(int i = 0; i < Direction.get(direction)*3; i++){
			avatarPoint = new Point((100-avatarPoint.y),(avatarPoint.x));
	    }

		avatarPoint = twoDToIso(avatarPoint);

		avatarPoint.x = (int)(avatarPoint.x * stepSize);
		avatarPoint.y = (int)(avatarPoint.y * stepSize);

		return avatarPoint;
	}

	/**
	 * Takes the name of the class and gets drawObject(...) to draw it.
	 *
	 * @param Point pt
	 * @param Tile2D tile
	 * @param Graphics g
	 */
	private void drawItems(Graphics g, Point pt, Tile2D tile) {
		if (tile.getItems() == null) return;

			int tileNum = 0;
			if (rotated90){
				tileNum = 1;
			}

			for (int i = 0; i < tile.getItems().size(); i++){
//				if (tile.getItems().get(i) instanceof Batery){
//					drawObject(g,pt,images.get("Battery"+tileNum));
//				}
			}

	}

	/**
	 * converts the coordinates of a 2d array to isometric
	 *
	 * @param Point point
	 * @return Point tempPt
	 */
	private Point twoDToIso(Point point) {
		Point tempPt = new Point(0, 0);
		tempPt.x = point.x - point.y;
		tempPt.y = (point.x + point.y) / 2;
		return (tempPt);
	}
}
