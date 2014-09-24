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
 * Basic chat room used to demonstrate the networking comparison between a client and server for those wanting to send something over the nwtwork.
 * @author veugeljame
 *
 */
public class ChatRoom implements ActionListener{

	// The client of this run that is talking to the server
	private ChatClient client;

	// Server if this chat client wants to start their own public server for everyone to connect to
	private ChatServer server;

	private int port = 32768;

	private JTextArea chatHistory;
	private JScrollPane scroll;
	private JTextField message;
	private JButton send;


	private JTextField IPConnection;
	private JLabel connectLabel;
	private JButton connect;
	private JButton startServer;
	private JTextField name;


	public ChatRoom(){

		// Set up a basic client for this ChatRoom
		// Tell this chat room to wait for input from the server that sends data to this client
		client = new ChatClient();

		// Set up interface
		setUpGui();

		// Loop forever
		while( true ){

			chatHistory.setText(client.getChatHistory());

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void setUpGui(){
		JFrame frame = new JFrame("Chat Room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(650,300);
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
		startServer = new JButton("Start Server");
		startServer.addActionListener(this);

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
		frame.getContentPane().add(startServer);
		frame.getContentPane().add(scroll);
		frame.getContentPane().add(message);
		frame.getContentPane().add(send);
		frame.setVisible(true);
	}

	/**
	 * Send a chat message to the server
	 * @param chatMessage text a user has entered to send to the everyone to view
	 * @return True if the message was sent and no exceptions were thrown
	 */
	private boolean sendMessageToServer(String chatMessage){

		try {

			// Send the chat message to the server
			client.sendData(chatMessage);
		} catch (IOException e) {

			// Could not send message to the server
			chatHistory.append("Could not send message!\n");
			e.printStackTrace();
			return false;
		}

		// Message sent successfully
		return true;
	}


	@Override
	public void actionPerformed(ActionEvent ae) {

		// Sends a message out to the client
		if( ae.getSource() == message || ae.getSource() == send ){

			// Attempt to send a message to the server
			if( !message.getText().equals("") && sendMessageToServer(message.getText()) ){
				message.setText("");
			}
		}
		else if( ae.getSource() == startServer ){


			if( startServer.getText().equals("Start Server")){

				// Attempt to send a message to the server
				if( server == null ){


					// Start a new server
					server = new ChatServer();

					// Attempt to connect to the server
					if( connectToServer(server.getIPAddress(), port) ){

						// Change button
						startServer.setText("Stop Server");

						// Don't allow us to connect to other servers
						connect.setEnabled(false);
					}
				}
			}
			else{

				// Stop the server
				server.stopServer();
				server = null;

				// Change button
				startServer.setText("Start Server");

				// Allow us to connect to other servers
				connect.setEnabled(true);
			}


		}
		else if( ae.getSource() == connect || ae.getSource() == IPConnection ){

			// Attempt to connect to the server
			connectToServer(IPConnection.getText(), port);
		}
		else if( ae.getSource () == name ){

			// Change the name of the client
			client.setName(name.getText());
		}
	}

	/**
	 * Attempt to connect to the server with the given ip and port
	 * @param ip IPAddress of server to connect to
	 * @param port Port
	 */
	public boolean connectToServer(String ip, int port){

		try {
			if( client.connect(ip, name.getText(), port) ){
				chatHistory.append("Connected to " + IPConnection.getText() + ":" + port + "\n");

				// Request chat history
				client.sendData("/get history");

				return true;
			}


		} catch (UnknownHostException e) {
			chatHistory.append("Could not connect to " + IPConnection.getText() + ":" + port + "\n");
			e.printStackTrace();

		} catch (IOException e) {
			chatHistory.append("Could not connect to " + IPConnection.getText() + ":" + port + "\n");
			e.printStackTrace();
		}

		// Could not connect
		return false;
	}

	public static void main(String[] args){
		new ChatRoom();
	}
}
