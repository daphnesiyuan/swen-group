package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class StartMenu {

	private DrawingPanel panel;
	private ArrayList<Image> buttons = new ArrayList<Image>();

	private int buttonWidth = 205;
	private int buttonHeight = 85;

	public StartMenu(DrawingPanel dp) {
		panel = dp;

		try {
			loadButtonImages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void redraw(Graphics g) {

		System.out.println("drawing..");

		for (int i = 0; i < buttons.size(); i++) {
			drawButton(g, i, buttons.get(i));
		}

	}

	public void drawButton(Graphics g, int i, Image b){
		g.drawImage(b, panel.getWidth()/2 - (buttonWidth/2), panel.getHeight()/3 + i*(panel.getHeight()/3) , null);
	}


	public void loadButtonImages() throws IOException {
		/*
		 * Image start = new
		 * Image(WindowFrame.class.getResource("startMenuImages/start.png"));
		 * Image load = new
		 * ImageIcon(WindowFrame.class.getResource("startMenuImages/load.png"));
		 * Image help = new
		 * ImageIcon(WindowFrame.class.getResource("startMenuImages/help.png"));
		 */

		System.out.println("images being loaded from StartMenu");

		java.net.URL startURL = WindowFrame.class.getResource("startMenuImages/start.png");
		java.net.URL loadURL = WindowFrame.class.getResource("startMenuImages/load.png");
		java.net.URL helpURL = WindowFrame.class.getResource("startMenuImages/help.png");

		Image start = null;
		Image load = null;
		Image help = null;

		try {
			start = ImageIO.read(startURL);
			load = ImageIO.read(loadURL);
			help = ImageIO.read(helpURL);
			} catch (IOException e) { e.printStackTrace(); }

		buttons.add(start);
		buttons.add(load);
		buttons.add(help);

		/*
		 * java.net.URL imageURL = Rendering.class.getResource(tileName+".png");
		 *
		 * Image img = null; try { img = ImageIO.read(imageURL); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */

	}

}
