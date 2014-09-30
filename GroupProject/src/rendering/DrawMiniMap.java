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
		g.fillRect(x, y, (int)width, (int)height);
		cellHeight = (height / room.getTiles()[0].length);
		cellWidth = (width / room.getTiles().length);
		Tile2D[][] tiles = room.getTiles().clone();
		for (int i = 0; i < tiles.length; i++ ){
			for(int j = 0; j < tiles[i].length; j++){
				if (tiles[j][i] instanceof Floor) {
					if (tiles[j][i].getAvatar() != null) {
						if (tiles[j][i].getAvatar().equals(charac)) {
							g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.5f));
						} else {
							g.setColor(new Color(0.5f, 0.1f, 0.1f, 0.5f));
						}
						// g.drawRect(x, y, (int)width, (int)height);

					} else {
						g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
					}
				}
				else if (tiles[j][i] instanceof Wall){
					g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.5f));
				}
				else if (tiles[j][i] instanceof Door){
					g.setColor(new Color(0.0f, 1.0f, 0.0f, 0.5f));
				}
				g.fillRect((int)(x+j*cellWidth), (int)(y+i*cellHeight), (int)(cellWidth), (int)(cellHeight));
			}
		}
	}

}
