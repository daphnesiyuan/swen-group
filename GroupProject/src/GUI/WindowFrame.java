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

import networking.GameClient;
import networking.Player;
import rendering.Rendering;

public class WindowFrame extends JFrame {
	private TopMenu topMenu;
	private DrawingPanel panel;
	private Graphics2D graphics; // for leon later?
	// private Rendering render=new Rendering(); //Leon's object
	// private Direction: n s e w


	public static DrawingPanel canvas;
	public static GameClient gameClient;
	public static Player player;

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
		setResizable(false); // prevent us from being resizeable
		setVisible(true); // make sure we are visible!
	}

	/*
	 * public void refreshGraphics(){ //render.redraw(graphics, room, character,
	 * direction); //TO BE FILLED LATER: we get room and char from client: jimmy
	 * }
	 */
	public void JPanelStuff() {
		System.out.println("In JPanel setups");
		panel = new DrawingPanel(this);
		add(panel, BorderLayout.CENTER); // add canvas
		setVisible(true); // make sure we are visible!
		panel.repaint(); // repaint susses the graphics object
	}
}