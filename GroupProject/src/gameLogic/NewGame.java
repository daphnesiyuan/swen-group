package gameLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;



/**
 * NewGame class is used when starting a new game.
 * Room data arguments must be provided when constructing a new NewGame object, so that the class can create and populate game attributes, including rooms, tiles, characters and items.
 *
 * @author Ryan Griffin
 */

public class NewGame {
	private List<Room> roomsInGame;
	private List<Avatar> activeCharacters;
	private List<Floor> spawnTiles;

	private Room startRoom;

	private boolean testing;

	private Game game;

	public NewGame(Game g){
		game = g;


		roomsInGame = new ArrayList<Room>();
		spawnTiles = new ArrayList<Floor>();

		activeCharacters = createCharacters();

		game.setRoomsInGame(roomsInGame);
		game.setActiveCharacters(activeCharacters);


	}


	private List<Avatar> createCharacters() {

		List<String> clientStrings = new ArrayList<String>();
		clientStrings.add("Ryan Griffin");

		List<Avatar> characters = new ArrayList<Avatar>();

		for(String s : clientStrings){
			Floor spawnXY = spawnXY();
			Avatar player = new Avatar(s,spawnXY,game);
			characters.add(player);
		}

		return characters;

	}


	private Floor spawnXY(){
		if(spawnTiles.size()<=0)return null;

		Floor spawnTile =  spawnTiles.get(0);
		spawnTiles.remove(0);
		Collections.shuffle(spawnTiles);
		return spawnTile;

	}




}
