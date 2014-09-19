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
import java.util.Calendar;
import java.util.Scanner;


public class Server implements Runnable{
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

	private String IPAddress;
	private int port = 32768;

	private boolean running = true;

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
			while(running){

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



				// See if this is a new client
				if( !clients.contains( cl ) ){

					// Tell everyone the new client has joined the server
					processServerMessage(cl.getName() + " has Connected.\n");
				}

				// Add the client to our list
				clients.add(cl);
				cl.start();
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
				processData(createNetworkObject(c.getName() + " has Disconnected.\n"));
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new object of data to be sent through the network
	 * @param data Object to send to all clients
	 * @return new Network Object containing the current date/time and servers IP
	 */
	private NetworkObject createNetworkObject(Object data){
		return new NetworkObject("Host", Calendar.getInstance(), data);
	}

	/**
	 * The method that is run once the server enters a message in console
	 * @param message
	 */
	private synchronized void processServerMessage(String message){

		// Process to everyone
		processData(createNetworkObject(message));
	}

	private synchronized boolean processCommand(String message){

		Scanner scan = new Scanner(message);

		return new CommandParser().parseCommand(scan);
	}

	/**
	 * Send the given message to every client connected to the server as well as to the server
	 * @param message Message to send to every client
	 */
	private synchronized void processData(NetworkObject data){

		// Check for a command
		if( processCommand((String)data.getData())){
			return;
		}

		// Display for the server in console
		System.out.println(data);

		// Send it to all our clients
		for( int i = 0; i < clients.size(); i++ ){
			try {

				// Valid connection
				// Send text to this client
				ObjectOutputStream out = new ObjectOutputStream(clients.get(i).socket.getOutputStream());

				//System.out.print("Sending '" + message + "' to " + clients.get(i).name + "\n");
				out.flush();
				out.writeObject(data);
				out.flush();
			} catch (IOException e) {

				// More broken clients
				if( e.getMessage().equals("Broken pipe") ){
					System.out.print("Handling broken pipe connection\n");
					removeClient(i--);
				}
			}
		}
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
	 *	Provides a running thread that will continuously check for incoming data from a Client and send it to the server for processing
	 * @author veugeljame
	 *
	 */
	private class ClientThread extends Thread {

		final String IPAddress;
		final Socket socket;
		public String name;

		public ClientThread(Socket socket, String IPAddress, String name){
			this.socket = socket;
			this.setName(name);
			this.IPAddress = IPAddress;
		}


		/**
		 * Wait for data from the client and
		 */
		public void run() {

			while( running ){

				// Receive Input
				try {

					// Get Message
					ObjectInputStream input;
					try{
						input = new ObjectInputStream(socket.getInputStream());
					}catch(IOException e){ continue; }

					// Get the data from what was sent through the network
					NetworkObject data = (NetworkObject)input.readObject();

					processData(data);

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

	/**
	 * Gets the IPAddress of the server for others to connect to
	 * @return String containing IPAddress
	 */
	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * Gets the port that the server is currently running off
	 * @return int containing the Port that the server is running off
	 */
	public int getPort() {
		return port;
	}

	public static void main(String[] args){
		new Server();
	}

	private class CommandParser{

		public boolean parseCommand(Scanner scan){


			if( !scan.hasNext()){
				return false;
			}


			String command = scan.next();

			// Set something
			if( command.equals("set") ){

				return parseSet(scan);
			}

			// Unknown command
			return false;
		}

		private boolean parseSet(Scanner scan){

			// Check for name
			if( !scan.hasNext() ){
				return false;
			}

			// Get next command that SHOULD be a name
			ClientThread client = parseClient(scan.next());

			// Check for failed client check
			if( client == null ){
				return false;
			}

			// Check for next name
			if( !scan.hasNext() ){
				return false;
			}

			// Name change
			if( scan.hasNext("name") ){

				scan.next();
				// Get the name they want to assign their name to
				String newName = scan.next();

				// set "name" name worked
				processData(createNetworkObject(client.getName() + " has changed their name to " + newName));

				client.setName(newName);

				return true;
			}

			return false;
		}

		private ClientThread parseClient(String name){
			for( int i = 0; i < clients.size(); i++ ){

				// Someone has this name
				if( clients.get(i).getName().equals(name) ){

					return clients.get(i);
				}
			}
			return null;
		}
	}

	/**
	 * Stops the server from running
	 */
	public void stop() {
		running = false;
	}
}
