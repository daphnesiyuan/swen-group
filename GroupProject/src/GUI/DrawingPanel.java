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
import java.util.List;

import javax.swing.JPanel;

import networking.ChatMessage;
import networking.GameClient;
import networking.GameServer;
import rendering.Direction;
import rendering.DrawChat;
import rendering.DrawCompass;
import rendering.DrawHealth;
import rendering.DrawInventory;
import rendering.DrawMiniMap;
import rendering.DrawWorld;
import rendering.ScoreBoard;

/**
 *
 * @author Daphne Wang and Leon North
 *
 */
public class DrawingPanel extends JPanel {

	private DrawWorld dw; // this draws all the game-stuff: locations chars
							// items etc
	private StartMenu sm;
	private WindowFrame wf;
	public static boolean startMenu; // play or menu mode flag

	private MyMouseListener mouse;
	private int mouseX;
	private int mouseY;
	private int hoverX;
	private int hoverY;
	private Handler handler;
	private KeyBoard keyboard;
	private MouseMotion mouseMotion;
	private String hoveredButton = "";

	private JoinMenu joinMenu;

	private XMLFile fileChooser;


	private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	private String currentMessage = "";
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

	private GameServer gs;
	private GameClient gc;

	private int directionI;
	private DrawCompass compass;
	private DrawInventory invo;
	private DrawMiniMap map;
	private ScoreBoard score;
	private DrawHealth health;

	// leon added
	private DrawChat chat;
	private boolean chatMode; // from rendering


	public DrawingPanel(WindowFrame win) {
		wf = win;
		sm = new StartMenu(this);
		startMenu = true; // by default
		handler = new Handler();

		directionI = 0;

		// set up mouse and key board stuff
		setUpMouseKeys();

		// leon added:
		chat = new DrawChat(this);

		// networking setup stuff
		// gs = new GameServer();
		// setUpNWEN();
		gc = new GameClient("Daphne", this);

		score = new ScoreBoard(this);
		health = new DrawHealth(this);
	}

	/**
	 * Paint Component: has two cases, redrawing for the start menu and
	 * redrawing for the in game - uses redraw methods from Leon's rendering classes
	 * @author Daphne Wang and Leon North
	 */
	@Override
	protected void paintComponent(Graphics g) {

		if (startMenu) {
			sm.redraw(g);
		}

		else { // else it is in game
			if (gc.getRoom() != null && gc.getAvatar() != null) {
				dw.redraw(g, gc.getRoom(), Direction.get(directionI),
						gc.getAvatar());
				compass.redraw(g, Direction.get(directionI));
				invo.redraw(g, gc.getAvatar().getInventory());
				map.redraw(g, gc.getRoom(), Direction.get(directionI));
				score.redraw(g, gc.getScore());
				health.redraw(g, gc.getAvatar());

				if (chatMode)
					chat.redraw(g, gc.getChatHistory(20), currentMessage);
			}
		}
	}

	/**
	 * Helper method for setting up the mouse and key listeners
	 */
	public void setUpMouseKeys() {
		keyboard = new KeyBoard(this);
		mouse = new MyMouseListener(this);
		this.addMouseListener(mouse);
		mouseMotion = new MouseMotion(this);
		this.addMouseMotionListener(mouseMotion);
	}

	/**
	 * helper method used by the mouse classes to interact with the panel and
	 * register clicks
	 *
	 * @param x the x coordinate of the click
	 * @param y the y coordinate of the click
	 */
	public void sendClick(int x, int y) {
		mouseX = x;
		mouseY = y;
		handler.mouseListener();
	}

	/**
	 * helper method used by the mouse classes to interact with the panel and
	 * register when the curser has moved
	 *
	 * @param x  the x coordinate of the move position
	 * @param y  the y coordinate of the move position
	 */
	public void sendHover(int x, int y) {
		hoverX = x;
		hoverY = y;
		handler.mouseMoved();
	}

	/**
	 * A helper method which takes cordinates and finds the button that match
	 * those If no matching button is found on the mouse click then it will
	 * return an empty string
	 *
	 * @param x the x coordinate of the click
	 * @param y the y coordinate of the click
	 * @return the string name associated with the appropriate button
	 */
	public String findButton(int x, int y) {

		int startW = (getWidth() / 2 - (sm.getButtonWidth() / 2));
		int startH1 = getHeight() / 3 - sm.getButtonHeight() / 2
				+ (-1 * (getHeight() / 3) / 2);
		int startH2 = getHeight() / 3 - sm.getButtonHeight() / 2
				+ (0 * (getHeight() / 3) / 2);
		int startH3 = getHeight() / 3 - sm.getButtonHeight() / 2
				+ (1 * (getHeight() / 3) / 2);
		int startH4 = getHeight() / 3 - sm.getButtonHeight() / 2
				+ (2 * (getHeight() / 3) / 2);

		if (x >= startW && x <= startW + sm.getButtonWidth() && y > startH1
				&& y < startH1 + sm.getButtonHeight()) {
			return "start";
		} else if (x >= startW && x <= startW + sm.getButtonWidth()
				&& y > startH2 && y < startH2 + sm.getButtonHeight()) {
			return "join";
		} else if (x >= startW && x <= startW + sm.getButtonWidth()
				&& y > startH3 && y < startH3 + sm.getButtonHeight()) {
			return "load";
		} else if (x >= startW && x <= startW + sm.getButtonWidth()
				&& y > startH4 && y < startH4 + sm.getButtonHeight()) {
			return "help";
		}

		return "";
	}

