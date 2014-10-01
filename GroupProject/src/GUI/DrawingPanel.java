package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import rendering.DrawWorld;

public class DrawingPanel extends JPanel{

	private DrawWorld dw; //this draws all the game-stuff: locations chars items etc
	private StartMenu sm;
	private WindowFrame wf;
	public static boolean startMenu; //play or menu mode flag


	public DrawingPanel(WindowFrame win){
		wf = win;
		dw = new DrawWorld( null, this ); //param: the character, and then a panel
		sm = new StartMenu( this );
		startMenu = true; //by default


	}

	@Override
	protected void paintComponent (Graphics g){

		if( startMenu ){
			System.out.println("start menu mode, ready to draw");
			sm.redraw(g);
		}

		else{ //else it is in game
			dw.redraw(g, null, null); //param: graphics, room, char, direction
			//potential changes later: flag for menu mode or play mode, and to have logic

			System.out.println("in game");
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1280, 720);
		return dimension;
	}


	/*
	 * Private inner class to handle action listeners - dealing with buttons
	 */

	private class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){



		}
	}
}
