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

public class DrawingPanel extends JPanel implements KeyListener{

	private DrawWorld dw; //this draws all the game-stuff: locations chars items etc
	private StartMenu sm;
	private WindowFrame wf;
	public static boolean startMenu; //play or menu mode flag

	private MyMouseListener mouse;
	private int mouseX;
	private int mouseY;
	private Handler handler;

	//Leons fields
	List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	String currentMessage = "";
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();


	private String direction;
	private int directionI;
	private DrawCompass compass;
	private DrawInventory invo;
	private DrawMiniMap map;

	//leon added
	private DrawChat chat;


	private boolean chatMode; //from rendering
	private KeyBoard keyboard;

	public DrawingPanel(WindowFrame win){
		wf = win;
		sm = new StartMenu( this );
		startMenu = true; //by default
		handler = new Handler();

		direction = "North"; //hard coded now...NEED TO CHANGE
		mouse = new MyMouseListener(this);
		this.addMouseListener( mouse );
		new ClientTest();
		keyboard = new KeyBoard(this);

		//leon added:
		chat = new DrawChat(this);
	}

	//from rendering
	public boolean isChatMode(){
		return chatMode;
	}

	public void setChatMode(boolean b){
		chatMode = b;
	}

	public int getDirection(){
		return directionI;
	}

	public void setDirection(int d){
		directionI = d;
	}

	@Override
	protected void paintComponent (Graphics g){

		if( startMenu ){
			System.out.println("start menu mode, ready to draw");
			sm.redraw(g);
		}

		else{ //else it is in game
			dw.redraw(g, ClientTest.gc.getRoom(), Direction.get(directionI)); //param: graphics, room, char, direction
			//potential changes later: flag for menu mode or play mode, and to have logic
			compass.redraw(g, Direction.get(directionI));
			invo.redraw(g, ClientTest.gc.getAvatar().getInventory()  , Direction.get(directionI));
			map.redraw(g, ClientTest.gc.getRoom() , Direction.get(directionI));
			System.out.println("in game");
			if(chatMode)chat.redraw(g, ClientTest.gc.getChatHistory(10), currentMessage);
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
		static GameServer gs = new GameServer();
		static GameClient gc = new GameClient("Daphne");



		public ClientTest(){


			try {
				gc.connect("130.195.6.69",32768);
				//gc.connect(gs, gc.getName());
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

	/*
	 *
	 *Leons code for key listener and other small things
	 *
	 *
	 *
	 *
	 */

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (!keysDown.contains(e.getKeyCode()))
			keysDown.add(new Integer(e.getKeyCode()));
		if(chatMode){
			currentMessage+=e.getKeyChar();
		}
		actionKeys();
		repaint();
	}


	private void actionKeys() {

		if (keysDown.contains(KeyEvent.VK_ALT)){
			chatMode = !chatMode;
		}
		if (chatMode){
			if (keysDown.contains(KeyEvent.VK_ENTER)){
				//chatMessages.add(new ChatMessage("Ryan", currentMessage, Color.RED));
				try {
					ClientTest.gc.sendChatMessageToServer(currentMessage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentMessage = "";
			} else {

			}
		}
		else{
			if (keysDown.contains(KeyEvent.VK_CONTROL)) {
				directionI = (directionI + 1) % 4;
			}
			if(keysDown.contains(KeyEvent.VK_W)){
				moveForward();
			}
			if(keysDown.contains(KeyEvent.VK_A)){
				moveLeft();
			}
			if(keysDown.contains(KeyEvent.VK_S)){
				moveBack();
			}
			if(keysDown.contains(KeyEvent.VK_D)){
				moveRight();
			}
		}
		keysDown.clear();
//		System.out.println(gameClient.roomIsModified());
//		while(!gameClient.roomIsModified()){
//			System.out.println("checking modified");
//
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		gameClient.setRoomModified(false);
		repaint();
	}

	private void moveRight() {
		//System.out.println(player);
		Move move = new Move(ClientTest.gc.getPlayer(), "D", Direction.get(directionI));

		try {
			ClientTest.gc.sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void moveBack() {
		//System.out.println(player);
		Move move = new Move(ClientTest.gc.getPlayer(), "S", Direction.get(directionI));

		try {
			ClientTest.gc.sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void moveLeft() {
		//System.out.println(player);
		Move move = new Move(ClientTest.gc.getPlayer(), "A", Direction.get(directionI));

		try {
			ClientTest.gc.sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void moveForward() {

//		System.out.println(player);
		Move move = new Move(ClientTest.gc.getPlayer(), "W", Direction.get(directionI));

		try {
			ClientTest.gc.sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
