package rendering;

import gameLogic.Avatar;
import gameLogic.Charger;
import gameLogic.Column;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Light;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Tree;
import gameLogic.Wall;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import GUI.DrawingPanel;

/**
 * This class will draw the location and everything in it.
 *
 * @author Leon North
 *
 */
public class DrawWorld {

	private FloatingPointer floatingPointer;

	private Avatar character; // the main player

	private double scale;
	private int width;
	private int height;
	private Point offset = new Point(690, 220);
	private JPanel panel;
	private boolean rotated90 = false; // used as a cheap way to show room rotation by
							           // flipping the images horizontally.
	private Map<String, BufferedImage> images;
	private String direction;

	public DrawWorld(Avatar character, DrawingPanel rendering) {

		floatingPointer = new FloatingPointer();
		this.character = character;
		this.panel = rendering;

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

	/**
	 * Makes the offset that everything needs to be drawn by to put the current
	 * players avatar in the centre of the screen.
	 *
	 * @param direction
	 * @param room
	 * @author Leon North
	 */
	private void calibrateOffset(String direction, Room room) {

		Point tile = null;

		Tile2D[][] tiles= room.getTiles().clone();
		System.out.println( "The real tile is: "+room.getTiles());
		System.out.println( "The FAKE tile is: "+tiles);
		for (int i = 0; i < Direction.get(direction)+3; i++){
			 tiles = rotate90(tiles);
			 System.out.println( "The FAKE 2 tile is: "+tiles);
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

		Point avatarOffset = avatarTilePos(tiles[tile.x][tile.y]);

		tile.x = (tile.x * width);
		tile.y = (tile.y * height);
		tile = twoDToIso(tile);

		tile.x = tile.x + avatarOffset.x;
		tile.y = tile.y + avatarOffset.y;

		tile.x = panel.getWidth() - (tile.x + (panel.getWidth() / 2));
		tile.y = (panel.getHeight()/3) - (tile.y - (panel.getHeight() / 5));

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
	 * @author Leon North
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
		drawNight(g);
	}

	/**
	 * Draws a night time mask over the map depending on system time.
	 *
	 * @param g: Graphics object
	 * @author Leon North
	 */
	private void drawNight(Graphics g) {
		long millis = System.currentTimeMillis();
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
		int secondsCycle = seconds % 2;
		int minuteCycle = minutes % 2;

		Graphics2D g2d = (Graphics2D)g;

		BufferedImage img = images.get("Night");
		if (character == null){System.out.println(character == null);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if (character.getInventory() == null){System.out.println("character.getInventory() == null");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		for(int i = 0; i < character.getInventory().size(); i++){
			if(character.getInventory().get(i) instanceof Light){
				img = images.get("NightLight");
				break;
			}
		}

		//make image transparent varying to the time of day (in meatspace)
		float alpha = 0F;
		if (minuteCycle == 1){
			if (seconds == 0){alpha = (millis %1000)/1000F;}
			else{alpha = 1.0F;}
		}
		else if (minuteCycle == 0){
			if (seconds == 0){alpha = 1.0F -(millis %1000)/1000F;}
			else{alpha = 0.0F;}
		}
		int rule = AlphaComposite.SRC_OVER;
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(rule, alpha);
		g2d.setComposite(ac);
		g2d.drawImage(img,0,0,(int)panel.getWidth(), (int)panel.getHeight(), null);
		g2d.setComposite(java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}

	/**
	 * Rotates the given 2d array 90 degrees
	 *
	 * @param Tile2D [][] tiles
	 * @return Tile2D[][] newTiles
	 * @author Leon North
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
	 * @author Leon North
	 */
	private void drawTile(Point pt, Tile2D tile, Graphics g) {

		int tileNum = 0;
		if (rotated90){
			tileNum = 1;
		}

		String tileName = tile.getClass().getName();
		//remove the "gamelogic."
		tileName = tileName.substring(10);

		if(tile instanceof Floor){
			return;
		}
		drawObject(g, pt, images.get(tileName+tileNum));

	}

	/**
	 * Generic drawing method that gets called to draw Tile2D, GameCharacter,
	 * Item. It will draw the image given to it at the point given to the graphics given.
	 *
	 * @param Graphics g
	 * @param Point pt
	 * @param java.net.URL imageURL
	 * @author Leon North
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
	 * @author Leon North
	 */
	private void drawCharacter(Graphics g, Point pt, Tile2D tile) {
		if (tile.getAvatar() == null) return;

		Avatar avatar = tile.getAvatar();

		Point avatarOffset = avatarTilePos(tile);

		pt.y-=(height/2);
		pt.x+=avatarOffset.x;
		pt.y+=avatarOffset.y;

		//cases:
		//avatar is ai and charging
		//avatar is ai
		//avatar is other player and charging
		//avatar is other player
		//avatar is current player and charging
		//avatar is current player
		if (avatar.getPlayerName().startsWith("ai") && avatar.getCell().isCharging()){ //AI && charging
			drawObject(g,pt,images.get("AvatarB"+avatar.getFacing().toString()+"Charging"+avatar.getSpriteIndex()));
		}
		else if (avatar.getPlayerName().startsWith("ai")){ //AI
			drawObject(g,pt,images.get("AvatarB"+avatar.getFacing().toString()+""+avatar.getSpriteIndex()));
		}
		else if(avatar.equals(character) && avatar.getCell().isCharging()){ //Avatar = current player and is charging
			drawObject(g,pt,images.get("AvatarA"+avatar.getFacing().toString()+"Charging"+avatar.getSpriteIndex()));
		}
		else if(avatar.equals(character) && avatar.getCell().isCharging()){ //Avatar = current player and NOT charging
			drawObject(g,pt,images.get("AvatarA"+avatar.getFacing().toString()+""+avatar.getSpriteIndex()));
		}
		else if(avatar.getCell().isCharging()){ //Avatar != current player and charging
			int avatarFacing = Direction.get(avatar.getFacing().toString());
			int otherRenderingDirection = Direction.get(avatar.getRenderDirection());
			int myRenderingDirection = Direction.get(direction);
			if(myRenderingDirection == 1 ||myRenderingDirection == 3){
				myRenderingDirection = (myRenderingDirection+2)%4;
			}
			int combinedDirection = (otherRenderingDirection + avatarFacing + myRenderingDirection) % 4;
			String facing = Direction.get(combinedDirection);
			drawObject(g,pt,images.get("AvatarA"+facing+"Charging"+avatar.getSpriteIndex()));
		}
		else{//Avatar != current player and NOT charging
			int avatarFacing = Direction.get(avatar.getFacing().toString());
			int otherRenderingDirection = Direction.get(avatar.getRenderDirection());
			int myRenderingDirection = Direction.get(direction);
			if(myRenderingDirection == 1 ||myRenderingDirection == 3){
				myRenderingDirection = (myRenderingDirection+2)%4;
			}
			int combinedDirection = (otherRenderingDirection + avatarFacing + myRenderingDirection) % 4;
			String facing = Direction.get(combinedDirection);
			drawObject(g,pt,images.get("AvatarA"+facing+""+avatar.getSpriteIndex()));
		}

		//either draw a floating pointer if avatar is current player
		//or draw the name above the avatar
		if (tile.getAvatar().equals(character))
			floatingPointer.reDraw(g, pt, width, height, offset);
		else{
			g.setColor(Color.BLUE);
			g.drawString(avatar.getPlayerName(), pt.x+offset.x-(width/2), pt.y+offset.y-(height*2));
		}
	}

	/**
	 * Returns the point within the tile that the avatar is standing
	 * @param tile: The tile the avatar is standing on
	 * @return Point: position the avatar is standing on within the tile
	 * @author Leon North
	 */
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
	 * @author Leon North
	 */
	private void drawItems(Graphics g, Point pt, Tile2D tile) {
		if (tile.getItems() == null) return;

		int tileNum = 0;
		if (rotated90){
			tileNum = 1;
		}

		for (int i = 0; i < tile.getItems().size(); i++){
			String itemName = tile.getItems().get(i).getClass().getName().substring(10);
			if (images.get(itemName+tileNum) == null){
				System.out.println(itemName);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			drawObject(g,pt,images.get(itemName+tileNum));
		}

	}

	/**
	 * converts the coordinates to isometric
	 *
	 * @param Point point
	 * @return Point tempPt
	 * @author Leon North
	 */
	private Point twoDToIso(Point point) {
		Point tempPt = new Point(0, 0);
		tempPt.x = point.x - point.y;
		tempPt.y = (point.x + point.y) / 2;
		return (tempPt);
	}
}
