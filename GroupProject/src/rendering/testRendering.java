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


/**
 *
 * @author northleon
 *
 */
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
				{new Wall(0,0,"w"),  new Wall(1,0,"w"),         new Wall(2,0,"w"),         new Wall(3,0,"w"),        new Wall(4,0,"w"),         new Wall(5,0,"w"),        new Wall(6,0,"w")},
				{new Door(0,1,"w"), new Floor(1,1,"f", false), new Floor(2,1,"f", false), new Floor(3,1,"f", false),new Floor(4,1,"f", false), new Floor(5,1,"f", false), new Wall(6,1,"w") },
				{new Wall(0,2,"w"), new Floor(1,2,"f", false), new Floor(2,2,"f", false), new Floor(3,2,"f", false),new Floor(4,2,"f", false), new Floor(5,2,"f", false), new Wall(6,2,"w") },
				{new Wall(0,3,"w"), new Floor(1,3,"f", false), new Floor(2,3,"f", false), new Floor(3,3,"f", false),new Floor(4,3,"f", false), new Floor(5,3,"f", false), new Wall(6,3,"w") },
				{new Wall(0,4,"w"), new Floor(1,4,"f", false), new Floor(2,4,"f", false), new Floor(3,4,"f", false),new Floor(4,4,"f", false), new Floor(5,4,"f", false), new Wall(6,4,"w") },
				{new Wall(0,5,"w"), new Floor(1,5,"f", false), new Floor(2,5,"f", false), new Floor(3,5,"f", false),new Floor(4,5,"f", false), new Floor(5,5,"f", false), new Wall(6,5,"w") },
				{new Wall(0,6,"w"),  new Wall(1,6,"w"),         new Wall(2,6,"w"),         new Wall(3,6,"w"),        new Wall(4,6,"w"),         new Door(5,6,"w"),        new Wall(6,6,"w"), }
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
