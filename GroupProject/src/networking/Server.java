package networking;

import java.awt.Color;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *Basic abstract server class that contains the required features of a server alogn with features of admins, pinging and more.
 * @author veugeljame
 *
 */
public abstract class Server implements Runnable{

	protected ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private ArrayList<String> admins = new ArrayList<String>(); // List of admin
																// IP Addresses

	// Pings from IP to time sent
	private HashMap<String, Calendar> pings = new HashMap<String, Calendar>();

	protected String IPAddress;
	private int port = 32768;
	private ServerSocket serverSocket;

	protected Server() {

		try {
			IPAddress = InetAddress.getLocalHost().getHostAddress();

			// Save the localhost as an admin
			admins.add(IPAddress);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Start listening for clients!
		Thread myThread = new Thread(this);
		myThread.start();

		// Check for dead clients
		/*Thread checkClients = new Thread(){

			final long MAX_DISCONNECT_TIME = 5000;

			@Override
			public void run(){

				while( true ){

					ArrayList<String> dead = new ArrayList<String>();
					long currentMillis = Calendar.getInstance().getTimeInMillis();

					// Check for last pings
					for( String IP : pings.keySet() ){
						Calendar date = pings.get(IP);
						if( ( date.getTimeInMillis() + MAX_DISCONNECT_TIME ) > currentMillis ){
							dead.add(IP);
						}
					}

					while( !dead.isEmpty() ){
						removeClient(getClientFromIP(dead.remove(0)));
					}

					try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		checkClients.start();*/

		// What happens when shut down
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				System.out.println("SHUT DOWN BITCH");
				stopServer();
			}
		});
	}

	/**
	 * Clients interact with server
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.print("Servers IP: " + IPAddress + " : " + port + "\n");
			System.out.print("Waiting for clients...\n");

			// Keep the server constantly running
			while (serverSocket != null && !serverSocket.isClosed()) {

				// Someone connects to the server
				Socket clientSocket = serverSocket.accept();

				// Wait for their public name to be sent through
				ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
				String name = (String) input.readObject();

				// Create a thread for each client
				ClientThread cl = new ClientThread(clientSocket, new Player(clientSocket.getInetAddress().getHostAddress(), name));

				// See if this is a new client
				if (!clients.contains(cl)) {

					// Tell our sub server that we have a new clinet
					newClientConnection(cl);
				}
				else{
					// Remove the old connection
					removeClient(cl,true);

					// We have rejoined
					clientRejoins(cl);
				}


				// Add the client to our list
				clients.add(cl);
				cl.start();
			}
		}  catch ( SocketException e ){

		} catch (Exception e) {
			System.out.print("\n====\n" + e.getMessage() + "\n====\n");
			e.printStackTrace();
		}finally {
			// Close our clients
			while( !clients.isEmpty() ){
				removeClient(clients.get(0),false);
			}
		}
		System.out.print("WARNING: The Server has been closed\n");
	}

	/**
	 * Removes the client at the given location from our list of clients
	 *
	 * @param client
	 *            index to remove a client from
	 * @param reconnecting
	 */
	private synchronized void removeClient(ClientThread client, boolean reconnecting) {

		int index = clients.indexOf(client);
		ClientThread c = clients.remove(index);
		c.stopClient();

		// Check if this client has disconnected
		if ( !reconnecting ) {
			sendToAllClients(new ChatMessage("~Admin",c.getPlayerName() + " has Disconnected.", Color.black, true), client);
			System.out.println(c.getPlayerName() + " has Disconnected.");
		}
	}

	/**
	 * Server was pinged by a client
	 *
	 * @param clientIP
	 *            IP of who wants the history sent to them
	 */
	protected synchronized long pinged(NetworkObject data) {

		Calendar currentTime = Calendar.getInstance();
		long delay = currentTime.getTimeInMillis()
				- data.getCalendar().getTimeInMillis();

		// TODO Make faster
		ClientThread client = null;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getIPAddress().equals(data.getIPAddress())) {
				client = clients.get(i);
				break;
			}
		}

		// Check client
		if (client == null) {
			throw new RuntimeException("Pinged by unknown client " + data.getIPAddress());
		}

		// Valid Client
		client.sendData(new NetworkObject(IPAddress, new ChatMessage("~Admin", "Ping: " + delay + "ms", Color.black, true)));

		return delay;
	}

	/**
	 * Server was pinged by a client
	 *
	 * @param clientIP
	 *            IP of who wants the history sent to them
	 */
	protected synchronized void pingClients() {

		final HashMap<String, Calendar> sentPings = new HashMap<String, Calendar>();

		// Send a ping to every client
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendData(new ChatMessage("/ping all", Color.black, true));

			sentPings.put(clients.get(i).getIPAddress(), Calendar.getInstance());
		}

		final long time = System.nanoTime();
		final long runTime = 10000;
		Thread pingThread = new Thread(){
			@Override
			public void run(){

				long lapse = System.nanoTime();
				if( sentPings.isEmpty() || (time + runTime) > lapse){
					//stop();
				}

				// Check for pings
				/*if( !pings.isEmpty() ){

					for( String sentIP : sentPings.keySet() ){

						// Has this IP been sent back to us yet?
						if( pings.containsKey(sentIP)){
							// Remove from everywhere
						}
					}

				}*/
			}
		};
	}

	/**
	 * Sends the given message to all clients on the server, provided they aren't listen in "exceptions"
	 * @param data Data to send to all the clients on the server
	 * @param exceptions Clients to not send the data to
	 */
	public void sendToAllClients(NetworkData data, ClientThread... exceptions) {

		for (int i = 0; i < clients.size(); i++) {

			ClientThread client = clients.get(i);

			// Check if we shouldn't send it to this client
			if( exceptions != null && exceptions.length > 0){
				for( ClientThread ex : exceptions ){
					if( ex.equals(client) ){
						continue;
					}
				}
			}

			// Send the data to this client
			client.sendData(data);
		}

	}

	/**
	 * Sends the given message to all clients on the server, provided they aren't listen in "exceptions"
	 * @param data Data to send to all the clients on the server
	 * @param exceptions Clients to not send the data to
	 */
	public void sendToAllClients(NetworkObject data, ClientThread... exceptions) {

		for (int i = 0; i < clients.size(); i++) {

			ClientThread client = clients.get(i);

			// Check if we shouldn't send it to this client
			if( exceptions != null && exceptions.length > 0){
				for( ClientThread ex : exceptions ){
					if( ex.equals(client) ){
						continue;
					}
				}
			}

			// Send the data to this client
			client.sendData(data);
		}

	}

	protected void sendToClient(String clientIP, NetworkData data) {

		// TODO HACK. Make FASTER!
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getIPAddress().equals(clientIP)) {
				clients.get(i).sendData(data);
				break;
			}
		}
	}

	protected ClientThread getClientFromName(String name) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getPlayerName().equals(name)) {
				return clients.get(i);
			}
		}

		return null;
	}

	protected ClientThread getClientFromIP(String IP) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getIPAddress().equals(IP)) {
				return clients.get(i);
			}
		}

		return null;
	}

	/**
	 * Provides a running thread that will continuously check for incoming data
	 * from a Client and send it to the server for processing
	 *
	 * @author veugeljame
	 *
	 */
	class ClientThread extends Thread {

		final Player player;
		final Socket socket;

		public ClientThread(Socket socket, Player player) {
			this.socket = socket;
			this.player = player;
		}

		public String getIPAddress() {
			return player.getIPAddress();
		}

		/**
		 * Wait for data from the client and
		 */
		public void run() {

			while (socket != null && !socket.isClosed()) {

				// Receive Input
				try {

					// Get Message
					ObjectInputStream input;
					try {
						input = new ObjectInputStream(socket.getInputStream());
					} catch (IOException e) {
						continue;
					}

					// Get the data from what was sent through the network
					NetworkObject data = null;
					try{
						data = (NetworkObject)input.readObject();
						data.getData().acknowledged = true; // We received the data
					}catch(InvalidClassException e){
						System.out.println("Class must be NetworkObject: " + e.classname);
						e.printStackTrace();
					}

					// Check if the data sent back to us was a ping all
					if( ((ChatMessage)data.getData()).message.equals("/ping all") ){
						if( pings.containsKey(data.getIPAddress()) ){
							pings.put(data.getIPAddress(), data.getCalendar());
						}
						continue;
					}

					// Sent data to our subclass for processing
					retrieveObject(data);

				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}


		public void stopClient(){
			if( socket != null && !socket.isClosed()){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void sendData(NetworkObject data) {
			try {

				// Valid connection
				// Send text to this client
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());

				// System.out.print("Sending '" + message + "' to " +
				// clients.get(i).name + "\n");
				out.flush();
				out.writeObject(data);
				out.flush();
			} catch (IOException e) {

				// More broken clients
				if (e.getMessage().equals("Broken pipe")) {
					removeClient(this,false);
				}
			}
		}

		public void sendData(NetworkData data) {
			sendData(new NetworkObject(IPAddress, data));
		}

		/*
		 * (non-Javadoc)
		 *
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

		/*
		 * (non-Javadoc)
		 *
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
			if (player == null) {
				if (other.player != null)
					return false;
			} else if (!player.equals(other.player))
				return false;
			return true;
		}

		private Server getOuterType() {
			return Server.this;
		}

		public String getPlayerName(){
			return player.getName();
		}

		public void setPlayerName(String name){
			player.setName(name);
		}
	}

	/**
	 * Gets the IPAddress of the server for others to connect to
	 *
	 * @return String containing IPAddress
	 */
	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * Gets the port that the server is currently running off
	 *
	 * @return int containing the Port that the server is running off
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Stops the server from running
	 */
	public void stopServer() {

		if( serverSocket != null && !serverSocket.isClosed() ){
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean isAdmin(String IP){
		return admins.contains(IP);
	}

	/**
	 * Returns a list of the admins IP's in the server
	 * @return ArrayList containing a list of IP's
	 */
	public ArrayList<String> getAdmins(){
		ArrayList<String> adminList = new ArrayList<String>();
		adminList.addAll(admins);

		return adminList;
	}

	public abstract void retrieveObject(NetworkObject data);
	public abstract void newClientConnection(ClientThread cl);
	public abstract void clientRejoins(ClientThread cl);
}
