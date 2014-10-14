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


	public WindowFrame() {
		super("An Adventure Game");
		JFrameSetUp();
		JPanelStuff();

		this.addKeyListener(panel.getKeyB());
	}

	public void JFrameSetUp() {
		topMenu = new TopMenu(this, panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1280, 720);
		setMinimumSize(new Dimension(800, 530));
		setVisible(true); // make sure we are visible!
	}

	public void JPanelStuff() {
		panel = new DrawingPanel(this);
		add(panel, BorderLayout.CENTER); // add canvas
		setVisible(true); // make sure we are visible!
		panel.repaint(); // repaint susses the graphics object
	}
}