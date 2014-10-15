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

/**
 * Class which handles the GUI that appears when you want to join a game.
 * It takes A username, and IP address of the server you want to join
 * @author wangdaph
 *
 */

public class JoinGUI extends JFrame{

	private JPanel jpanel;
	private JButton connectButton;
	private theHandler handler;
	private JTextField textIP;
	private JTextField textName;
	private String ipAddress = null; // for the creation of a new player
	private String name = null;
	private GameClient gc;
	private DrawingPanel panel;

	public JoinGUI(DrawingPanel p, GameClient g) {
		gc = g;
		handler = new theHandler();
		connectButton = new JButton("Connect");
		connectButton.addActionListener(handler);
		panel = p;
	}

	public void setup() {
		new JFrame("The set up menu");
		this.setSize(400, 200);
		setResizable(false); // prevent us from being resizeable
		jpanel = new JPanel();
		jpanel.setLayout(new FlowLayout()); // change later
		jpanel.add(new JLabel("Please connect to a server and fill in an IP address"));

		jpanel.add(Box.createHorizontalStrut(28)); // a spacer

		this.add(jpanel);
		this.setAlwaysOnTop(true); // ensures it pops up in front
		this.setVisible(true);

		// user inputs
		setTextField();
		jpanel.add(connectButton);

		getRootPane().setDefaultButton(connectButton);
	}

	public void setTextField() {
		JLabel label1 = new JLabel("\nIP Address:");
		textIP = new JTextField(20);
		textIP.getDocument().addDocumentListener(new MyDocumentListener());
		JLabel label2 = new JLabel("\nUsername");
		textName = new JTextField(20);
		textName.getDocument().addDocumentListener(new MyDocumentListener());

		jpanel.add(label1);
		jpanel.add(textIP);
		jpanel.add(Box.createHorizontalStrut(35)); // a spacer for alignment
		jpanel.add(label2);
		jpanel.add(textName);
	}

	/**
	 * This is an inner class which handles changes made in the text box for
	 * player name and IP address
	 */
	private class MyDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e) {
			ipAddress = textIP.getText();
			name = textName.getText();

		}

		public void removeUpdate(DocumentEvent e) {
			ipAddress = textIP.getText();
			name = textName.getText();
		}

		public void changedUpdate(DocumentEvent e) {
			// Plain text components do not fire these events
		}
	}

	/**
	 * Helper method to start the game when connecting. This sorts out the drawing and launching the actual game
	 * This also deals with failures in case the IP address is invalid, then it will fire an error message
	 * which means you cannot proceed with the game until a valid IP address is inputted
	 *
	 * @author Daphne Wang
	 */
	public void startGame(){
		gc.setName(name);

		try {
			if( gc.connect( ipAddress ) ){

				Room temp = gc.getRoom();
				while( temp == null){

					if( !panel.getGameClient().isConnected() ){
						sendFailure("The server is full, you cannot join this game");
						return;
					}

					temp = gc.getRoom();
				}
				dispose();

				panel.startDrawWorld();
				panel.setGameMode();
			}

		} catch (UnknownHostException e1) {
			sendFailure("Cannot find server");

		} catch (IOException e1) {
			sendFailure("Please input valid details!");
		}
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
				if (ipAddress != null && ipAddress.length() > 0 && name != null && name.length() > 0) { //input is valid
					//MUST ADD NWEN CONNECT STUFF
					startGame();

				} else {
					JoinGUI.this.sendFailure("Input valid details");
				}
			}
		}
	}

	/**
	 * A mini method which deals with the GUI for invalid details during registration
	 * @author Daphne Wang
	 */

	public void sendFailure(String message ) {
		JFrame warning = new JFrame();
		JOptionPane.showMessageDialog(warning,
				message);
	}
}