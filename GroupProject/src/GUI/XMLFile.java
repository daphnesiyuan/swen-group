package GUI;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import networking.GameClient;
import networking.GameServer;

/**
 * Class to deal with the file chooser GUI for when you're trying to load a game in.
 * Uses methods from Antonia's XML classes to do so
 * @author Daphne Wang and Antonia Caskey
 *
 */
public class XMLFile{

	public static File getDefaultGame(WindowFrame w, DrawingPanel p, GameClient c, GameServer s){
		JFileChooser chooser = new JFileChooser();

		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
		chooser.setFileFilter(xmlfilter);
        chooser.setFileFilter(xmlfilter);
        chooser.setDialogTitle("Open XML file");


        int returnVal = chooser.showOpenDialog(w);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           System.out.println("You chose to open this file: " +
           chooser.getSelectedFile().getName());
           return chooser.getSelectedFile();
        }
        return null;
	}
}
