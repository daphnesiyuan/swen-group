package GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Class which is the main start screen when opening the game. It contains all the basic
 * buttons and enables you to start, join, load or view help options.
 * Also runs the splash art too.
 * @author wangdaph
 */
public class StartMenu {

	private DrawingPanel panel;
	private ArrayList<Image> buttons = new ArrayList<Image>(); //array of buttons appeared
	private ArrayList<Image> buttonsDEF = new ArrayList<Image>(); //array of un hovered images
	private ArrayList<Image> buttonsHOV = new ArrayList<Image>(); //array of hover images

	private BufferedImage splash;

	private int buttonWidth = 205;
	private int buttonHeight = 85;

	public StartMenu(DrawingPanel dp) {
		panel = dp;

		try {

			loadImages();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * A redraw method for the start menu: draws a white background and draws the buttons
	 * @param g Graphics object
	 * @author Daphne Wang
	 */
	public void redraw(Graphics g) {
		g.setColor(Color.WHITE);

		drawSplashArt(g);

		for (int i = 0; i < buttons.size(); i++) {
			drawButton(g, i, buttons.get(i));
		}

	}

	/**
	 * A helper method which loads the background art to support the style of the game
	 * @param g
	 *
	 * @author Daphne Wang
	 */
	public void drawSplashArt(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		float alpha = 1F;

		int rule = AlphaComposite.SRC_OVER;
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(rule, alpha);
		g2d.setComposite(ac);

		g2d.drawImage(splash,0,0,(int)panel.getWidth(), (int)panel.getHeight(), null);
		g2d.setComposite(java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}

	public int getButtonWidth(){
		return buttonWidth;
	}

	public int getButtonHeight(){
		return buttonHeight;
	}


	/**
	 * A helper method which draws a single button onto the graphics object.
	 * contains some arithmetic which deals with the i'th button and where it should be positioned relative to the panel
	 * the size of the panel doesn't matter as the position drawn will scale with proportion
	 * @param g: the Graphics object
	 * @param i: i'th button being drawn
	 * @param b: the image of the actual button
	 *
	 * @author Daphne Wang
	 */
	public void drawButton(Graphics g, int i, Image b){
		i--;
		if(i==-1){ //add a spacer vertically if it is the first button
			g.drawImage(b, panel.getWidth()/2 - (buttonWidth/2),
					panel.getHeight()/3 - buttonHeight/2 + (i*(panel.getHeight()/3)/2) , null);
		}
		else{
			//g.drawImage(b, panel.getWidth()/2 - (buttonWidth/2), panel.getHeight()/3 + i*(panel.getHeight()/3) , null);
			g.drawImage(b, panel.getWidth()/2 - (buttonWidth/2),
					panel.getHeight()/3 - buttonHeight/2 + (i*(panel.getHeight()/3)/2) , null);
		}
	}


	/**
	 * Helper buttons for loading image resources, and populating the 3 arraylists: default buttons, hover buttons, and buttons appeared
	 * @throws IOException
	 *
	 * @author Daphne Wang
	 */
	public void loadImages() throws IOException {
		java.net.URL imageURL = WindowFrame.class.getResource("startMenuImages/splash.png");

		try {
			splash = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//default buttons
		java.net.URL startURL = WindowFrame.class.getResource("startMenuImages/start.png");
		java.net.URL joinURL = WindowFrame.class.getResource("startMenuImages/join.png");
		java.net.URL loadURL = WindowFrame.class.getResource("startMenuImages/load.png");
		java.net.URL helpURL = WindowFrame.class.getResource("startMenuImages/help.png");

		//hover buttons
		java.net.URL startHOV = WindowFrame.class.getResource("startMenuImages/start2.png");
		java.net.URL joinHOV = WindowFrame.class.getResource("startMenuImages/join2.png");
		java.net.URL loadHOV = WindowFrame.class.getResource("startMenuImages/load2.png");
		java.net.URL helpHOV = WindowFrame.class.getResource("startMenuImages/help2.png");

		Image start = null;
		Image join = null;
		Image load = null;
		Image help = null;

		Image start2=null;
		Image join2=null;
		Image load2=null;
		Image help2=null;

		try {
			start = ImageIO.read(startURL);
			join = ImageIO.read(joinURL);
			load = ImageIO.read(loadURL);
			help = ImageIO.read(helpURL);

			start2 = ImageIO.read(startHOV);
			join2 = ImageIO.read(joinHOV);
			load2 = ImageIO.read(loadHOV);
			help2 = ImageIO.read(helpHOV);

			} catch (IOException e) { e.printStackTrace(); }

		buttons.add(start);
		buttons.add(join);
		buttons.add(load);
		buttons.add(help);

		buttonsDEF.add(start);
		buttonsDEF.add(join);
		buttonsDEF.add(load);
		buttonsDEF.add(help);

		buttonsHOV.add(start2);
		buttonsHOV.add(join2);
		buttonsHOV.add(load2);
		buttonsHOV.add(help2);
	}

	/**
	 * Method to set the image of a button when mouse has hovered onto it
	 * @param button
	 */
	public void loadHoverButton(String button){

		if(button.equals("start")){
			buttons.set(0, buttonsHOV.get(0));
			panel.repaint();
		}
		else if(button.equals("join")){
			buttons.set(1, buttonsHOV.get(1));
			panel.repaint();
		}
		else if(button.equals("load")){
			buttons.set(2, buttonsHOV.get(2));
			panel.repaint();
		}
		else if(button.equals("help")){
			buttons.set(3, buttonsHOV.get(3));
			panel.repaint();
		}
	}


	/**
	 * Method to reset the image of a button after the mouse has moved away
	 * @param button
	 *
	 * @author Daphne Wang
	 */
	public void resetUnHoverButton(String button){
		if(button.equals("start")){
			buttons.set(0, buttonsDEF.get(0));
			panel.repaint();
		}
		else if(button.equals("join")){
			buttons.set(1, buttonsDEF.get(1));
			panel.repaint();
		}
		else if(button.equals("load")){
			buttons.set(2, buttonsDEF.get(2));
			panel.repaint();
		}
		else if(button.equals("help")){
			buttons.set(3, buttonsDEF.get(3));
			panel.repaint();
		}
	}


	/**
	 * A helper method which takes cordinates and finds the button that match
	 * those If no matching button is found on the mouse click then it will
	 * return an empty string
	 *
	 * @param x the x coordinate of the click
	 * @param y the y coordinate of the click
	 * @return the string name associated with the appropriate button
	 *
	 * @author Daphne Wang
	 */
	public String findButton(int x, int y) {

		int startW = (panel.getWidth() / 2 - (getButtonWidth() / 2));
		int startH1 = panel.getHeight() / 3 - getButtonHeight() / 2
				+ (-1 * (panel.getHeight() / 3) / 2);
		int startH2 = panel.getHeight() / 3 - getButtonHeight() / 2
				+ (0 * (panel.getHeight() / 3) / 2);
		int startH3 = panel.getHeight() / 3 - getButtonHeight() / 2
				+ (1 * (panel.getHeight() / 3) / 2);
		int startH4 = panel.getHeight() / 3 - getButtonHeight() / 2
				+ (2 * (panel.getHeight() / 3) / 2);

		if (x >= startW && x <= startW + getButtonWidth() && y > startH1
				&& y < startH1 + getButtonHeight()) {
			return "start";
		} else if (x >= startW && x <= startW + getButtonWidth()
				&& y > startH2 && y < startH2 + getButtonHeight()) {
			return "join";
		} else if (x >= startW && x <= startW + getButtonWidth()
				&& y > startH3 && y < startH3 + getButtonHeight()) {
			return "load";
		} else if (x >= startW && x <= startW + getButtonWidth()
				&& y > startH4 && y < startH4 + getButtonHeight()) {
			return "help";
		}

		return "";
	}

}
