package networking;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * Basic chat room used to demonstrate the networking comparison between a client and server
 * @author veugeljame
 *
 */
public class ChatRoom implements ActionListener, ClientListener{

	// The client of this run that is talking to the server
	private Client client;


	private int port = 32768;

	private JTextArea chatHistory;
	private JScrollPane scroll;
	private JTextField message;
	private JButton send;


	private JTextField IPConnection;
	private JLabel connectLabel;
	private JButton connect;
	private JTextField name;


	public ChatRoom(){

		client = new Client(this);
		setUpGui();
	}

	private void setUpGui(){
		JFrame frame = new JFrame("Chat Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,320);
		frame.setLayout(new FlowLayout());

		JLabel yourIP = new JLabel("Your IP: " + client.getClientIPAddress() );
		IPConnection = new JTextField(client.getClientIPAddress());
		IPConnection.setPreferredSize(new Dimension(110,25));
		IPConnection.addActionListener(this);
		name = new JTextField( "" );
		name.addActionListener(this);
		try {
			name.setText( InetAddress.getLocalHost().getHostName() );
			client.setName(name.getText() );

		} catch (UnknownHostException e) {e.printStackTrace();}


		connectLabel = new JLabel("Connect To: ");
		connect = new JButton("Connect");
		connect.addActionListener(this);

		chatHistory = new JTextArea(15,15);
		chatHistory.setEditable(false);
		chatHistory.setLineWrap(true);
		scroll = new JScrollPane(chatHistory);
		scroll.setPreferredSize(new Dimension(500,200));
		message = new JTextField(15);
		message.addActionListener(this);
		send = new JButton("Send");
		send.addActionListener(this);

		frame.getContentPane().add(yourIP);
		frame.getContentPane().add(new JLabel("Name: "));
		frame.getContentPane().add(name);
		frame.getContentPane().add(connectLabel);
		frame.getContentPane().add(IPConnection);
		frame.getContentPane().add(connect);
		frame.getContentPane().add(scroll);
		frame.getContentPane().add(message);
		frame.getContentPane().add(send);
		frame.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent ae) {
		if( ae.getSource() == message || ae.getSource() == send ){

			// Sends a message out to the client
			String text = message.getText();
			client.sendObject(text);

			// Changes the text field to null
			message.setText("");
		}
		else if( ae.getSource() == connect || ae.getSource() == IPConnection ){
			try {
				// Attempt to connect to the server
				if( client.connect(IPConnection.getText(), port) ){
					chatHistory.append("Connected to " + IPConnection.getText() + ":" + port + "\n");
				}


			} catch (UnknownHostException e) {
				chatHistory.append("Could not connect to " + IPConnection.getText() + ":" + port + "\n");
				e.printStackTrace();
			} catch (IOException e) {
				chatHistory.append("Could not connect to " + IPConnection.getText() + ":" + port + "\n");
				e.printStackTrace();
			}

		}
		else if( ae.getSource () == name ){
			client.setName(name.getText());
			chatHistory.append("Your Name changed to " + client.getName() + "\n");
		}
	}




	@Override
	public synchronized void retrieveObject(Object object) {

		// We know we are sending text over the net
		// Cast to string to get the message
		String text = (String)object;

		// Update our history with the new text
		chatHistory.append(text + "\n");
		scroll.getVerticalScrollBar().setValue(scroll.getMaximumSize().height);

	}

	public static void main(String[] args){
		new ChatRoom();
	}
}
