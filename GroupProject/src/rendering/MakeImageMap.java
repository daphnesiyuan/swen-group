package rendering;

import gameLogic.Avatar;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Makes a map of all the images in the game String -> Buffered image
 *
 * @author Leon North
 *
 */
public class MakeImageMap {

	static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	static int avatarAnimations = 4;
	static int tileAnimations = 2;
	static int directions = 4;
	static int pointerAnimations = 6;
	static int itemAnimations = 2;

	public MakeImageMap() {

	}

	/**
	 * Makes a map of all the images in the game String -> Buffered image
	 * and returns it
	 *
	 * @return map of String -> BufferedImage
	 * @author Leon North
	 */
	public static Map<String, BufferedImage> makeMap(){
		//Avatars
		for(int j = 0; j < directions; j++){
			for(int i = 0; i < avatarAnimations; i++){
				//Avatar A
				addToMap("AvatarA"+Direction.get(j)+""+i);
				//Avatar A Charging
				addToMap("AvatarA"+Direction.get(j)+"Charging"+i);

				//Avatar B
				addToMap("AvatarB"+Direction.get(j)+""+i);
			}
		}
		//Tiles
		for(int i = 0; i < tileAnimations; i++){
			//wall
			addToMap("Wall"+i);

			//floor
			addToMap("Floor"+i);

			//door
			addToMap("Door"+i);

			//Column
			addToMap("Column"+i);

			//doors
			addToMap("Door"+i);

			//red door
			addToMap("RedDoor"+i);

			//yellow door
			addToMap("YellowDoor"+i);

			//green door
			addToMap("GreenDoor"+i);

			//purple door
			addToMap("PurpleDoor"+i);

			//tree
			addToMap("Tree"+i);

			//charger
			addToMap("Charger"+i);
		}

		//Pointer
		for(int i = 0; i < pointerAnimations; i++){
			//pointer
			addToMap("FloatingPointer"+i);
		}

		//Items
		for(int i = 0; i < itemAnimations; i++){

			//Battery
			addToMap("Battery"+i);

			//Light
			addToMap("Light"+i);

			//Shoes
			addToMap("Shoes"+i);
		}

		//Night time
		addToMap("Night");
		addToMap("NightLight");

		return images;
	}

	/**
	 * Takes a string and adds an entry to the map by finding the corresponding
	 * file on disk
	 *
	 * @param name
	 * @author Leon North
	 */
	private static void addToMap(String name){

		//System.out.println(name);
		java.net.URL imageURL = MakeImageMap.class.getResource(name + ".png");

		BufferedImage img = null;
		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		images.put(name, img);
	}

}
