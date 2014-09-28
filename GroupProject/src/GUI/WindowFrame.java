package GUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowFrame extends JFrame {

	private TopMenu topMenu;
	private DrawingPanel panel;
	private Graphics2D graphics; //for leon later?
	//private Rendering render=new Rendering(); //Leon's object
	//private Direction: n s e w

	public WindowFrame() {
		super("Game title");
		JFrameSetUp();
		JPanelStuff();
	}

	public void JFrameSetUp() {
		topMenu = new TopMenu(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1000, 700);

		setResizable(true); // prevent us from being resizeable
		setVisible(true); // make sure we are visible!

	}

/*	public void refreshGraphics(){
		//render.redraw(graphics, room, character, direction); //TO BE FILLED LATER: we get room and char from client: jimmy
	}*/

	public void JPanelStuff() {
		System.out.println("In JPanel setups");

		panel = new DrawingPanel(this);

		add(panel, BorderLayout.CENTER); // add canvas

		setVisible(true); // make sure we are visible!


		setButton(startButton);
		setButton(loadButton);

		//startButton.setRolloverEnabled(true);
		//startButton.setRolloverIcon(new RolloverIcon(start));
	}

	public void setButton(JButton button){
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setLocation(1000, 500);
		button.addActionListener(handler);


		startPanel.add(button);
		startPanel.validate();

	}

	/*
	 * Private inner class to handle action listeners - dealing with buttons
	 */

	private class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){

			if(e.getSource()==startButton){

				System.out.println("Pressed start button");
			}

		}
		//panel.repaint(); //repaint susses the graphics object
	}


}
