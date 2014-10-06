package rendering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawCompass {

	JPanel panel;

	public DrawCompass(JPanel panel) {
		this.panel = panel;
	}

	public void redraw(Graphics g, String direction){


		if (direction.toLowerCase().equals("east")){
			direction = "West";
		}
		else if (direction.toLowerCase().equals("west")){
			direction = "East";
		}

		java.net.URL imageURL = DrawCompass.class.getResource("Compass"+direction+".png");
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int size = (int)((panel.getWidth()/1280.0) * 150);
		int buffer = (int) ((panel.getWidth() / 1280.0) * 10);
		g.drawImage(img, panel.getWidth() - size - buffer, panel.getHeight() - size - buffer, size, size, null);
	}

}
