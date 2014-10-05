package rendering;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class FloatingPointer {

	int totalAnimations = 6;
	int animationNum = 0;
	Map<String, BufferedImage> images;

	public FloatingPointer() {
		// TODO Auto-generated constructor stub
		images = MakeImageMap.makeMap();
	}

	private void makeFloatingSquareMap() {
//		images = new HashMap<String,BufferedImage>();
//		BufferedImage img = null;
//		java.net.URL imageURL = null;
//
//		for (int i = 0; i < totalAnimations; i++) {
//			imageURL = Rendering.class.getResource("FloatingPointer" + i
//					+ ".png");
//			try {
//				img = ImageIO.read(imageURL);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			images.put("FloatingPointer"+i, img);
//		}

	}

	public void reDraw(Graphics g, Point pt, int width, int height, Point offset) {

		BufferedImage img = images.get("FloatingPointer" + animationNum);
		int imgHeight = ((int) img.getHeight(null) / 250);

		g.drawImage(img, offset.x + pt.x - width, offset.y + pt.y
				- ((width * imgHeight)), width * 2, height * imgHeight, null);

		animationNum = (animationNum + 1) % totalAnimations;
	}

}
