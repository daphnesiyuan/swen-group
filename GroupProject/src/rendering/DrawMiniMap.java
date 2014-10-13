package rendering;

import gameLogic.Avatar;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Room;
import gameLogic.Tile2D;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Draws a minimap on the panel
 *
 * @author Leon North
 *
 */
public class DrawMiniMap {

	private JPanel panel;
	private Avatar charac;
	private double width;
	private double height;
	private double buffer;
	private double cellHeight;
	private double cellWidth;
	private static final double STARTWIDTH = 1280;
	private static final double STARTMAPHEIGHT = 135;
	private static final int NUMCARDS = 5;

	public DrawMiniMap(JPanel panel, Avatar charac) {
		this.panel = panel;
		this.charac = charac;

	}

	/**
	 * Draws a mini map on the panel object.
	 * @param g
	 * @param room
	 * @param direction
	 */
	public void redraw(Graphics g, Room room, String direction){
		width = ((panel.getWidth() / STARTWIDTH) * STARTMAPHEIGHT);
		height = width;
		buffer = (width / STARTMAPHEIGHT);
		int x = (int) width * NUMCARDS;
		int y = (int)(panel.getHeight() - height - (buffer * NUMCARDS));

		cellHeight = (height / room.getTiles()[0].length);
		cellWidth = (width / room.getTiles().length);

		//clone the tiles so we don't modify the game logics tiles
		Tile2D[][] tiles = room.getTiles().clone();

		//rotate the tiles to up is facing the top right of the screen
		for (int i = 0; i < Direction.get(direction)+3; i++){
			tiles = rotate90(tiles);
		}

		//for each tile, set the color and draw it.
		for (int i = 0; i < tiles.length; i++ ){
			for(int j = 0; j < tiles[i].length; j++){
				g.setColor(chooseColor(tiles[i][j]));
				g.fillRect((int)(x+(i*cellWidth)), (int)(y+(j*cellHeight)), (int)(cellWidth-buffer), (int)(cellHeight-buffer));
			}
		}
	}

	/**
	 * Returns a color depending on the tile that it is given.
	 * Doors get a special color.
	 * Avatars are Red, solid red if they are the player too.
	 * Floor tiles are drawn pale transparent.
	 * Other tiles are a darker transparent.
	 * @param tile
	 * @return
	 */
	private Color chooseColor(Tile2D tile) {
		Color color = null;

		//Avatars are normally only on a floor tile.
		//If the avatar = the players avater, draw it solid red,
		//otherwise draw it transparent red.
		if (tile instanceof Floor) {
			if (tile.getAvatar() != null) {
				if (tile.getAvatar().equals(charac)) {
					color = new Color(1.0f, 0.0f, 0.0f);
				} else {
					color = new Color(0.5f, 0.1f, 0.1f, 0.5f);
				}
			}
			//if the floor tile has no avatar just draw a normal color
			else {
				color = new Color(0.5f, 0.5f, 0.5f, 0.5f);
			}
		}
		//doors get there own special color
		else if (tile instanceof Door){
			color = new Color(0.0f, 0.0f, 1.0f, 0.5f);
		}
		//other obstacles such as columns, trees, walls get drawn
		//the same darker color
		else{
			color = new Color(0.1f, 0.2f, 0.1f, 0.5f);
		}
		return color;
	}

	/**
	 * Takes a 2d array and returns a new 2d array rotated 90 degrees left.
	 * @param tiles: 2d array of tile2d objects
	 * @return 2d array of tile2d objects
	 * @author Leon North
	 */
	private Tile2D[][] rotate90(Tile2D[][] tiles) {
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

}