	/**
	 * A helper method which sets up the client/server tools required to play
	 * the game
	 */
	public boolean setUpNWEN() {
		//gc = new GameClient("Daphne", this);

		try {
			// gc.connect("130.195.6.69",32768); //jimmy
			gc.connect(gs); // your own server
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
		while (room == null) {
			room = gc.getRoom();
			System.out.println(room);
			System.out.println(gc.isConnected());
		}
		return true;
	}

	/**
	 * A private inner class to take care of the actions associated with mouse
	 * hovering and clicking
	 */
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}

		// handles hovering
		public void mouseMoved() {
			if (startMenu) {
				if (findButton(hoverX, hoverY).equals("start")) {
					hoveredButton = "start";
					sm.loadHoverButton("start");
				} else if (findButton(hoverX, hoverY).equals("join")) {
					hoveredButton = "join";
					sm.loadHoverButton("join");
				} else if (findButton(hoverX, hoverY).equals("load")) {
					hoveredButton = "load";
					sm.loadHoverButton("load");
				} else if (findButton(hoverX, hoverY).equals("help")) {
					hoveredButton = "help";
					sm.loadHoverButton("help");
				}

				else if (hoveredButton != "") {
					sm.resetUnHoverButton(hoveredButton);
					// sends through the button which was last hovered on
					hoveredButton = "";
				}
			}
		}

		/**
		 * Handles the mouse clicking of the start menu buttons
		 */
		public void mouseListener() {
			String s = findButton(mouseX, mouseY);
			if (startMenu) {
				if (s.equals("start")) {
					gs = new GameServer(); //might remove later
					//setUpNWEN();

					StartGUI startG = new StartGUI(DrawingPanel.this, gc, gs);
					startG.setup();

					/*dw = new DrawWorld(gc.getAvatar(), DrawingPanel.this);
					compass = new DrawCompass(DrawingPanel.this);
					invo = new DrawInventory(DrawingPanel.this);
					map = new DrawMiniMap(DrawingPanel.this, gc.getAvatar());
					repaint();*/


				} else if (s.equals("join")) {
					joinMenu = new JoinMenu(DrawingPanel.this, gc);
					joinMenu.setup();

				} else if (s.equals("load")) {
					fileChooser = new XMLFile(wf);

				} else if (s.equals("help")) {

				} else {
					System.out.println("no active button");
				}
			}

			else { // not in start menu and in game.

				int i = invo.findBox(mouseX, mouseY);
				if (i == 0) { // box 1
					if (i <= (gc.getAvatar().getInventory().size())
							&& (gc.getAvatar().getInventory().get(0)) != null) {
						Item item = gc.getAvatar().getInventory().get(0);
					}
				} else if (i == 1) {
					if (i <= (gc.getAvatar().getInventory().size())
							&& (gc.getAvatar().getInventory().get(1)) != null) {
						Item item = gc.getAvatar().getInventory().get(1);
					}
				} else if (i == 2) {
					if (i <= (gc.getAvatar().getInventory().size())
							&& (gc.getAvatar().getInventory().get(2)) != null) {
						Item item = gc.getAvatar().getInventory().get(2);
					}
				}

				else if (i == 3) {
					if (i <= (gc.getAvatar().getInventory().size())
							&& (gc.getAvatar().getInventory().get(3)) != null) {
						Item item = gc.getAvatar().getInventory().get(3);
					}
				} else if (i == 4) {
					if (i <= (gc.getAvatar().getInventory().size())
							&& (gc.getAvatar().getInventory().get(4)) != null) {
						Item item = gc.getAvatar().getInventory().get(4);
					}
				}
			}
		}

	}

	public void testMethod(){

		System.out.println(">>>>>>>>>> IN TEST METHOD");

		dw = new DrawWorld(gc.getAvatar(), DrawingPanel.this);
		compass = new DrawCompass(DrawingPanel.this);
		invo = new DrawInventory(DrawingPanel.this);
		map = new DrawMiniMap(DrawingPanel.this, gc.getAvatar());
		repaint();
	}

	////////////////getters and setters
	/**
	 * @author Daphne Wang
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1280, 720);
		return dimension; }

	public void setGameMode() {
		startMenu = false; }

	public KeyBoard getKeyB() {
		return keyboard; }

	public int getDirection() {
		return directionI; }

	public void setDirection(int d) {
		directionI = d; }

	public GameClient getGameClient() {
		return gc; }

	public ArrayList<Integer> getKeysDown() {
		return keysDown; }

	public void startDrawWorld() { //uses constructors of leon's constructors
		dw = new DrawWorld(gc.getAvatar(), DrawingPanel.this);
		compass = new DrawCompass(DrawingPanel.this);
		invo = new DrawInventory(DrawingPanel.this);
		map = new DrawMiniMap(DrawingPanel.this, gc.getAvatar());
	}

	//helper methods for leon and jimmy's chat stuff
	public boolean isChatMode() {
		return chatMode; }

	public void setChatMode(boolean b) {
		chatMode = b; }

	public void addToCurrentMessage(String s) {
		currentMessage += s; }

	public void setCurrentMessage(String s) {
		currentMessage = s; }

	public String getCurrentMessage() {
		return currentMessage; }

}
