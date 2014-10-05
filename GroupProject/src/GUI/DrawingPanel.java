package GUI;

import gameLogic.Item;
import gameLogic.Room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import networking.ChatMessage;
import networking.GameClient;
import networking.GameServer;
import networking.Move;
import rendering.Direction;
import rendering.DrawChat;
import rendering.DrawCompass;
import rendering.DrawInventory;
import rendering.DrawMiniMap;
import rendering.DrawWorld;

public class DrawingPanel extends JPanel  {

	private DrawWorld dw; //this draws all the game-stuff: locations chars items etc
	private StartMenu sm;
	private WindowFrame wf;
	public static boolean startMenu; //play or menu mode flag

	private MyMouseListener mouse;
	private int mouseX;
	private int mouseY;
	private Handler handler;
	private KeyBoard keyboard;
	private MouseMotion mouseMotion;

	//Leons fields
	List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	String currentMessage = "";
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

	private GameServer gs;
	private GameClient gc;

	private String direction;
	private int directionI;
	private DrawCompass compass;
	private DrawInventory invo;
	private DrawMiniMap map;

	//leon added
	private DrawChat chat;


	private boolean chatMode; //from rendering


	public DrawingPanel(WindowFrame win){
		wf = win;
		sm = new StartMenu( this );
		startMenu = true; //by default
		handler = new Handler();

		direction = "North"; //hard coded now...NEED TO CHANGE

		//set up mouse and key board stuff
		setUpMouseKeys();

		//leon added:
		chat = new DrawChat(this);

		//networking setup stuff
		gs = new GameServer();
		setUpNWEN();
	}


	////////////////getters and setters

	public KeyBoard getKeyB(){
		return keyboard;
	}

	public boolean isChatMode(){
		return chatMode;
	}

	public void setChatMode(boolean b){
		chatMode = b;
	}

	public void addToCurrentMessage(String s){
		currentMessage+=s;
	}

	public void setCurrentMessage(String s){
		currentMessage = s;
	}

	public String getCurrentMessage(){
		return currentMessage;
	}

	public int getDirection(){
		return directionI;
	}

	public void setDirection(int d){
		directionI = d;
	}

	public GameClient getGameClient(){
		return gc;
	}

	public ArrayList<Integer> getKeysDown(){
		return keysDown;
	}

	////////////////////////////////////

	@Override
	protected void paintComponent (Graphics g){

		if( startMenu ){
			System.out.println("start menu mode, ready to draw");
			sm.redraw(g);
		}

		else{ //else it is in game
			dw.redraw(g, gc.getRoom(), Direction.get(directionI)); //param: graphics, room, char, direction
			//potential changes later: flag for menu mode or play mode, and to have logic
			compass.redraw(g, Direction.get(directionI));
			invo.redraw(g, gc.getAvatar().getInventory()  , Direction.get(directionI));
			map.redraw(g, gc.getRoom() , Direction.get(directionI));
			System.out.println("in game");
			if(chatMode)chat.redraw(g, gc.getChatHistory(10), currentMessage);
		}
	}


	public void setUpMouseKeys(){
		keyboard = new KeyBoard(this);
		mouse = new MyMouseListener(this);
		this.addMouseListener( mouse );
		mouseMotion = new MouseMotion(this);
		this.addMouseMotionListener(mouseMotion);
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
	 * A helper method which takes cordinates and finds the button that match those
	 * If no matching button is found on the mouse click then it will return an empty string
	 * @param x: the x coordinate of the click
	 * @param y: the y coordinate of the click
	 * @return: the string name associated with the appropriate button
	 */
	public String findButton(int x, int y){

		int startW = (getWidth()/2 - (sm.getButtonWidth()/2));
		int startH1 = getHeight()/3 - sm.getButtonHeight()/2 + (-1*(getHeight()/3)/2);
		int startH2 = getHeight()/3 - sm.getButtonHeight()/2 + (0*(getHeight()/3)/2);
		int startH3 = getHeight()/3 - sm.getButtonHeight()/2 + (1*(getHeight()/3)/2);
		int startH4 = getHeight()/3 - sm.getButtonHeight()/2 + (2*(getHeight()/3)/2);

		//panel.getWidth()/2 - (buttonWidth/2),
		//panel.getHeight()/3 - buttonHeight/2 + (i*(panel.getHeight()/3)/2)

		//System.out.println("x="+startW + " y="+startH);
		if ( x>=startW && x<=startW+sm.getButtonWidth() && y>startH1 && y<startH1+sm.getButtonHeight() ){
			return "start";
		}
		else if ( x>=startW && x<=startW+sm.getButtonWidth() && y>startH2 && y<startH2+sm.getButtonHeight() ){
			return "join";
		}
		else if ( x>=startW && x<=startW+sm.getButtonWidth() && y>startH3 && y<startH3+sm.getButtonHeight() ){
			return "load";
		}
		else if ( x>=startW && x<=startW+sm.getButtonWidth() && y>startH4 && y<startH4+sm.getButtonHeight() ){
			return "help";
		}

		return "";
	}

	public void setUpNWEN(){
		gc = new GameClient("Daphne", this);

		try {
			//gc.connect("130.195.6.69",32768); //jimmy
			gc.connect(gs, gc.getName()); //your own server
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
	}


	/*
	 * Private inner class to handle action listeners - dealing with buttons
	 */

	private class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){

		}

		public void mouseListener(){
			if(startMenu){
				if ( findButton( mouseX, mouseY ).equals("start") ){
					System.out.println("clicked start button");
					startMenu = false; //no longer in the start menu mode
					dw = new DrawWorld( gc.getAvatar() ,DrawingPanel.this ); //param: the character, and then a panel
					compass = new DrawCompass( DrawingPanel.this );
					invo = new DrawInventory( DrawingPanel.this );
					map = new DrawMiniMap( DrawingPanel.this, gc.getAvatar() );
					repaint();
				}


				else if ( findButton( mouseX, mouseY ).equals("join") ){
					System.out.println("PRESSED JOIN BUTTON");
				}

				else if ( findButton( mouseX, mouseY ).equals("load") ){
					System.out.println("PRESSED LOAD BUTTON");
				}
				else if ( findButton( mouseX, mouseY ).equals("help") ){
					System.out.println("PRESSED HELP BUTTON");
				}

				else{
					System.out.println("no active button");
				}
			}

			else{ //not in start menu and in game.

				System.out.println("clicked mouse at x="+mouseX+" y="+mouseY);

			}
		}

	}


}
