package GUI;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import dataStorage.XMLLoader;

public class XMLFile{
	WindowFrame wf;
	JFileChooser chooser;

	public XMLFile(WindowFrame w){
		wf = w;
		System.out.println("in xml file constructor");


		chooser = new JFileChooser();
		//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
		chooser.setFileFilter(xmlfilter);
        chooser.setFileFilter(xmlfilter);
        chooser.setDialogTitle("Open XML file");


        int returnVal = chooser.showOpenDialog(w);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           System.out.println("You chose to open this file: " +
                chooser.getSelectedFile().getName());

           XMLLoader xml = new XMLLoader();
           xml.loadGame( chooser.getSelectedFile() );
        }
	}


}
