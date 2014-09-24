package rendering;

import gameLogic.location.Door;
import gameLogic.location.Floor;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.location.Wall;
import gameLogic.physical.Item;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class testRendering extends javax.swing.JFrame {

	public static Rendering canvas;

	public testRendering () {
		Room room = makeRoom();
		canvas = new Rendering(room);
		setLayout(new BorderLayout()); // use border layour
		add(canvas, BorderLayout.CENTER); // add canvas
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // pack components tightly together
		//setResizable(false); // prevent us from being resizeable
		setVisible(true); // make sure we are visible!
		this.addKeyListener(canvas);
	}

	private Room makeRoom() {
		//int r, List<Tile2D> roomTiles, List<? extends Item> roomitems) {
		int roomNumber = 1;
		List<Tile2D> tiles = new ArrayList<Tile2D>();
		tiles.add(new Wall(0,0,"w"));
		tiles.add(new Door(1,0,"w"));
		tiles.add(new Wall(2,0,"w"));
		tiles.add(new Wall(3,0,"w"));
		tiles.add(new Floor(0,1,"f", false));
		tiles.add(new Floor(1,1,"f", false));
		tiles.add(new Floor(2,1,"f", false));
		tiles.add(new Floor(3,1,"f", false));
		tiles.add(new Floor(0,2,"f", false));
		tiles.add(new Floor(1,2,"f", false));
		tiles.add(new Floor(2,2,"f", false));
		tiles.add(new Floor(3,2,"f", false));
		tiles.add(new Floor(0,3,"f", false));
		tiles.add(new Floor(1,3,"f", false));
		tiles.add(new Floor(2,3,"f", false));
		tiles.add(new Floor(3,3,"f", false));
		Room room = new Room(roomNumber,tiles,null);
		return room;
	}

	public void repaint(){
		canvas.repaint();
	}

	public static void main(String[] args) {
		new testRendering();
	}
}
