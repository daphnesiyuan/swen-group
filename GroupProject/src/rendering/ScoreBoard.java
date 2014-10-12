package rendering;

import gameLogic.Avatar;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class ScoreBoard {

	JPanel panel;
	double width;
	double height;
	double buffer;
	double cellHeight;
	double cellWidth;
	public static final double STARTWIDTH = 1280;
	public static final double STARTSCOREHEIGHT = 135;
	public static final int NUMCARDS = 6;

	public ScoreBoard(JPanel panel) {
		this.panel = panel;
	}

	public void redraw(Graphics g, List<Avatar> avatars){

		width = ((panel.getWidth() / STARTWIDTH) * STARTSCOREHEIGHT);
		height = width;
		buffer = (width / STARTSCOREHEIGHT) * 10;
		int x = (int) width * NUMCARDS + (int)(buffer*2);
		int y = (int)(panel.getHeight() - height - (buffer/2));

		g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
		g.fillRoundRect(x, y, (int)width, (int)height, (int)buffer, (int)buffer);

		g.setColor(Color.BLACK);
		if (avatars != null){
			for (int i = 0; i < avatars.size(); i++){
				//String score = avatars.get(i).getPlayerName()+": "+avatars.get(i).getScore();
				//g.drawString(score, x, y);
			}
		}
	}
}
