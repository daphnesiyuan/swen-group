package rendering;

import gameLogic.Avatar;
import gameLogic.Floor;
import gameLogic.Light;
import gameLogic.Room;
import gameLogic.Tile2D;

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
		drawNight(g);
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
	}

	//-----------------------Drawing things in the location
	//-----------------------Everything goes to drawObject() to actually draw

	/**
	 * Takes the name of the class and gets drawObject(...) to draw it.
	 *
	 * @param Point pt
	 * @param String tileName
	 * @param Graphics g
	 * @author Leon North
	 */
	private void drawTile(Point pt, Tile2D tile, Graphics g) {

		//pick the animation number
		int tileNum = 0;
		if (rotated90){
			tileNum = 1;
		}

		String tileName = tile.getClass().getName();
		//remove the "gamelogic." at the start of the name.
		tileName = tileName.substring(10);

		//We are not drawing floor tiles, it was a design choice.
		if(tile instanceof Floor){
			return;
		}

		//send the image and point off to get drawn
		drawObject(g, pt, images.get(tileName+tileNum));
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
			String facing = otherAvatarFacing(avatar);
			drawObject(g,pt,images.get("AvatarA"+facing+"Charging"+avatar.getSpriteIndex()));
		}
		else{//Avatar != current player and NOT charging
			String facing = otherAvatarFacing(avatar);
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
	 * This calculates the correct facing direction of other avatars relative to the direction
	 * the current player is facing.
	 * @param avatar : other avatar
	 * @return string : the direction that the avatar is facing
	 */
	private String otherAvatarFacing(Avatar avatar) {
		int avatarFacing = Direction.get(avatar.getFacing().toString());          //gets the direction that the avatar is facing
																				  //relative to the direction they are viewing the world

		int otherRenderingDirection = Direction.get(avatar.getRenderDirection()); //the rendering direction of the other avatar

		int myRenderingDirection = Direction.get(direction);                     // the direction the current avatar is facing  relative
																			     //to the rendering direction

		if(myRenderingDirection == 1 ||myRenderingDirection == 3){               // the current players rendering direction (it was buggy
			myRenderingDirection = (myRenderingDirection+2)%4;                   // so I added code to plug the hole).
		}

		int combinedDirection = (otherRenderingDirection + avatarFacing + myRenderingDirection) % 4;  //calculation to get other avatars facing direction
																									  //relative to the current avatars rendering direction
		String facing = Direction.get(combinedDirection);
		return facing;
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

		//pick the animation number
		int tileNum = 0;
		if (rotated90){
			tileNum = 1;
		}

		//for each item in the inventory, get the corresponding image and send it to drawObject() to draw it
		for (int i = 0; i < tile.getItems().size(); i++){
			String itemName = tile.getItems().get(i).getClass().getName().substring(10);
			drawObject(g,pt,images.get(itemName+tileNum));
		}
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

		//check if the avatar is carying a light, if they are, use a lighter image as a night mask
		for(int i = 0; i < character.getInventory().size(); i++){
			if(character.getInventory().get(i) instanceof Light){
				img = images.get("NightLight");
				break;
			}
		}

		//make image transparent varying to the time of day (in meatspace).
		//1 second transition to night, night for 59 seconds, 1 second
		//transition to day, day for 59 seconds, rinse repeat.
		//pick the alpha depending on the previous rule.
		float alpha = 0F;
		if (minuteCycle == 1){
			if (seconds == 0){alpha = (millis %1000)/1000F;}
			else{alpha = 1.0F;}
		}
		else if (minuteCycle == 0){
			if (seconds == 0){alpha = 1.0F -(millis %1000)/1000F;}
			else{alpha = 0.0F;}
		}

		//set an alpha composite on the buffered image to make it transparent.
		int rule = AlphaComposite.SRC_OVER;
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(rule, alpha);
		g2d.setComposite(ac);
		g2d.drawImage(img,0,0,(int)panel.getWidth(), (int)panel.getHeight(), null);
		g2d.setComposite(java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}

	//-----------------------------------Utilities

	/**
	 * Returns the point within the tile that the avatar is standing
	 * @param tile: The tile the avatar is standing on
	 * @return Point: position the avatar is standing on within the tile
	 * @author Leon North
	 */
	public Point avatarTilePos(Tile2D tile){

		double stepSize = width/100.0; // 100 is the number of positions across a tile.
		Avatar avatar = tile.getAvatar();

		Point avatarPoint = new Point((int)avatar.getTileXPos(), (int)avatar.getTileYPos());

		//rotate the ppoint around to the same direction the location is
		for(int i = 0; i < Direction.get(direction)*3; i++){
			avatarPoint = new Point((100-avatarPoint.y),(avatarPoint.x));
	    }

		//convert point to isometric view
		avatarPoint = twoDToIso(avatarPoint);

		//scale the change to fit with the drawn tile
		avatarPoint.x = (int)(avatarPoint.x * stepSize);
		avatarPoint.y = (int)(avatarPoint.y * stepSize);

		return avatarPoint;
	}

	/**
	 * converts the coordinates to isometric
	 *
	 * @param Point point
	 * @return Point tempPt
	 * @author Leon North
	 */
	private Point twoDToIso(Point point) {
		//rotates points by -45 degrees, squeezes the y to 50%
		Point tempPt = new Point(0, 0);
		tempPt.x = point.x - point.y;
		tempPt.y = (point.x + point.y) / 2;
		return (tempPt);
	}

	/**
	 * Rotates the given 2d array 90 degrees
	 *
	 * @param Tile2D [][] tiles
	 * @return Tile2D[][] newTiles
	 * @author Leon North
	 */
	private Tile2D[][] rotate90(Tile2D[][] tiles) {

		//makes a new 2d array, takes each object and puts it into the corresponding
		//position in the new array rotated by 90 degrees
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
	 * Makes the offset that everything needs to be drawn by to put the current
	 * players avatar in the centre of the screen.
	 *
	 * @param direction
	 * @param room
	 * @author Leon North
	 */
	private void calibrateOffset(String direction, Room room) {

		Point tile = null;

		//copy the tiles
		Tile2D[][] tiles= room.getTiles().clone();

		//rotate the copied tiles to the correct direction we are facing
		for (int i = 0; i < Direction.get(direction)+3; i++){//the 3 is to solve a bug that was not resolved but plugged
			 tiles = rotate90(tiles);
		}

		//travers the 2d array to find the tile our avatar is in.
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles.length; j++){
				if(tiles[j][i].getAvatar() != null && tiles[j][i].getAvatar().equals(character)){
					tile = new Point(j,i);
					break;
				}
			}
		}

		//find the offset for where in the tile the avatar is
		Point avatarOffset = avatarTilePos(tiles[tile.x][tile.y]);

		//scale the offset by the panel width
		tile.x = (tile.x * width);
		tile.y = (tile.y * height);

		//convert the point from 2d to isometric
		tile = twoDToIso(tile);

		//add the avatar offset
		tile.x = tile.x + avatarOffset.x;
		tile.y = tile.y + avatarOffset.y;

		//center the offset
		tile.x = panel.getWidth() - (tile.x + (panel.getWidth() / 2));
		tile.y = (panel.getHeight()/3) - (tile.y - (panel.getHeight() / 5));

		offset = tile;
	}
}
