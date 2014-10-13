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



public class StartGUI extends JFrame{

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
		gs=s;
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
		jpanel.add(Box.createHorizontalStrut(30)); // a spacer
		jpanel.add(new JLabel("Please fill in a username"));



		this.add(jpanel);
		this.setAlwaysOnTop(true); // ensures it pops up in front
		this.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// user inputs
		setTextField();
		jpanel.add(connectButton);

	}

	public void setTextField() {
		JLabel label2 = new JLabel("\n\nUsername");
		textName = new JTextField(20);
		textName.getDocument().addDocumentListener(new MyDocumentListener());
		jpanel.add(Box.createHorizontalStrut(150)); // a spacer for alignment
		jpanel.add(label2);
		jpanel.add(textName);
	}

	/**
	 * This is an inner class which handles changes made in the text box for
	 * player name and IP address
	 */
	private class MyDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e) {
			name = textName.getText();

		}

		public void removeUpdate(DocumentEvent e) {
			name = textName.getText();
			System.out.println("name="+name);
		}

		public void changedUpdate(DocumentEvent e) {
			// Plain text components do not fire these events
		}
	}

	/**
	 * This is a private class that implements action listener This class can
	 * handle events in the set-up GUI It contains one method:
	 * actionPerformed(e)
	 *
	 * @author Daphne
	 */

	private class theHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == connectButton) {
				if ( name != null && name.length() > 0 ) { //input is valid

					gc.setName(name);
					try {
						gc.connect(gs);

						Room temp = gc.getRoom();
						while( temp == null){
							panel.setGameMode();
							temp = gc.getRoom();
						}


						dispose();
						panel.startDrawWorld();
						panel.setGameMode();
						System.out.println("clicked connect");


					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else {
					sendFailure();
				}
			}

		}

		public void sendFailure() {
			JFrame warning = new JFrame();
			JOptionPane.showMessageDialog(warning,
					"Please input valid details!");
		}


	}
}