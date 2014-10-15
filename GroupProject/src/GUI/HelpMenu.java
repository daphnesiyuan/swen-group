package GUI;

import gameLogic.Light;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import rendering.MakeImageMap;

public class HelpMenu {

	private BufferedImage img=null;
	private DrawingPanel panel;
	private boolean helpMode=false;

	/**
	 * This class takes care of the help screen which is activated with pressing the esc key or clicking help at the start of the game
	 * This class is aware of whether it is currently being used or not with a boolean
	 * @param p DrawingPanel
	 *
	 * @author Daphne Wang
	 */

	public HelpMenu(DrawingPanel p){
		panel = p;
		java.net.URL imageURL = WindowFrame.class.getResource("helpImage/help menu.png");

		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Identifies whether the game state is on the help screen or not
	 * @return
	 */

	public boolean isHelpMode(){
		return helpMode;
	}

	public void helpOff(){
		helpMode=false;
	}

	public void helpOn(){
		helpMode=true;
	}

	/**
	 * Drawing method similar leon's draw world class: DrawNight();
	 * @param g Graphics object for drawing things
	 */
	public void drawHelp(Graphics g){

		Graphics2D g2d = (Graphics2D)g;

		float alpha = 0.8F;

		int rule = AlphaComposite.SRC_OVER;
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(rule, alpha);
		g2d.setComposite(ac);
		g2d.drawImage(img,0,0,(int)panel.getWidth(), (int)panel.getHeight(), null);
		g2d.setComposite(java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
}
