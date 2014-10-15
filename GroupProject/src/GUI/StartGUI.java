package GUI;

import gameLogic.Room;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import networking.GameClient;
import networking.GameServer;

/**
 * Class to deal with the GUI for when you start the game
 * It takes only a username for the player and all the IP address and networking is done behind the scene to prepare
 * a server ready for the user
 * @author Daphne Wang
 */
public class StartGUI extends JFrame {

	private JPanel jpanel;
	private JButton connectButton;
	private theHandler handler;
	private JTextField textName;
	private String name = null;
	private GameClient gc;
	private GameServer gs;
	private DrawingPanel panel;

	public StartGUI(DrawingPanel p, GameClient c, GameServer s) {
		gc = c;
		gs = s;
		handler = new theHandler();
		connectButton = new JButton("Connect");
		connectButton.addActionListener(handler);
		panel = p;

		getRootPane().setDefaultButton(connectButton); //line to hit enter which will submit automatically
	}

	/**
	 * Helper method to set up all the little things about the JFrame such as sizing
	 * buttons and layouts
	 *
	 * @author Daphne Wang
	 */
	public void setup() {
		new JFrame("The set up menu");
		this.setSize(400, 200);
		setResizable(false); // prevent us from being resizeable
		jpanel = new JPanel();
		jpanel.setLayout(new FlowLayout()); // change later
		jpanel.add(Box.createHorizontalStrut(30)); // a spacer
		jpanel.add(new JLabel("Please fill in a username"));

		this.add(jpanel);
		this.setAlwaysOnTop(true); // ensures it pops up in front
		this.setVisible(true);

		// user inputs
		setTextField();
		jpanel.add(connectButton);
	}

	/**
	 * Mini method to set all the text fields
	 * @author Daphne Wang
	 */
	public void setTextField() {
		JLabel label2 = new JLabel("\n\nUsername");
		textName = new JTextField(20);
		textName.getDocument().addDocumentListener(new MyDocumentListener());
		jpanel.add(Box.createHorizontalStrut(150)); // a spacer for alignment
		jpanel.add(label2);
		jpanel.add(textName);
	}

	/**
	 * Method which sets up the game play
	 * @author Daphne Wang
	 */
	public void startGame() {
		System.out.println("starting game");
		gc.setName(name);
		try {
			gc.connect(gs);

			Room temp = gc.getRoom();
			while (temp == null) {
				panel.setGameMode();
				temp = gc.getRoom();
			}
			dispose();
			panel.startDrawWorld();
			panel.setGameMode();

		} catch (UnknownHostException e1) {
			sendFailure();
		} catch (IOException e1) {
			sendFailure();
		}
	}

	/**
	 * This is an inner class which handles changes made in the text box for
	 * player name, whether the user is inputting or backspacing on the content
	 * @author Daphne Wang
	 */
	private class MyDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e) {
			name = textName.getText();

		}

		public void removeUpdate(DocumentEvent e) {
			name = textName.getText();
		}

		public void changedUpdate(DocumentEvent e) {
			// Plain text components do not fire these events
		}
	}

	/**
	 * Small GUI which deals with invalid inputs or failed server connection
	 * @author Daphne Wang
	 */
	public void sendFailure() {
		JFrame warning = new JFrame();
		JOptionPane.showMessageDialog(warning,
				"Error, try again!");
	}

	/**
	 * This is a private class that implements action listener This class can
	 * handle events in the set-up GUI It contains one method:
	 * actionPerformed(e)
	 *
	 * @author Daphne Wang
	 */

	private class theHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == connectButton) {
				if (name != null && name.length() > 0) { // input is valid

					startGame();

				} else {
					sendFailure();
				}
			}

		}

	}
}