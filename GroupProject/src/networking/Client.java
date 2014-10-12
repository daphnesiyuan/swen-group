package networking;
import java.awt.Color;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ConnectException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;

/**
 *Abstract Client class that holds all the required features when connecting, sending and receiving data from/to the server
 * @author veugeljame
 *
 */
public abstract class Client{

	// Current connected server
	private Socket socket;
	protected String connectedIP = "null";
	protected int connectedPort = -1;

	// Which player the gameclient is controlling
	protected Player player;

	// Everyone to be sent to the server
	//private Object outGoingPacketLock = new Object();
	private ArrayDeque<NetworkObject> outgoingPackets = new ArrayDeque<NetworkObject>(200);

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private InputWaiter myThread;

	private final int port = 32768;

	/**
	 * Creates a new Client object with a link to who is waiting for information from the server
	 * @param listener Who is waiting for information from the server
	 */
	public Client(){

		player = new Player();
		try {
			player.setIPAddress( Inet6Address.getLocalHost().getHostAddress() );

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the IP Address that this client is currently connected to
	 * @return String containing the connected IP Address
	 */
	public String getConnectedIPAddress(){
		return connectedIP;
	}

	/**
	 * Returns the IP Address of the player according to the server they are connected to
	 * @return String containing the connected IP Address
	 */
	public String getIPAddress(){
		return player.getIPAddress();
	}


	/**
	 * Attempts to connect to the given IPAddress and port number of the
	 * @param IPAddress IPAddress of the connection to connect as
	 * @param playerName Identification of this client
	 * @param port Default: 32768
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	protected boolean connect(String IPAddress, String playerName, int port) throws UnknownHostException, IOException{

		// Disconnect from the server
		disconnect();

		// Attempt Connection
		Socket temp;
		try{
			temp = new Socket(IPAddress,port);
		}catch( ConnectException e ){
			return false;
		}


		try {

			// Wait for new IP
			ObjectInputStream in = new ObjectInputStream(temp.getInputStream());
			player.setIPAddress((String)in.readObject());

			// Give the server our name
			ObjectOutputStream out = new ObjectOutputStream(temp.getOutputStream());
			out.writeObject(playerName);
			out.flush();

			// Receive name from server
			in = new ObjectInputStream(temp.getInputStream());
			player.setName((String)in.readObject());

			// Start our new socket
			myThread = new InputWaiter();
			myThread.start();

			// Clear all packets that were pending
			outgoingPackets.clear();

			// Record server
			connectedIP = IPAddress;
			connectedPort = port;

			// Listen on this socket now
			socket = temp;

			// Perform our setup since we connected to a server
			successfullyConnected(playerName);


			// Successful connection
			return true;


		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Disconnects from the current server
	 * @return
	 */
	public boolean disconnect(){

		// Check if we have a current Socket, close if we do
		if( socket != null && !socket.isClosed()){
			try {
				socket.close();
			} catch (IOException e) { return false;}
		}

		//Wait for us to stop listening to the current socket
		if( myThread != null ){
			try {
				myThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Closed
		return true;
	}

	/**
	 * Attemps to reconnect to the current server using the given name
	 * @return true if connected, false if not.
	 */
	public boolean reconnect(String name){
		try {
			return connect(connectedIP, name, connectedPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Stores the given packet in a queue for sending once we have a socket
	 * @param data Object to sent to the server for processing
	 */
	protected boolean sendData(NetworkData data) throws IOException{

		// Data stored successfully
		return sendData(new NetworkObject(getIPAddress(), data));
	}

	protected boolean sendData(NetworkObject data){


		// Check if we have a connection
		if( socket == null || socket.isClosed() ){
			return false;
		}

		// Send to server
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());

			// Get packet to send
			outputStream.writeObject(data);
			outputStream.flush();

			// Send to client for client sided review
			retrieveObject(data);

			return true;
		} catch(NotSerializableException e){
			System.out.println("Client: Something is not Serializable, and can not be sent in object:\n" + data);
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public class InputWaiter extends Thread{
		@Override
		public void run() {
			try{

				while( socket != null && !socket.isClosed() ){

					// Wait for text
					try{
						inputStream = new ObjectInputStream(socket.getInputStream());
					}catch(IOException e ){ continue; }
					catch(NullPointerException e ){ continue; }

					// Get data sent to us
					NetworkObject data;
					try{
						data = (NetworkObject)inputStream.readObject();
					}catch(SocketException e){
						continue;
					}catch(StreamCorruptedException e){
						continue;
					}

					// Send object to client that is waiting for data
					retrieveObject(data);
				}


			}
			catch(SocketException e){
				e.printStackTrace();
			}
			catch(StreamCorruptedException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally{
				if( socket != null && !socket.isClosed() ){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				retrieveObject(new NetworkObject(getIPAddress(), new ChatMessage("You have been Disconnected", Color.black, true)));
			}
		}
	}

	/**
	 * Checks if the client is connected to a server
	 * @return True if socket is valid
	 */
	public boolean isConnected(){
		return socket != null && !socket.isClosed();
	}

	/**
	 * Servers send out objects as data to all their clients.
	 * This method is called once a single object is sent to a client to be handled by who is listening.
	 * Method MUST be synchronized
	 * @param data The data sent from a server to be handled
	 * @return
	 */
	public abstract void retrieveObject(NetworkObject data);

	/**
	 * What's called once we successfully connect to a server
	 * @param playerName name of our player that we are connecting with
	 */
	public abstract void successfullyConnected(String playerName);

	public int getPort() {
		return port;
	}
}