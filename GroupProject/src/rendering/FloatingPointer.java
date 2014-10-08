package rendering;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * 
 * @author northleon
 *
 */
public class FloatingPointer {

	int delay = 0;
	int totalAnimations = 6;
	int animationNum = 0;
	Map<String, BufferedImage> images;

	public FloatingPointer() {
		images = MakeImageMap.makeMap();
	}

	public void reDraw(Graphics g, Point pt, int width, int height, Point offset) {

		BufferedImage img = images.get("FloatingPointer" + animationNum);
		int imgHeight = ((int) img.getHeight(null) / 250);

		g.drawImage(img, offset.x + pt.x - width, offset.y + pt.y
				- ((width * imgHeight)), width * 2, height * imgHeight, null);

		delay++;
		if (delay == 3){
			delay = 0;
			animationNum = (animationNum + 1) % totalAnimations;
		}

	}

}
