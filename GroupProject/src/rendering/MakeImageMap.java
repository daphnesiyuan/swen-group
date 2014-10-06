package rendering;

import gameLogic.Avatar;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class MakeImageMap {

	static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	static int avatarAnimations = 4;
	static int tileAnimations = 2;
	static int directions = 4;
	static int pointerAnimations = 6;

	public MakeImageMap() {

	}

	public static Map<String, BufferedImage> makeMap(){
		for(int j = 0; j < directions; j++){
			for(int i = 0; i < avatarAnimations; i++){
				//Avatar A
				String avatarAName = "AvatarA"+Direction.get(j)+""+i;
				java.net.URL avatarAURL = MakeImageMap.class.getResource(avatarAName + ".png");
				addToMap(avatarAName, avatarAURL);

				//		Avatar B
				String avatarBName = "AvatarB"+Direction.get(j)+""+i;
				java.net.URL avatarBURL = MakeImageMap.class.getResource(avatarBName + ".png");
				addToMap(avatarBName, avatarBURL);
			}
		}

		for(int i = 0; i < tileAnimations; i++){
			//wall
			String wallName = "Wall"+i;
			java.net.URL wallURL = MakeImageMap.class.getResource(wallName + ".png");
			addToMap(wallName, wallURL);

			//floor
			String floorName = "Floor"+i;
			java.net.URL floorURL = MakeImageMap.class.getResource(floorName + ".png");
			addToMap(floorName, floorURL);

			//door
			String doorName = "Door"+i;
			java.net.URL doorURL = MakeImageMap.class.getResource(doorName + ".png");
			addToMap(doorName, doorURL);
		}

		for(int i = 0; i < pointerAnimations; i++){
			String pointerName = "FloatingPointer"+i;
			java.net.URL pointerURL = MakeImageMap.class.getResource(pointerName + ".png");
			addToMap(pointerName, pointerURL);
		}
		return images;

	}

	private static void addToMap(String name, java.net.URL imageURL){

		System.out.println(name);
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		images.put(name, img);
	}

}
