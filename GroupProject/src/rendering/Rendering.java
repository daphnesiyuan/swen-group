package rendering;

import gameLogic.gameState.NewGame;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;
import gameLogic.location.*;

import java.awt.Color;
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

import javax.imageio.ImageIO;
import javax.swing.JPanel;





public class Rendering extends JPanel implements KeyListener{

	//testing code with game logic
	Room room;
	int scale = 40;
	int width = 1 * scale;
	int height =width;
	Point corner = new Point(350,100);



	public Rendering(Room room){
		this.room = room;
		//this.
	}

	protected void paintComponent(Graphics g) {
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, this.getWidth(), this.getHeight());
//		drawLocation(g);
//		drawInventory(g);
//		drawCompas(g);
		DrawWorld draw = new DrawWorld(null, this);
		draw.redraw(g, room, null, null);
	}

	private void drawLocation(Graphics g) {
		Tile2D[][] tiles = room.getTiles();
		for(int i = 0; i < tiles.length; i++){
			for (int j = 0; j < tiles[i].length; j++) {
				int x = i * width;
				int y = j * height;
				String tileName = tiles[i][j].getClass().getName();
				placeTile(twoDToIso(new Point(x,y)),tileName,g);
			}
		}
	}

	private void placeTile(Point pt, String tileName, Graphics g) {
		System.out.println(tileName);
		java.net.URL imageURL = Rendering.class.getResource(tileName+".png");

		Image img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imgHeight = ((int) img.getHeight(null)/20);

		g.drawImage(img, corner.x+pt.x - width, corner.y+pt.y - ((width*imgHeight)),width*2, height*imgHeight, null);
	}

	private Point twoDToIso(Point pt) {
		  Point tempPt = new Point(0,0);
		  tempPt.x = pt.x - pt.y;
		  tempPt.y = (pt.x + pt.y) / 2;
		  return(tempPt);
	}

	private void drawInventory(Graphics g) {
		// TODO Auto-generated method stub
	}

	private void drawCompas(Graphics g) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
