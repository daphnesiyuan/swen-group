package gameLogic;

import java.io.Serializable;
import java.util.List;

public class Environment extends Thread implements Serializable{


	private static final long serialVersionUID = 388911180114116326L;

	private Game game;
	private List<Light> lights;

	private int count = 0;


	public Environment(Game game){
		this.game = game;

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
		Key key = new Key(room.getTiles()[1][5]);
		room.getTiles()[1][5].addItem(key);

	}

	private void genLight(int i){
		Room room = game.getRoomsInGame().get(i);
		Light light = new Light(room.getTiles()[1][1]);
		room.getTiles()[1][1].addItem(light);

	}

}