package rendering;

import gameLogic.Item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Draws the inventory (Max 5 Items) onto the bottom of the panel.
 *
 * @author Leon North
 *
 */
public class DrawInventory {
	private JPanel panel;
	private int width;
	private int height;
	private int buffer;
	private int firstBoxEdge;
	private int yBot;
	private int yTop;
	private int inventoryHeight;

	public DrawInventory(JPanel panel) {
		this.panel = panel;
	}

	/**
	 *
	 * @param g: Graphics object
	 * @param inventory: list of inventory objects
	 * @param direction: String of the direction
	 * @author Leon North
	 */
	public void redraw(Graphics g, List<Item> inventory){
		width = (int) ((panel.getWidth() / 1280.0) * 100);
		height = (int) (width * 1.15);
		buffer = (int) (width / 10.0);

		//numbers for mouse maths
		inventoryHeight = buffer+height+buffer;
		firstBoxEdge = width+buffer;
		yTop = panel.getHeight()-buffer-height-buffer; //from top of window to the top of the inventory box WITHIN the inventory frame
		yBot = yTop+height;

		//draw outline for items
		g.setColor(new Color(0.5f,0.5f,0.5f,0.5f));
		//g.setColor(Color.BLUE);
		g.fillRoundRect(width, panel.getHeight() - buffer - height - buffer - buffer, (buffer * 6 + width * 5), buffer + height + buffer, buffer, buffer);
		for(int i  = 0; i < 5; i++){
			g.fillRect(width + width*i + buffer + buffer*i, panel.getHeight() - buffer - height - buffer, width, height);
			drawInvItem(g,i,inventory);
		}
	}

	/**
	 * Draws items in the appropriate boxes.
	 * @param g: Graphics
	 * @param i: Position in the inventory
	 * @param inventory: List of inventory objects
	 * @author Leon North
	 */
	private void drawInvItem(Graphics g, int i, List<Item> inventory) {
		if (i >= inventory.size()){return;}
		String itemName = inventory.get(i).getClass().getName().substring(10);
		java.net.URL imageURL = DrawInventory.class.getResource(itemName+""+0+".png");
		if(imageURL == null){
			System.out.println("could not find image on disk: "+ itemName);
		}
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, width + width*i + buffer + buffer*i, panel.getHeight() - buffer - height - buffer, width, height, null);
	}

	/**
	 * Helper method used in the drawing panel which finds the appropriate inventory box which matches the mouse location
	 * @param x: x coordinate of the mouse
	 * @param y: y coordinate of the mouse
	 * @return: int for the i'th box that the mouse interacted with
	 * @author Daphne Wang
	 */
	public int findBox(int x, int y){

		if( x>=firstBoxEdge && x<=firstBoxEdge+width && y>=yTop && y<=yBot ){
			return 0;
		}
		else if( x>=firstBoxEdge+(1*buffer) && x<=firstBoxEdge+(2*width)+(2*buffer) && y>=yTop && y<=yBot ){
			return 1;
		}
		else if( x>=firstBoxEdge+(2*buffer) && x<=firstBoxEdge+(3*width)+(3*buffer) && y>=yTop && y<=yBot ){
			return 2;
		}
		else if( x>=firstBoxEdge+(3*buffer) && x<=firstBoxEdge+(4*width)+(4*buffer) && y>=yTop && y<=yBot ){
			return 3;
		}
		else if( x>=firstBoxEdge+(4*buffer) && x<=firstBoxEdge+(5*width)+(5*buffer) && y>=yTop && y<=yBot ){
			return 4;
		}

		return 50; //failure number which indicates NO box was clicked on
	}
}
