package GUI;

import gameLogic.Item;
import gameLogic.Room;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JPanel;

import networking.GameClient;
import rendering.DrawCompass;
import rendering.DrawInventory;
import rendering.DrawMiniMap;
import rendering.DrawWorld;

public class DrawingPanel extends JPanel{

	private DrawWorld dw; //this draws all the game-stuff: locations chars items etc
	private StartMenu sm;
	private WindowFrame wf;
	public static boolean startMenu; //play or menu mode flag

	private MyMouseListener mouse;
	private int mouseX;
	private int mouseY;
	private Handler handler;



	private String direction;
	private DrawCompass compass;
	private DrawInventory invo;
	private DrawMiniMap map;


	public DrawingPanel(WindowFrame win){
		wf = win;
		sm = new StartMenu( this );
		startMenu = true; //by default
		handler = new Handler();

		direction = "North"; //hard coded now...NEED TO CHANGE
		mouse = new MyMouseListener(this);
		this.addMouseListener( mouse );

		new ClientTest();
	}

	@Override
	protected void paintComponent (Graphics g){

		if( startMenu ){
			System.out.println("start menu mode, ready to draw");
			sm.redraw(g);
		}

		else{ //else it is in game
			dw.redraw(g, ClientTest.gc.getRoom(), direction); //param: graphics, room, char, direction
			//potential changes later: flag for menu mode or play mode, and to have logic
			compass.redraw(g, direction);
			invo.redraw(g, ClientTest.gc.getAvatar().getInventory()  , direction);
			map.redraw(g, ClientTest.gc.getRoom() , direction);
			System.out.println("in game");
		}
	}





	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1280, 720);
		return dimension;
	}

	public void sendClick(int x, int y){
		mouseX = x;
		mouseY = y;
		handler.mouseListener();
	}


	/**
	 * A helper class which takes cordinates and finds the button that match those
	 * If no matching button is found on the mouse click then it will return an empty string
	 * @param x: the x coordinate of the click
	 * @param y: the y coordinate of the click
	 * @return: the string name associated with the appropriate button
	 */
	public String findButton(int x, int y){

		//panel.getWidth()/2 - (buttonWidth/2), panel.getHeight()/3 - buttonHeight/2 + (i*(panel.getHeight()/3)/2)
		int startW = (getWidth()/2 - (sm.getButtonWidth()/2));
		int startH = getHeight()/3 - sm.getButtonHeight()/2 + (-1*(getHeight()/3)/2);

		//System.out.println("x="+startW + " y="+startH);
		if ( x>=startW && x<=startW+sm.getButtonWidth() && y>startH && y<startH+sm.getButtonHeight() ){
			return "start";
		}

		return "";
	}


	/*
	 * Private inner class to handle action listeners - dealing with buttons
	 */

	private class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){

		}

		public void mouseListener(){
			if ( findButton( mouseX, mouseY ).equals("start") ){
				System.out.println("clicked start button");
				startMenu = false; //no longer in the start menu mode
				dw = new DrawWorld( ClientTest.gc.getAvatar() ,DrawingPanel.this ); //param: the character, and then a panel
				compass = new DrawCompass( DrawingPanel.this );
				invo = new DrawInventory( DrawingPanel.this );
				map = new DrawMiniMap( DrawingPanel.this, ClientTest.gc.getAvatar() );
				repaint();
			}

			else{
				System.out.println("no active button");
			}
		}

	}

	private static class ClientTest {

		static GameClient gc = new GameClient("Daphne");

		public ClientTest(){

			try {
				gc.connect("130.195.7.84",32768);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Room room = gc.getRoom();
			while(room == null){
				room = gc.getRoom();
				System.out.println(room);
				System.out.println(gc.isConnected());
			}

			//gc.setName(name);

		}

	}
}
