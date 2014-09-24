package rendering;

import gameLogic.gameState.NewGame;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;

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



import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Rendering extends JPanel implements KeyListener{

	//testing code with game logic
	Room room;
	int scale = 40;
	int width = 1 * scale;
	int height =width;
	Point corner = new Point(350,100);

	public Rendering(Room room){
		this.room = room;
	}

	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		drawLocation(g);
		drawInventory(g);
		drawCompas(g);
	}

	private void drawLocation(Graphics g) {
		ArrayList<Tile2D> tiles = (ArrayList<Tile2D>) room.getTiles();
		for(int i = 0; i < tiles.size(); i++){
			int x = tiles.get(i).getXPos() * width;
			int y = tiles.get(i).getYPos() * height;
			String tileName = tiles.get(i).getClass().getName();
			placeTile(twoDToIso(new Point(x,y)),tileName,g);
		}

	}

	private void placeTile(Point pt, String tileName, Graphics g) {
		System.out.println(tileName);
		java.net.URL imageURL = Rendering.class.getResource(tileName+".png");
		//System.out.println(imageURL.toString());

		Image img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}


		//		if (rotated90){
//			// Flip the image horizontally
//			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//			tx.translate(-img.getWidth(null), 0);
//			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//			img = op.filter((BufferedImage) img, null);
//		}
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
