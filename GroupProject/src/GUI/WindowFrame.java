package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import networking.GameClient;
import networking.Player;


public class WindowFrame extends JFrame {
	private TopMenu topMenu;
	private DrawingPanel panel;


	public WindowFrame() {
		super("An Adventure Game");

		panel = new DrawingPanel(this);
		JFrameSetUp();
		JPanelStuff();
		closingWarningBox();

		this.addKeyListener(panel.getKeyB());
	}

	public void JFrameSetUp() {
		topMenu = new TopMenu(this, panel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setSize(1280, 720);
		setMinimumSize(new Dimension(800, 530));
		setVisible(true); // make sure we are visible!
	}

	public void JPanelStuff() {
		add(panel, BorderLayout.CENTER); // add canvas
		setVisible(true); // make sure we are visible!
		panel.repaint(); // repaint susses the graphics object
	}

	public void closingWarningBox(){
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit the game?", "Warning",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
	}


	public void revertToStartMenu(){
		int confirmed = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to exit the game?", "Warning",
				JOptionPane.YES_NO_OPTION);

		if (confirmed == JOptionPane.YES_OPTION) {
			panel.setStartMode();
		}
	}

	public void sendFailure() {
		JFrame warning = new JFrame();
		JOptionPane.showMessageDialog(warning,
				"You can only save while in game");
	}
}