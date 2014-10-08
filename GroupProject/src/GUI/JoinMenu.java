package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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



public class JoinMenu extends JFrame{

	private JPanel jpanel;
	private JButton submit;
	private theHandler handler;
	private JTextField textIP;
	private JTextField textName;
	private String name = null; // for the creation of a new player
	private GameClient gc;

	public JoinMenu(DrawingPanel panel, GameClient g) {
		gc = g;
		handler = new theHandler();
		submit = new JButton("Submit");
		submit.addActionListener(handler);
	}

	public void discard() {
		dispose();
	}

	public void setup() {

		new JFrame("The set up menu");
		this.setSize(400, 200);

		jpanel = new JPanel();
		jpanel.setLayout(new FlowLayout()); // change later
		jpanel.add(new JLabel("Please connect to a server and fill in an IP address"));

		jpanel.add(Box.createHorizontalStrut(50)); // a spacer

		this.add(jpanel);
		this.setAlwaysOnTop(true); // ensures it pops up in front
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// user inputs
		setTextField();
		jpanel.add(submit);
	}

	public void setTextField() {
		JLabel label1 = new JLabel("\nIP Address:");
		label1.setToolTipText("hit enter to finalise ip address input");
		textIP = new JTextField(20);
		textIP.getDocument().addDocumentListener(new MyDocumentListener());

		JLabel label2 = new JLabel("\nIn game name:");
		textName = new JTextField(20);
		textName.getDocument().addDocumentListener(new MyDocumentListener());

		jpanel.add(label1);
		jpanel.add(label2);
		jpanel.add(textIP);
		jpanel.add(textName);
	}

	/**
	 * This is an inner class which handles changes made in the text box for
	 * player name
	 */
	private class MyDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e) {
			name = textIP.getText();
		}

		public void removeUpdate(DocumentEvent e) {
			name = textIP.getText();
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

			//System.out.println("action listener");

			// responds to radiobuttons
			if ("ColonelMustard".equals(e.getActionCommand())) {

			}

			// responds to the final submit, and forwards new player info onto
			// the board
			else if (e.getSource() == submit) {
				if (name != null && name.length() > 0) { // valid
																			// inputs
					// reset
					name = null;
					textIP.setText("");


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