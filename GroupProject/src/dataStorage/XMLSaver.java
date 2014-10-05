package dataStorage;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import gameLogic.Avatar;
import gameLogic.Floor;
import gameLogic.Game;
import gameLogic.Room;

/**
 * A class that acts as a shell for a save parser. Contains the saveGame() method
 * which creates the root element, parses all the rooms in the game and then creates
 * a new document using these elements which is outputted to a file.
 *
 *
 * @author caskeyanto
 *
 */
public class XMLSaver {

	Game game;

	public XMLSaver(Game game){
		this.game = game;
	}

	public boolean saveGame(){
		String fileName = "saved_at_" + System.currentTimeMillis();
		XMLSaveParser parser = new XMLSaveParser(this);


		Element root = new Element("Game");
		for(Room r : game.getRoomsInGame()){
			Element e = parser.parseRoom(r);
			if(e == null)return false;
			root.addContent(e);
		}

	    Document doc = new Document();
	    doc.setRootElement(root);
	    try {
	      XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
	      xmlOutput.output(doc, new FileOutputStream(fileName));
	    }
	    catch (IOException e) {
	      System.err.println(e);
	    }

		return true;
	}

}
