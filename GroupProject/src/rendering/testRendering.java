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
		setVisible(true); // make sure we are visible!
		this.addKeyListener(canvas);
	}

	//Makes a fake room for testing purposes
	private Room makeRoom() {

		int roomNumber = 1;

		Tile2D[][] tiles = new Tile2D[][]{
				{new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w")},
				{new Door(1,0,"w"), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Wall(3,0,"w") },
				{new Wall(3,0,"w"), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false),new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Wall(3,0,"w") },
				{new Wall(3,0,"w"), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false),new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Wall(3,0,"w") },
				{new Wall(3,0,"w"), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false),new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Wall(3,0,"w") },
				{new Wall(3,0,"w"), new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Floor(0,1,"f", false),new Floor(0,1,"f", false), new Floor(0,1,"f", false), new Wall(3,0,"w") },
				{new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Wall(3,0,"w"), new Door(1,0,"w"), new Wall(3,0,"w"), }
		};

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
