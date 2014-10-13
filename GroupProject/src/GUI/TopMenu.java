package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TopMenu implements ActionListener{

	boolean mouse = false;
	private JMenuBar menuBar;
	private WindowFrame Window;
	private DrawingPanel panel;


	private JMenuItem mainDropDown;
	private JMenuItem innerCatagory;
	private JMenuItem option;


	private JMenuItem option1a;
	private JMenuItem option1b;


	public TopMenu(WindowFrame w, DrawingPanel p){
		Window = w;
		panel = p;
		menuBar = new JMenuBar();

		setupMenuBar();
	}


	/**
	 * Action performed to respond to any interactions with the top menu
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    if ((e.getActionCommand()).equals("Save")) {
	    	System.out.println("do this ok");
	    	if(panel.getGameServer().saveGame()){
	    		System.out.println("game successfully saved");
	    	}
	    	else{
	    		System.out.println("YOU FUCKED UP THE SAVING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
	    	}

	    	mouse = true;
	    }
	    else if ( (e.getActionCommand()).equals("Do that") ){
	    	System.out.println("do that ok");
	    }

	}

	/**
	 * Method to make any setups necessary for the top menu, such as options and inner menus
	 */
	public void setupMenuBar() {
		mainDropDown = new JMenu("File");
		option = new JMenu("Some option");

		option1a = new JMenuItem("Save");
		option1b = new JMenuItem("Do that");

		mainDropDown.add(option);
		option.add(option1a);
		option.add(option1b);

		option1a.addActionListener(this);
		option1b.addActionListener(this);

		menuBar.add(mainDropDown);

		Window.setJMenuBar(menuBar);
	}


}
