package networking;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;


public class Server implements Runnable{
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

	private String IPAddress;
	private int port = 32768;

	public Server(){

		try {
			IPAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Start listening for clients!
		Thread myThread = new Thread(this);
		myThread.start();


		// Server listens for input directly to servers terminal
		Thread serverTextBox = new Thread(new ServerTextListener());
		serverTextBox.start();

	}

	/**
	 * Clients interact with server
	 */
	public void run(){

		ServerSocket serverSocket = null;

		try{

			serverSocket = new ServerSocket(port);
			System.out.print("Servers IP: " + IPAddress + "\n");
			System.out.print("Waiting for clients...\n");


			// Keep the server constantly running
			while(true){

				// Check if the server socket is valid
				if( serverSocket == null || serverSocket.isClosed() ){
					System.out.print("Connection for the server has been closed!.\n");
				}

				// Someone connects to the server
				Socket clientSocket = serverSocket.accept();

				// Wait for their public name to be sent through
				ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
				String name = (String)input.readObject();

				// Create a thread for each client
				ClientThread cl = new ClientThread(clientSocket, clientSocket.getInetAddress().getHostAddress(), name);
				clients.add(cl);
				cl.start();

				// See if this is a new client
				if( clients.contains( cl ) ){
					System.out.print(cl.name + " has connected.\n");
				}
			}
		}
		catch(Exception e){
			System.out.print("\n====\n" + e.getMessage() + "\n====\n");
			e.printStackTrace();
		}
		finally{
			System.out.print("\n====\n SERVER CLOSING SOCKET \n====\n");
			if ( serverSocket != null ){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Removes the client at the given location from our list of clients
	 * @param i index to remove a client from
	 */
	private synchronized void removeClient(int i ){

		ClientThread c =  clients.remove(i);

		try {
			c.socket.close();

			// Check if this client has disconnected
			if( !clients.contains(c) ){
				processMessage(c.name + " has Disconnected.\n");
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method that is run once the server enters a message in console
	 * @param message
	 */
	private synchronized void processServerMessage(String message){

		// Process to everyone
		processMessage("Host: " + message);
	}

	/**
	 * Send the given message to every client connected to the server as well as to the server
	 * @param message Message to send to every client
	 */
	public synchronized void processMessage(String message){

		// Display message to server if the host didn't send the message
		if( message.charAt(0) == '>'){
			System.out.print(message + "\n");
		}

		// Send it to all our clients
		for( int i = 0; i < clients.size(); i++ ){
			try {

				// Check if client is connected
				if( clients.get(i).socket.isClosed() || clients.get(i).socket.isOutputShutdown() || !clients.get(i).socket.isConnected()){
					removeClient(i--);
					continue;
				}

				// Valid connection
				// Send text to this client
				ObjectOutputStream out = new ObjectOutputStream(clients.get(i).socket.getOutputStream());

				//System.out.print("Sending '" + message + "' to " + clients.get(i).name + "\n");
				out.flush();
				out.writeObject(message);
				out.flush();
			} catch (IOException e) {


				if( e.getMessage().equals("Broken pipe") ){
					System.out.print("Handling broken pipe connection\n");
					removeClient(i--);
				}
			}
		}
	}





	public static void main(String[] args){

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){

				// Start Server
				new Server();
			}
		});

	}

	/**
	 * A class that listens for input from the console and sends the input to processServerMessage
	 * @author veugeljame
	 *
	 */
	private class ServerTextListener implements Runnable{

		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String text;

			while( true ){
				try {
					text = (String)br.readLine();

					if( text != null ){
						processServerMessage(text);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


	}

	/**
	 *
	 * @author veugeljame
	 *
	 */
	private class ClientThread extends Thread {

		final String IPAddress;
		final Socket socket;
		final String name;

		public ClientThread(Socket socket, String IPAddress, String name){
			this.socket = socket;
			this.name = name;
			this.IPAddress = IPAddress;
		}


		public void run() {

			while( true ){

				// Receive Input
				try {

					// Get Message
					ObjectInputStream input;
					try{
						input = new ObjectInputStream(socket.getInputStream());
					}catch(IOException e){ continue; }

					// Tell the server to process it
					String text = "> " + name + ": " + (String)input.readObject();
					processMessage(text);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}


		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((IPAddress == null) ? 0 : IPAddress.hashCode());
			return result;
		}


		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ClientThread))
				return false;
			ClientThread other = (ClientThread) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (IPAddress == null) {
				if (other.IPAddress != null)
					return false;
			} else if (!IPAddress.equals(other.IPAddress))
				return false;
			return true;
		}


		private Server getOuterType() {
			return Server.this;
		}



	}


	public String getIPAddress() {
		return IPAddress;
	}

	public int getPort() {
		return port;
	}
}
