package rendering;

import gameLogic.Floor;
import gameLogic.Game;
import gameLogic.Avatar;
import gameLogic.NewGame;
import gameLogic.Room;
import gameLogic.Tile2D;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import networking.ChatMessage;
import networking.GameClient;
import networking.Move;
import networking.Player;




/**
 *
 * @author northleon
 *
 */
public class Rendering extends JPanel implements KeyListener{

	testRendering testrendering;
	//testing code with game logic
	Room room;
	int scale = 50;
	int width = 1 * scale;
	int height =width;
	Point corner = new Point(600,300);
	//Map<Integer, String> directionMap = new HashMap<Integer, String>();
	int direction;
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

	DrawWorld draw;
	DrawInventory inventory;
	DrawCompass compass;
	DrawChat chat;
	DrawMiniMap miniMap;

	String currentMessage;

	Tile2D tile = new Floor(1,4, false);

	List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	boolean chatMode;
	//Avatar charac = new Avatar("willy", tile, null);
	Avatar charac;

	Player player;

	GameClient gameClient;



	public Rendering(Room room, Avatar avatar, testRendering testrendering, Player player, GameClient gameClient){
		this.gameClient = gameClient;
		this.player = player;
		this.testrendering = testrendering;
		this.room = room;
		direction = 0;
		this.charac = avatar;

		draw = new DrawWorld(charac, this);
		//draw = new DrawWorld(null, this);
		inventory = new DrawInventory(this);
		compass = new DrawCompass(this);
		miniMap = new DrawMiniMap(this, charac);

		chat = new DrawChat(this);
		currentMessage = "";


		//room.getTiles()[2][3].addPlayer(charac);

		chatMessages.add(new ChatMessage("Leon","This is a test message.", Color.BLUE));
		chatMessages.add(new ChatMessage("Ryan","This is another test message.", Color.RED));
		chatMessages.add(new ChatMessage("Jimmy","Hurr Durr.", Color.MAGENTA));
	}

	public Rendering(Room room){
		this.room = room;
		direction = 0;
		//this.charac = avatar;

		draw = new DrawWorld(this);
		inventory = new DrawInventory(this);
		compass = new DrawCompass(this);
		//miniMap = new DrawMiniMap(this, charac);



		chat = new DrawChat(this);
		currentMessage = "";

		//room.getTiles()[2][3].addPlayer(charac);

		room.getTiles()[2][3].addAvatar(charac);


		chatMessages.add(new ChatMessage("Leon","This is a test message.", Color.BLUE));
		chatMessages.add(new ChatMessage("Ryan","This is another test message.", Color.RED));
		chatMessages.add(new ChatMessage("Jimmy","Hurr Durr.", Color.MAGENTA));
	}

	protected void paintComponent(Graphics g) {

		Tile2D[][] tiles = gameClient.getRoom().getTiles();
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[0].length; j++){
				System.out.print(tiles[j][i].getAvatar());
			}
			System.out.println();
		}

		draw.redraw(g, gameClient.getRoom(), Direction.get(direction));
		inventory.redraw(g, charac.getInventory(), Direction.get(direction));
		compass.redraw(g, Direction.get(direction));
		miniMap.redraw(g, room, Direction.get(direction));
		if(chatMode)chat.redraw(g, chatMessages, currentMessage);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1280, 720);
		return dimension;
	}

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
		//System.out.println("bla");
		repaint();
	}

	private void actionKeys() {

		if (keysDown.contains(KeyEvent.VK_ALT)){
			chatMode = !chatMode;
		}
		if (chatMode){
			if (keysDown.contains(KeyEvent.VK_ENTER)){
				chatMessages.add(new ChatMessage("Ryan", currentMessage, Color.RED));
				currentMessage = "";
			} else {

			}
		}
		else{
			if (keysDown.contains(KeyEvent.VK_CONTROL)) {
				direction = (direction + 1) % 4;
			}
			if(keysDown.contains(KeyEvent.VK_W)){
				moveForward();
			}
		}
		keysDown.clear();
	}

	private void moveForward() {

		System.out.println(player);
		Move move = new Move(player, "W", "North");

		try {
			testrendering.gameClient.sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
