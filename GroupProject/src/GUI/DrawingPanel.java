package GUI;

import java.awt.Graphics;

import javax.swing.JPanel;

import rendering.DrawWorld;

public class DrawingPanel extends JPanel{

	DrawWorld dw; //this draws all the game-stuff: locations chars items etc

	public DrawingPanel(  ){
		dw = new DrawWorld( null, this ); //param: the character, and then a panel
	}

	protected void paintComponent (Graphics g){
		dw.redraw(g, null, null, null); //param: graphics, room, char, direction
		
		
		//potential changes later: flag for menu mode or play mode, and to have logic 
	}
}
