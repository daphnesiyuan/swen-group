
package dataStorage;

import java.io.File;

import gameLogic.Game;

public class XMLLoader {

	XMLLoadParser xmlLoad;

	public XMLLoader(){}

	public Game loadDefault(){
		Game g = new Game(false);
		File file = new File("default.xml");
		xmlLoad = new XMLLoadParser(file);
		g = xmlLoad.loadGame();

		return g;
	}

	public Game loadGame(File file){
		Game g = new Game();
		xmlLoad = new XMLLoadParser(file);
		g = xmlLoad.loadGame();
		return g;
	}

}
