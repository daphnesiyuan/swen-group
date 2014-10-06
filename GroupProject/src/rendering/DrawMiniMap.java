package rendering;

import gameLogic.Avatar;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Wall;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 *
 * @author northleon
 *
 */
public class DrawMiniMap {

	JPanel panel;
	Avatar charac;
	double width;
	double height;
	double buffer;
	double cellHeight;
	double cellWidth;

	public DrawMiniMap(JPanel panel, Avatar charac) {
		this.panel = panel;
		this.charac = charac;

	}

	public void redraw(Graphics g, Room room, String direction){
		width = ((panel.getWidth() / 1280.0) * 135.0);
		height = width;
		buffer = (width / 135.0)*2;
		int x = (int) width*5;
		int y = (int)(panel.getHeight() - height - (buffer * 5));
		g.setColor(new Color(0.5f,0.5f,0.5f,0.5f));
		//g.fillRect(x, y, (int)width, (int)height);
		cellHeight = (height / room.getTiles()[0].length);
		cellWidth = (width / room.getTiles().length);
		Tile2D[][] tiles = room.getTiles().clone();
		for (int i = 0; i < Direction.get(direction)+3; i++){
			tiles = rotate90(tiles);
		}
		for (int i = 0; i < tiles.length; i++ ){
			for(int j = 0; j < tiles[i].length; j++){
				if (tiles[i][j] instanceof Floor) {
					if (tiles[i][j].getAvatar() != null) {
						if (tiles[i][j].getAvatar().equals(charac)) {
							g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.5f));
						} else {
							g.setColor(new Color(0.5f, 0.1f, 0.1f, 0.5f));
						}
					}
					else {
						g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
					}
				}
				else if (tiles[i][j] instanceof Wall){
					g.setColor(new Color(0.1f, 0.2f, 0.1f, 0.5f));
				}
				else if (tiles[i][j] instanceof Door){
					g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.5f));
				}
				g.fillRect((int)(x+(i*cellWidth)), (int)(y+(j*cellHeight)), (int)(cellWidth-buffer), (int)(cellHeight-buffer));
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

}
