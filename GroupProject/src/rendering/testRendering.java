package rendering;

import gameLogic.Avatar;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Item;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Wall;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import networking.GameClient;
import networking.Player;


/**
 *
 * @author northleon
 *
 */
public class testRendering extends javax.swing.JFrame {

	public static Rendering canvas;
	public static GameClient gameClient;
	public static Player player;

	public testRendering () {


		gameClient = new GameClient("Ryan");
		try {
			gameClient.connect("130.195.7.84", "Ryan", 32768);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.gameClient = gameClient;

		//System.out.println(gameClient.getChatHistory().get(0));
		System.out.println(gameClient.getName());


		Room room = gameClient.getRoom();
		while (room == null){
			room = gameClient.getRoom();
			System.out.println("room check");
		}

		Avatar avatar = gameClient.getAvatar();
		while (avatar == null){
			avatar = gameClient.getAvatar();
			System.out.println("avatar check");
		}

		Player player = gameClient.getPlayer();
		while (player == null){
			this.player = gameClient.getPlayer();
		}
		System.out.println(player);


		canvas = new Rendering(room, avatar, this, player, gameClient);
		setLayout(new BorderLayout()); // use border layout
		add(canvas, BorderLayout.CENTER); // add canvas
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // pack components tightly together
		setVisible(true); // make sure we are visible!
		this.addKeyListener(canvas);




//		Room room = makeRoom();
//		canvas = new Rendering(room);
//		setLayout(new BorderLayout()); // use border layour
//		add(canvas, BorderLayout.CENTER); // add canvas
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		pack(); // pack components tightly together
//		setVisible(true); // make sure we are visible!
//		this.addKeyListener(canvas);

	}

	//Makes a fake room for testing purposes
	private Room makeRoom() {

		int roomNumber = 1;

		Tile2D[][] tiles = new Tile2D[][]{
				{new Wall(0,0),  new Wall(1,0),         new Wall(2,0),         new Wall(3,0),        new Wall(4,0),         new Wall(5,0),        new Wall(6,0)},
				{new Door(0,1), new Floor(1,1, false), new Floor(2,1, false), new Floor(3,1, false),new Floor(4,1, false), new Floor(5,1, false), new Wall(6,1) },
				{new Wall(0,2), new Floor(1,2, false), new Floor(2,2, false), new Floor(3,2, false),new Floor(4,2, false), new Floor(5,2, false), new Wall(6,2) },
				{new Wall(0,3), new Floor(1,3, false), new Floor(2,3, false), new Floor(3,3, false),new Floor(4,3, false), new Floor(5,3, false), new Wall(6,3) },
				{new Wall(0,4), new Floor(1,4, false), new Floor(2,4, false), new Floor(3,4, false),new Floor(4,4, false), new Floor(5,4, false), new Wall(6,4) },
				{new Wall(0,5), new Floor(1,5, false), new Floor(2,5, false), new Floor(3,5, false),new Floor(4,5, false), new Floor(5,5, false), new Wall(6,5) },
				{new Wall(0,6),  new Wall(1,6),         new Wall(2,6),         new Wall(3,6),        new Wall(4,6),         new Door(5,6),        new Wall(6,6), }
		};

		Room room = new Room(roomNumber,tiles,null);
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j].setRoom(room);
			}
		}
		return room;
	}

	public void repaint(){
		System.out.println("in repaint");
		canvas.repaint();
	}

	public static void main(String[] args) {
		new testRendering();
	}
}
