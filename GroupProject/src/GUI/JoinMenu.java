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
	private JButton connectButton;
	private theHandler handler;
	private JTextField textIP;
	private JTextField textName;
	private String ipAddress = null; // for the creation of a new player
	private String name = null;
	private GameClient gc;

	public JoinMenu(DrawingPanel panel, GameClient g) {
		gc = g;
		handler = new theHandler();
		connectButton = new JButton("Connect");
		connectButton.addActionListener(handler);
	}

	public void discard() {
		dispose();
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// user inputs
		setTextField();
		jpanel.add(connectButton);
	}

	public void setTextField() {
		JLabel label1 = new JLabel("\nIP Address:");
		//label1.setToolTipText("hit enter to finalise ip address input");
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
			System.out.println("ip address="+ipAddress+" name="+name);
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
				if (ipAddress != null && ipAddress.length() > 0 && name != null && name.length() > 0 ) { //input is valid

					ipAddress = null;
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