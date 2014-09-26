package rendering;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gameLogic.entity.GameCharacter;
import gameLogic.location.Room;
import gameLogic.location.Tile2D;

public class DrawWorld {

	GameCharacter character;
	String direction;
	Tile2D[][] rotatedArray;
	int scale = 40;
	int width = 1 * scale;
	int height = width;
	Point offset = new Point(350,100);
	JPanel panel;

	public DrawWorld(GameCharacter character, Rendering rendering){
		this.character = character;
		this.panel = rendering;
	}


	public void redraw(Graphics g, Room room, GameCharacter character, String direction){

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		drawLocation(g, room, direction);
//		drawInventory(g);
//		drawCompas(g);



	}

	private void drawLocation(Graphics g, Room room, String direction2) {
		// TODO Auto-generated method stub
		Tile2D[][] tiles = rotate2DArray(room.getTiles(), direction);

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
		java.net.URL imageURL = Rendering.class.getResource(tileName+".png");

		Image img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imgHeight = ((int) img.getHeight(null)/20);

		g.drawImage(img, offset.x+pt.x - width, offset.y+pt.y - ((width*imgHeight)),width*2, height*imgHeight, null);

	}


	private Point twoDToIso(Point point) {
		  Point tempPt = new Point(0,0);
		  tempPt.x = point.x - point.y;
		  tempPt.y = (point.x + point.y) / 2;
		  return(tempPt);
	}


	private Tile2D[][] rotate2DArray(Tile2D[][] tiles, String direction) {
		return tiles;
	}


}
