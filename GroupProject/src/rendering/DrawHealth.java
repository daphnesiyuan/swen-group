package rendering;

import gameLogic.Avatar;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawHealth {

	JPanel panel;
	double width;
	double height;
	double buffer;
	double cellHeight;
	double cellWidth;
	public static final double STARTWIDTH = 1280;
	public static final double STARTSCOREHEIGHT = 135;
	public static final int NUMCARDS = 7;

	public DrawHealth(JPanel panel) {
		this.panel = panel;
	}

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
