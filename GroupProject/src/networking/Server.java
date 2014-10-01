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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 *Basic abstract server class that contains the required features of a server alogn with features of admins, pinging and more.
 * @author veugeljame
 *
 */
public abstract class Server implements Runnable{

	protected ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	protected ArrayList<String> admins = new ArrayList<String>(); // List of admin
																// IP Addresses

	// Pings from IP to time sent
	private HashMap<String, Calendar> lastPinged = new HashMap<String, Calendar>();

	protected String IPAddress;
	protected int port = 32768;
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

			private long timeLastPing;
			private int clientPingDelay = 1000;

			@Override
			public void run(){

				while( true ){

					// Ping clients to check for outgoing packets
					if( System.currentTimeMillis() > (timeLastPing + clientPingDelay) ){
						pingClients();
						timeLastPing = System.currentTimeMillis();
					}




					try {
						sleep(30);
					} catch (InterruptedException e) {
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
		System.out.print("WARNING: The Server has closed\n");
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
	 * @param scan2
	 *
	 * @param clientIP
	 *            IP of who wants the history sent to them
	 */
	protected synchronized long ping(Scanner scan, NetworkObject data) {

		// See if the ping has a destination
		if( scan.hasNext() && !scan.hasNext(IPAddress) ){

			// Ping all clients?
			if( scan.hasNext("all") ){
				for (int i = 0; i < clients.size(); i++) {
					ClientThread client = clients.get(i);
					pingClient(client.getPlayerName(), data);
				}
			}
			else if( !scan.hasNext("everyone") ){

				// Where is the message going to?
				pingClient(scan.nextLine().trim(),data);
			}
			return -1;
		}
		return pingServer(data.getIPAddress(), data.getCalendar());
	}

	protected synchronized void pingClient(String whoToPing, NetworkObject data){

		// Get who pinged the server
		ClientThread to = getClientFromName(whoToPing);

		// Check if we can find the client via name instead
		if( to == null ){
			System.out.println("Couldnt find client from Name");
			to = getClientFromIP(whoToPing);
			if( to == null ){

				// Couldn't send TO a person
				System.out.println("Unable to find client with whoToPing: " + whoToPing);
				return;
			}
		}

		to.sendData(data);
	}

	protected synchronized long pingServer(String whoPingedMe, Calendar dateSentFromClient){
		Calendar currentTime = Calendar.getInstance();
		long delay = currentTime.getTimeInMillis()
				- dateSentFromClient.getTimeInMillis();

		// Get who pinged the server
		ClientThread client = getClientFromIP(whoPingedMe);

		// Check client
		if (client == null) {
			if( !whoPingedMe.equals(IPAddress) ){
				System.out.println("Pinged by unknown client " + whoPingedMe);
			}

		}
		else{
			// Send the ping back to the client
			client.sendData(new NetworkObject(IPAddress, new ChatMessage("~Admin", "Ping: " + delay + "ms", Color.black, true)));
		}

		return delay;
	}

	/**
	 * Server was pinged by a client
	 *
	 * @param clientIP
	 *            IP of who wants the history sent to them
	 */
	protected synchronized void pingClients() {

		for (int i = 0; i < clients.size(); i++) {

			ClientThread client = clients.get(i);

			pingClient(client.getPlayerName(), new NetworkObject(IPAddress, new ChatMessage("~Admin","/ping everyone",Color.black,true)));

			if( !clients.contains(client) ){
				i--;
			}
		}
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

		// TODO HACK. Make FASTER!
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getPlayerName().equals(name)) {
				return clients.get(i);
			}
		}

		return null;
	}

	protected ClientThread getClientFromIP(String IP) {

		// TODO HACK. Make FASTER!
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

		// Determines if we are still running the server
		// If False, then outgoing packets are stopped
		boolean running = true;

		// Player object to use according to the client
		final Player player;

		// Socket to send backs to and receive from the client
		Socket socket;

		private Object outGoingPacketLock = new Object();
		private ArrayDeque<NetworkObject> outgoingPackets = new ArrayDeque<NetworkObject>();

		public ClientThread(Socket socket, Player player) {
			this.socket = socket;
			this.player = player;

			Thread outgoingThread = new Thread(){
				@Override
				public void run(){

					ObjectOutputStream outputStream = null;
					while( running ){

						// Check if we are connected
						if( isConnected() ){
							try {

								NetworkObject popped;

								// Try pinging the server if we have a server
								synchronized(outGoingPacketLock){
									if( outgoingPackets.isEmpty() ){
										sendData(new ChatMessage(getName(), "/ping everyone", Color.black, true));
										try { sleep(1000); } catch (InterruptedException e) {}
									}
								}


								// Get packet to send
								popped = outgoingPackets.pop();

								// Send to server
								outputStream = new ObjectOutputStream(ClientThread.this.socket.getOutputStream());

								outputStream.writeObject(popped);
								outputStream.flush();

								// Send to client for client sided review
								retrieveObject(popped);

							} catch(SocketException e){
								ClientThread.this.socket = null;
								continue;
							}catch (IOException e) {
								e.printStackTrace();
							}

							try { sleep(30); } catch (InterruptedException e) {e.printStackTrace();}
						}
						else{
							try { sleep(1000); } catch (InterruptedException e) {retrieveObject(new NetworkObject(IPAddress, new ChatMessage("WARNING", "Waiting for socket to reconnect....",Color.black,true)));}
						}
					}
				}
			};
			outgoingThread.start();
		}

		/**
		 * Checks if the client is connected to a server
		 * @return True if socket is valid
		 */
		public boolean isConnected(){
			return socket != null && !socket.isClosed();
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
						System.out.println("Incoming object must be NetworkObject: " + e.classname);
						e.printStackTrace();
					}
					catch(SocketException e){
						continue;
					}

					// Check if the data sent back to us was a ping all
					if( ((ChatMessage)data.getData()).message.startsWith("/ping") ){

						// Ping Everyone
						if( ((ChatMessage)data.getData()).message.startsWith("/ping everyone") ){
							lastPinged.put(getPlayerName(), Calendar.getInstance());
							continue;
						}
						else{
							data = new NetworkObject(getIPAddress(), data.getData(), data.getCalendar());
						}
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

		/**
		 * Stops the client and thread.
		 */
		public void stopClient(){
			running = false;
			if( socket != null && !socket.isClosed()){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Queues data to be stored for sending to this client
		 * @param data To be sent to the client
		 */
		public synchronized void sendData(NetworkObject data) {
			// Check if we have a connection
			if( socket != null && !socket.isClosed() ){

				// Add to our packets to send
				synchronized(outGoingPacketLock){
					outgoingPackets.add(data);
				}
			}
		}

		/**
		 * Queues data to be stored for sending to this client
		 * @param data To be sent to the client
		 */
		public synchronized void sendData(NetworkData data) {
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
