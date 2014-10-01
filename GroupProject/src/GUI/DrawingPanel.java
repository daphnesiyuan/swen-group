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

	private MyMouseListener mouse;
	private int mouseX;
	private int mouseY;
	private Handler handler;


	public DrawingPanel(WindowFrame win){
		wf = win;
		sm = new StartMenu( this );
		startMenu = true; //by default
		handler = new Handler();
		/* change the dw-later */
		//dw = new DrawWorld( null, this ); //param: the character, and then a panel

		mouse = new MyMouseListener(this);
		this.addMouseListener( mouse );
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

	public void sendClick(int x, int y){
		mouseX = x;
		mouseY = y;
		handler.mouseListener();
	}

	public String findButton(int x, int y){

		//panel.getWidth()/2 - (buttonWidth/2), panel.getHeight()/3 - buttonHeight/2 + (i*(panel.getHeight()/3)/2)
		int startW = (getWidth()/2 - (sm.getButtonWidth()/2));
		int startH = getHeight()/3 - sm.getButtonHeight()/2 + (-1*(getHeight()/3)/2);

		//System.out.println("x="+startW + " y="+startH);
		if ( x>=startW && x<=startW+sm.getButtonWidth() && y>startH && y<startH+sm.getButtonHeight() ){
			return "start";
		}

		return "";
	}


	/*
	 * Private inner class to handle action listeners - dealing with buttons
	 */

	private class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){

		}

		public void mouseListener(){
			if ( findButton( mouseX, mouseY ).equals("start") ){
				System.out.println("clicked start button");
				startMenu = false; //no longer in the start menu mode
				dw = new DrawWorld( null, DrawingPanel.this ); //param: the character, and then a panel
			}

			else{
				System.out.println("no active button");
			}
		}

	}
}
