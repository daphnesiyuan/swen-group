package networking;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private ChatRoomServer server;

	private int port = 32768;

	private JPanel chatHistory;
	private JScrollPane scroll;
	private JTextField message;
	private JButton send;

	private String publicIP = "173.255.249.21";

	private JTextField IPConnection;
	private JLabel connectLabel;
	private JButton connect;
	private JButton startServer;


	public ChatRoom(){

		// Set up a basic client for this ChatRoom
		// Tell this chat room to wait for input from the server that sends data to this client
		client = new ChatClient();

		// Set up interface
		setUpGui();

		// Loop forever
		while( true ){

			// Update our text if anything has changed
			refreshChatHistoryPanel();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Refreshes the panel to display all the chat messages accordingly
	 */
	private void refreshChatHistoryPanel(){

		// Remove all the panels
		chatHistory.removeAll();

		// Get the chat history
		ArrayList<ChatMessage> history = client.getChatHistory();

		for(ChatMessage cm : history ){
			JLabel label = new JLabel(cm.toString());
			label.setForeground(cm.color);
			chatHistory.add(label);
		}


		chatHistory.revalidate();

		// Scroll to bottom of the page
		int height = (int)chatHistory.getPreferredSize().getHeight();
        scroll.getVerticalScrollBar().setValue(height);

        // Reset the panel
        chatHistory.repaint();
	}

	private void setUpGui(){
		JFrame frame = new JFrame("Chat Room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(650,300);
		frame.setLayout(new FlowLayout());

		JLabel yourIP = new JLabel("Your IP: " + client.getClientIPAddress() );
		IPConnection = new JTextField(publicIP);
		IPConnection.setPreferredSize(new Dimension(110,25));
		IPConnection.addActionListener(this);
		try {
			client.setName( InetAddress.getLocalHost().getHostName() );
		} catch (UnknownHostException e) {e.printStackTrace();}


		connectLabel = new JLabel("Connect To: ");
		connect = new JButton("Connect");
		connect.addActionListener(this);
		startServer = new JButton("Start Server");
		startServer.addActionListener(this);

		chatHistory = new JPanel();
		chatHistory.setFocusable(false);
		chatHistory.setBackground(Color.white);
		chatHistory.setLayout(new BoxLayout(chatHistory, BoxLayout.Y_AXIS));
		scroll = new JScrollPane(chatHistory);
		scroll.setPreferredSize(new Dimension(500,200));
		message = new JTextField(15);
		message.addActionListener(this);
		send = new JButton("Send");
		send.addActionListener(this);

		frame.getContentPane().add(yourIP);
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
			client.appendWarningMessage("Could not send message!\n");
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
					server = new ChatRoomServer();

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
	}

	/**
	 * Attempt to connect to the server with the given ip and port
	 * @param ip IPAddress of server to connect to
	 * @param port Port
	 */
	public boolean connectToServer(String ip, int port){

		try {
			if( client.connect(ip, client.getName(), port) ){
				chatHistory.removeAll();
				client.appendWarningMessage("Connected to " + IPConnection.getText() + ":" + port);

				return true;
			}
			else{
				client.appendWarningMessage("Could not connect to " + IPConnection.getText() + ":" + port);
			}


		} catch (UnknownHostException e) {
			client.appendWarningMessage("Could not connect to " + IPConnection.getText() + ":" + port);
			e.printStackTrace();

		} catch (IOException e) {
			client.appendWarningMessage("Could not connect to " + IPConnection.getText() + ":" + port);
			e.printStackTrace();
		}


		// Could not connect
		return false;
	}

	public static void main(String[] args){
		new ChatRoom();
	}
}
