package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import networking.GameClient;
import networking.Player;


public class WindowFrame extends JFrame {
	private TopMenu topMenu;
	private DrawingPanel panel;


	public static DrawingPanel canvas;
	public static GameClient gameClient;
	public static Player player;


	public WindowFrame() {
		super("An Adventure Game");
		JFrameSetUp();
		JPanelStuff();

		this.addKeyListener(panel.getKeyB());
	}

	/**
	 * @author Daphne Wang
	 */
	public void JFrameSetUp() {
		topMenu = new TopMenu(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1280, 720);
		setMinimumSize(new Dimension(924, 520));
		setVisible(true); // make sure we are visible!
	}

	/**
	 * @author Daphne Wang
	 */
	public void JPanelStuff() {
		panel = new DrawingPanel(this);
		add(panel, BorderLayout.CENTER); // add canvas
		setVisible(true); // make sure we are visible!
		panel.repaint(); // repaint susses the graphics object
	}
}