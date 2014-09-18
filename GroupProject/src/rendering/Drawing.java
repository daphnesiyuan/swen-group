package rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class Drawing extends JPanel implements KeyListener{

	int scale = 40;
	int width = 1 * scale;
	int height =width;
	Point corner = new Point(350,100);
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

		int[][] room = new int[][] {
				{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
				{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 3 },
				{ 3, 1, 1, 1, 3, 3, 3, 1, 1, 3 },
				{ 3, 1, 1, 1, 1, 1, 3, 1, 1, 3 },
				{ 3, 1, 1, 1, 1, 1, 3, 1, 1, 3 },
				{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 3 },
				{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 3 },
				{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 3 },
				{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 3 },
				{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 } };

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		width = 1 * scale;
		height =width;
		//draw background
		g.setColor(Color.BLACK);
		g.fillRect(0,0,getWidth(),getHeight());



		g.setColor(Color.WHITE);
		for(int i = 0; i < room.length; i++){
			for (int j = 0; j < room.length; j++) {
				int x = j * width;
				int y = i * height;
				placetile(room[i][j], twoDToIso(new Point(x, y)), g);
				//break;
			}
		}
	}


	private void placetile(int i, Point pt, Graphics g) {


		java.net.URL imageURL = Drawing.class.getResource("tile2.png");
		if (i == 3){
			imageURL = Drawing.class.getResource("Wall2.png");
			}
		Image img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int imgHeight = ((int) img.getHeight(null) / img.getWidth(null) ) + 1 * height;
		//int y = img.getHeight(null) - (height/2);
		int y = (int) (height );
		System.out.println((int)20/img.getHeight(null));

		if(i == 3){g.drawImage(img, corner.x+pt.x - width, corner.y+pt.y - (height/2) - (y),width*2, height*2, null);}
		else
		g.drawImage(img, corner.x+pt.x - width, corner.y+pt.y - (height/2),width*2, height, null);

		//width = 20;
		//height = 20;
		//g.fillOval(pt.x + corner.x - 5, pt.y +corner.y - 5, 10, 10);

	}

	/*
	 *
function isoTo2D(pt:Point):Point{
  var tempPt:Point = new Point(0, 0);
  tempPt.x = (2 * pt.y + pt.x) / 2;
  tempPt.y = (2 * pt.y - pt.x) / 2;
  return(tempPt);
}
	 */


	private Point twoDToIso(Point pt) {
	  Point tempPt = new Point(0,0);
	  tempPt.x = pt.x - pt.y;
	  tempPt.y = (pt.x + pt.y) / 2;
	  return(tempPt);
	}


	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(700, 600);
		return dimension;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!keysDown.contains(e.getKeyCode()))
			keysDown.add(new Integer(e.getKeyCode()));
		moveCorner();

	}

	private void moveCorner() {
		if (keysDown.contains(KeyEvent.VK_UP))
			corner.y -= height;

		if (keysDown.contains(KeyEvent.VK_DOWN))
			corner.y += height;

		if (keysDown.contains(KeyEvent.VK_LEFT))
			corner.x -= width*2;

		if (keysDown.contains(KeyEvent.VK_RIGHT))
			corner.x += width*2;

		if (keysDown.contains(KeyEvent.VK_SPACE))
			rotate90();

		if (keysDown.contains(KeyEvent.VK_A))
			scale++;

		if (keysDown.contains(KeyEvent.VK_Z))
			scale--;

		System.out.println(corner.x);
		repaint();

	}


	private void rotate90() {
		// TODO Auto-generated method stub
	    int w = room.length;
	    int h = room[0].length;
	    int[][] ret = new int[h][w];
	    for (int i = 0; i < h; ++i) {
	        for (int j = 0; j < w; ++j) {
	            ret[i][j] = room[w - j - 1][i];

	        }
	    }
	    room = ret;
	}


	@Override
	public void keyReleased(KeyEvent e) {
		keysDown.remove(new Integer(e.getKeyCode()));

	}
}
