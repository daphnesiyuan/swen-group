package gameLogic;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Environment extends Thread implements Serializable{


	private static final long serialVersionUID = 388911180114116326L;

	private Game game;
	private List<Light> lights;

	private int count = 0;

	private List<String> keyColors;
	private List<String> doorColors;

	// items in the game are identified using unique item ids.
	private int itemID;

	public Environment(Game game){
		this.game = game;
		this.itemID = 0;

		this.doorColors = new ArrayList<String>();
		this.keyColors = new ArrayList<String>();

		keyColors.add("R");
		keyColors.add("G");
		keyColors.add("Y");
		keyColors.add("P");

		doorColors.add("R");
		doorColors.add("G");
		doorColors.add("Y");
		doorColors.add("P");


	}

	@Override
	public void run(){
		while(true){
			if(count == 0){		// initial environment item generation
				for(int i = 1; i < 5; i++){		// at start - put lights in all rooms but arena.
					genLight(i);
					genKey(i);
				}



				count++;
			}
		}
	}
	private void genKey(int i){
		Room room = game.getRoomsInGame().get(i);
		if(keyColors.get(0).equals("R")){
			RedKey key  = new RedKey(room.getTiles()[1][5]);
			key.setItemID(itemID++);
			room.getTiles()[1][5].addItem(key);
			keyColors.remove(0);
		}
		else if(keyColors.get(0).equals("G")){
			GreenKey key = new GreenKey(room.getTiles()[1][5]);
			key.setItemID(itemID++);
			room.getTiles()[1][5].addItem(key);
			keyColors.remove(0);
		}
		else if(keyColors.get(0).equals("Y")){
			YellowKey key = new YellowKey(room.getTiles()[1][5]);
			key.setItemID(itemID++);
			room.getTiles()[1][5].addItem(key);
			keyColors.remove(0);
		}
		else if(keyColors.get(0).equals("P")){
			PurpleKey key = new PurpleKey(room.getTiles()[1][5]);
			key.setItemID(itemID++);
			room.getTiles()[1][5].addItem(key);
			keyColors.remove(0);
		}
	}

	private void genLight(int i){
		Room room = game.getRoomsInGame().get(i);
		Light light = new Light(room.getTiles()[1][1]);
		light.setItemID(itemID++);
		room.getTiles()[1][1].addItem(light);

	}

}