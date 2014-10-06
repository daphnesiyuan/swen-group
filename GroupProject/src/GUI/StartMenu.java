package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class StartMenu {

	private DrawingPanel panel;
	private ArrayList<Image> buttons = new ArrayList<Image>(); //array of buttons appeared
	private ArrayList<Image> buttonsDEF = new ArrayList<Image>(); //array of un hovered images
	private ArrayList<Image> buttonsHOV = new ArrayList<Image>(); //array of hover images

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
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

		for (int i = 0; i < buttons.size(); i++) {
			drawButton(g, i, buttons.get(i));
		}

	}

	public int getButtonWidth(){
		return buttonWidth;
	}

	public int getButtonHeight(){
		return buttonHeight;
	}


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


	public void loadButtonImages() throws IOException {

		System.out.println("images being loaded from StartMenu");

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

		System.out.println("===================== AT HOVER BUTTON METHOD: "+button);

		if(button.equals("start")){
			System.out.println("start button hovered");
			buttons.set(0, buttonsHOV.get(0));
			panel.repaint();
		}
		else if(button.equals("join")){
			buttons.set(1, buttonsHOV.get(1));
			panel.repaint();
		}
		else if(button.equals("load")){
			System.out.println("sload button hovered");
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
	 */
	public void resetUnHoverButton(String button){
		if(button.equals("start")){
			System.out.println("start button un-hovered");
			buttons.set(0, buttonsDEF.get(0));
			panel.repaint();
		}
		else if(button.equals("join")){
			System.out.println("join button un-hovered");
			buttons.set(1, buttonsDEF.get(1));
			panel.repaint();
		}
		else if(button.equals("load")){
			System.out.println("load button un-hovered");
			buttons.set(2, buttonsDEF.get(2));
			panel.repaint();
		}
		else if(button.equals("help")){
			System.out.println("help button un-hovered");
			buttons.set(3, buttonsDEF.get(3));
			panel.repaint();
		}
	}

}
