package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A method which deals with the top menu bar where you can choose to either save or quit the game
 * @author Daphne Wang
 *
 */
public class TopMenu implements ActionListener{

	boolean mouse = false;
	private JMenuBar menuBar;
	private WindowFrame Window;
	private DrawingPanel panel;


	private JMenuItem mainDropDown;
	private JMenuItem option;


	private JMenuItem saveOption;
	private JMenuItem quitOption;


	public TopMenu(WindowFrame w, DrawingPanel p){
		Window = w;
		panel = p;
		menuBar = new JMenuBar();

		setupMenuBar();
	}


	/**
	 * Action performed to respond to any interactions with the top menu
	 * @author Daphne Wang
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    if ((e.getActionCommand()).equals("Save")) {
	    	if( !panel.isStartMode() ){ //can only save if you're in game mode
	    		if(panel.getGameServer().saveGame()){
		    		System.out.println("game successfully saved");
		    	}
		    	else{
		    		System.out.println("Did not save properly");
		    	}	}
	    	else{ Window.sendFailure(); }

	    	mouse = true;
	    }
	    else if ( (e.getActionCommand()).equals("Quit") ){
	    	Window.revertToStartMenu();
	    }

	}

	/**
	 * Method to make any setups necessary for the top menu, such as options and inner drop down menus
	 * @author Daphne Wang
	 */
	public void setupMenuBar() {
		mainDropDown = new JMenu("File");
		option = new JMenu("Game");

		saveOption = new JMenuItem("Save");
		quitOption = new JMenuItem("Quit");

		mainDropDown.add(option);
		option.add(saveOption);
		option.add(quitOption);

		saveOption.addActionListener(this);
		quitOption.addActionListener(this);

		menuBar.add(mainDropDown);

		Window.setJMenuBar(menuBar);
	}


}
