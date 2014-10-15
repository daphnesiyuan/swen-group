package GUI;

import gameLogic.Item;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private DrawWorld dw; // this draws all the game-stuff: avatars items etc
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

	private JoinGUI joinG;
	private StartGUI startG;
	private HelpMenu help;

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
		help = new HelpMenu(DrawingPanel.this);

		directionI = 0;

		setUpMouseKeys();

		// leon added:
		chat = new DrawChat(this);

		// networking setup stuff
		gc = new GameClient("Daphne", this);

		score = new ScoreBoard(this);
		health = new DrawHealth(this);
	}

	/**
	 * Paint Component: has two cases, redrawing for the start menu and
	 * redrawing for the in game - uses redraw methods from Leon's rendering
	 * classes
	 *
	 * @author Daphne Wang and Leon North
	 */
	@Override
	protected void paintComponent(Graphics g) {

		if (help.isHelpMode()) {
			help.drawHelp(g);
		}

		else if (startMenu)
			sm.redraw(g);

		else if (!startMenu) { // else it is in game
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
	 * @author Daphne Wang
	 */
	public void setUpMouseKeys() {
		keyboard = new KeyBoard(this, help);
		mouse = new MyMouseListener(this);
		this.addMouseListener(mouse);

		mouseMotion = new MouseMotion(this);
		this.addMouseMotionListener(mouseMotion);
	}

	/**
	 * helper method called from mouse listener classes to send over locations
	 * of recent clicks to this panel
	 *
	 * @param x the x coordinate of the click
	 * @param y the y coordinate of the click
	 *
	 * @author Daphne Wang
	 */
	public void sendClick(int x, int y) {
		mouseX = x;
		mouseY = y;
		handler.mouseListener();
	}

	/**
	 * helper method used by the mouse-hover classes to register and send over
	 * where the mouse has most recently moved/hovered on the panel.
	 *
	 * @param x the x coordinate of the move position
	 * @param y the y coordinate of the move position
	 *
	 * @author Daphne Wang
	 */
	public void sendHover(int x, int y) {
		hoverX = x;
		hoverY = y;
		handler.mouseMoved();
	}

	/**
	 * Method to take care of situations where users disconnect from the game
	 * and reverting back to the start menu
	 *
	 * @author Jimmy Veug
	 */
	private class GameCheckThread implements Runnable {
		@Override
		public void run() {
			while (gc.isConnected()) {

				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("disconnected");
			startMenu = true;
		}
	}

	/**
	 * A private inner class to take care of the actions associated with mouse
	 * hovering and clicking
	 * @author Daphne Wang
	 */
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}

		// handles hovering on the start menu and also in game
		public void mouseMoved() {
			if (startMenu) {
				if (sm.findButton(hoverX, hoverY).equals("start")) {
					hoveredButton = "start";
					sm.loadHoverButton("start");
				} else if (sm.findButton(hoverX, hoverY).equals("join")) {
					hoveredButton = "join";
					sm.loadHoverButton("join");
				} else if (sm.findButton(hoverX, hoverY).equals("load")) {
					hoveredButton = "load";
					sm.loadHoverButton("load");
				} else if (sm.findButton(hoverX, hoverY).equals("help")) {
					hoveredButton = "help";
					sm.loadHoverButton("help");
				} else if (hoveredButton != "") {
					sm.resetUnHoverButton(hoveredButton);
					// sends through the button which was last hovered on
					hoveredButton = "";
				}
			}

			else { // must be in game play mode
				int i = invo.findBox(hoverX, hoverY);
				if (i == 0) {
					if (i <= (getGameClient().getAvatar().getInventory().size())
							&& (getGameClient().getAvatar().getInventory()
									.size() > 0)) {
						String d = getGameClient().getAvatar().getInventory()
								.get(0).getDescription();
						setToolTipText(d);
					}
				} else if (i == 1) {
					if (i < (getGameClient().getAvatar().getInventory().size())
							&& (getGameClient().getAvatar().getInventory()
									.get(1)) != null) {
						String d = getGameClient().getAvatar().getInventory()
								.get(1).getDescription();
						setToolTipText(d);
					}
				} else if (i == 2) {
					if (i < (getGameClient().getAvatar().getInventory().size())
							&& (getGameClient().getAvatar().getInventory()
									.get(2)) != null) {
						String d = getGameClient().getAvatar().getInventory()
								.get(2).getDescription();
						setToolTipText(d);
					}
				} else if (i == 3) {
					if (i < (getGameClient().getAvatar().getInventory().size())
							&& (getGameClient().getAvatar().getInventory()
									.get(3)) != null) {
						String d = getGameClient().getAvatar().getInventory()
								.get(3).getDescription();
						setToolTipText(d);
					}
				} else if (i == 4) {
					if (i < (getGameClient().getAvatar().getInventory().size())
							&& (getGameClient().getAvatar().getInventory()
									.get(4)) != null) {
						String d = getGameClient().getAvatar().getInventory()
								.get(4).getDescription();
						setToolTipText(d);
					}
				} else {
					setToolTipText(null);
				}
			}
		}

		/**
		 * Handles the mouse clicking of the start menu buttons
		 * @author Daphne Wang
		 */
		public void mouseListener() {
			String s = sm.findButton(mouseX, mouseY);
			if (startMenu && !help.isHelpMode()) {
				if (s.equals("start")) {
					gs = new GameServer();
					startG = new StartGUI(DrawingPanel.this, gc, gs);
					startG.setup();

				} else if (s.equals("join")) {
					joinG = new JoinGUI(DrawingPanel.this, gc);
					joinG.setup();

				} else if (s.equals("load")) {
					fileChooser = new XMLFile(wf);

				} else if (s.equals("help")) {
					help.helpOn();
					repaint();

				} else {
				}
			}

		}

	}

	/**
	 * Helper method to action the drawing of the game when you click interact
	 * with start or join after connecting to the game This prepares the map
	 * visuals
	 *
	 * @author Daphne Wang
	 */
	public void startDrawWorld() {
		dw = new DrawWorld(gc.getAvatar(), DrawingPanel.this);
		compass = new DrawCompass(DrawingPanel.this);
		invo = new DrawInventory(DrawingPanel.this);
		map = new DrawMiniMap(DrawingPanel.this, gc.getAvatar());
		repaint();

		Thread th = new Thread(new GameCheckThread()); // Jimmy's stuff
		th.start();
	}

	// //////////////getters and setters
	/**
	 * @author Daphne Wang
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1280, 720);
		return dimension;
	}

	public boolean isStartMode() {
		return startMenu;
	}

	public void setGameMode() {
		startMenu = false;
	}

	public void setStartMode() {
		startMenu = true;
	}

	public KeyBoard getKeyB() {
		return keyboard;
	}

	public DrawInventory getInvo() {
		return invo;
	}

	public int getDirection() {
		return directionI;
	}

	public void setDirection(int d) {
		directionI = d;
	}

	public GameClient getGameClient() {
		return gc;
	}

	public GameServer getGameServer() {
		return gs;
	}

	public ArrayList<Integer> getKeysDown() {
		return keysDown;
	}

	// leon and jimmy's chat methods
	public boolean isChatMode() {
		return chatMode;
	}

	public void setChatMode(boolean b) {
		chatMode = b;
	}

	public void addToCurrentMessage(String s) {
		currentMessage += s;
	}

	public void setCurrentMessage(String s) {
		currentMessage = s;
	}

	public String getCurrentMessage() {
		return currentMessage;
	}

}
