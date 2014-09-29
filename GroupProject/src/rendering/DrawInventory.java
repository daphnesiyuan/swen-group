package rendering;

import gameLogic.Item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawInventory {
	JPanel panel;
	int width;
	int height;
	int buffer;

	public DrawInventory(JPanel panel) {
		this.panel = panel;
	}

	public void redraw(Graphics g, List<Item> inventory, String direction){
		width = (int) ((panel.getWidth() / 1280.0) * 100);
		height = (int) (width * 1.15);
		buffer = (int) (width / 10.0);

		//draw outline for items
		g.setColor(new Color(0.5f,0.5f,0.5f,0.5f));
		//g.setColor(Color.BLUE);
		g.fillRoundRect(width, panel.getHeight() - buffer - height - buffer - buffer, (buffer * 6 + width * 5), buffer + height + buffer, buffer, buffer);
		for(int i  = 0; i < 5; i++){
			g.fillRect(width + width*i + buffer + buffer*i, panel.getHeight() - buffer - height - buffer, width, height);
			drawInvItem(g,i,inventory);
		}
	}

	private void drawInvItem(Graphics g, int i, List<Item> inventory) {
		if (i >= inventory.size()){return;}
		String itemName = inventory.get(i).getClass().getName();
		java.net.URL imageURL = Rendering.class.getResource(itemName+".png");
		if(imageURL == null){
			System.out.println("could not find image on disk");
		}
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, width + width*i + buffer + buffer*i, panel.getHeight() - buffer - height - buffer, width, height, null);
	}
}
