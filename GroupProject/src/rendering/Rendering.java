package rendering;

import gameLogic.entity.GameCharacter;
import gameLogic.gameState.Game;
import gameLogic.gameState.NewGame;
import gameLogic.location.*;

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
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;





public class Rendering extends JPanel implements KeyListener{

	//testing code with game logic
	Room room;
	int scale = 50;
	int width = 1 * scale;
	int height =width;
	Point corner = new Point(600,300);
	Map<Integer, String> directionMap = new HashMap<Integer, String>();
	int direction;
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();
	DrawWorld draw;

	Tile2D tile = new Floor(2,2,"f", false);
	GameCharacter charac = new GameCharacter("willy", tile, null);



	public Rendering(Room room){
		this.room = room;
		direction = 0;
		draw = new DrawWorld(charac, this);
		directionMap.put(0, "north");
		directionMap.put(1, "west");
		directionMap.put(2, "south");
		directionMap.put(3, "east");

	}

	protected void paintComponent(Graphics g) {
		draw.redraw(g, room, charac, directionMap.get(direction));
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
		actionKeys();
		//System.out.println("bla");
		repaint();
	}

	private void actionKeys() {
		if (keysDown.contains(KeyEvent.VK_SPACE)){
			direction = (direction + 1) % 4;
		}
		keysDown.clear();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
