package GUI;

import java.io.IOException;
import java.net.UnknownHostException;

import gameLogic.Game;
import gameLogic.Room;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import networking.GameClient;
import networking.GameServer;
import dataStorage.XMLLoader;

/**
 * Class to deal with the file chooser GUI for when you're trying to load a game in.
 * Uses methods from Antonia's XML classes to do so
 * @author Daphne Wang and Antonia Caskey
 *
 */
public class XMLFile{
	WindowFrame wf;
	DrawingPanel panel;
	JFileChooser chooser;
	GameClient gc;
	GameServer gs;

	public XMLFile(WindowFrame w, DrawingPanel p, GameClient c, GameServer s){
		wf = w;
		panel = p;
		gc=c;
		gs=s;
		chooser = new JFileChooser();

		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
		chooser.setFileFilter(xmlfilter);
        chooser.setFileFilter(xmlfilter);
        chooser.setDialogTitle("Open XML file");


        int returnVal = chooser.showOpenDialog(w);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           System.out.println("You chose to open this file: " +
                chooser.getSelectedFile().getName());

           XMLLoader xml = new XMLLoader();
          Game game = xml.loadGame( chooser.getSelectedFile() );

          startGame();
        }
	}

	/**
	 * Method which sets up the game play
	 * @author Daphne Wang
	 */
	public void startGame() {
		System.out.println("starting game");
		panel.getGameClient().setName("reload");
		try {
			panel.getGameClient().connect(gs);

			Room temp = panel.getGameClient().getRoom();
			while (temp == null) {
				panel.setGameMode();
				temp = panel.getGameClient().getRoom();
			}
			panel.startDrawWorld();
			panel.setGameMode();

		} catch (UnknownHostException e1) {
			sendFailure();
		} catch (IOException e1) {
			sendFailure();
		}
	}

	/**
	 * Small GUI which deals with invalid inputs or failed server connection
	 * @author Daphne Wang
	 */
	public void sendFailure() {
		JFrame warning = new JFrame();
		JOptionPane.showMessageDialog(warning,
				"Error, try again!");
	}

}
