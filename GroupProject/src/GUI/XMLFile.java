package GUI;

import gameLogic.Game;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import dataStorage.XMLLoader;

/**
 * Class to deal with the file chooser GUI for when you're trying to load a game in.
 * Uses methods from Antonia's XML classes to do so
 * @author Daphne Wang and Antonia Caskey
 *
 */
public class XMLFile{
	WindowFrame wf;
	JFileChooser chooser;

	public XMLFile(WindowFrame w){
		wf = w;
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
        }
	}


}
