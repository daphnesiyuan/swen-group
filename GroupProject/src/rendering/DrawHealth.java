package rendering;

import gameLogic.Avatar;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Draws the health on the bottom of the panel.
 *
 * @author Leon North
 *
 */
public class DrawHealth {

	private JPanel panel;
	private double width;
	private double height;
	private double buffer;
	private static final double STARTWIDTH = 1280;
	private static final double STARTSCOREHEIGHT = 135;
	private static final int NUMCARDS = 7;

	public DrawHealth(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * Draws the Health bar on the panel.
	 *
	 * @param g : Graphics object
	 * @param avatar : Avatar object, the current player.
	 */
	public void redraw(Graphics g, Avatar avatar){
		width = ((panel.getWidth() / STARTWIDTH) * STARTSCOREHEIGHT);
		height = width;

		buffer = (width / STARTSCOREHEIGHT) * 10;
		int x = (int) width * NUMCARDS + (int)(buffer*4);
		int y = (int)(panel.getHeight() - height - (buffer/2));

		width = width /2;

		g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
		g.fillRoundRect(x, y, (int)width, (int)height, (int)buffer, (int)buffer);

		g.setColor(Color.RED);
		g.drawString(avatar.getCell().getBatteryLife()+"", x+10, y+20);
	}
}
