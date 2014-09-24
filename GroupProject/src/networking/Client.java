package networking;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;

/**
 *Abstract Client class that holds all the required features when connecting, sending and receiving data from/to the server
 * @author veugeljame
 *
 */
public abstract class Client implements Runnable{

	private Socket socket;
	private String IPAddress = "null";


	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private Thread myThread;

	/**
	 * Creates a new Client object with a link to who is waiting for information from the server
	 * @param listener Who is waiting for information from the server
	 */
	public Client(){

		try {
			IPAddress = InetAddress.getLocalHost().getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the IP Address that this client is currently connected to
	 * @return String containing the connected IP Address
	 */
	public String getConnectedIPAddress(){
		return this.IPAddress;
	}

	/**
	 * Returns lan IP of the Client
	 * @return String containing the connected IP Address
	 */
	public String getClientIPAddress(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "???.???.???";
	}


	/**
	 * Attempts to connect to the given IPAddress and port number of the
	 * @param IPAddress IPAddress of the connection to connect as
	 * @param ID Identification of this client
	 * @param port Default: 32768
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(String IPAddress, String ID, int port) throws UnknownHostException, IOException{


		// Check if we have a current Socket, close if we do
		if( socket != null ){
			socket.close();
		}

		// Attempt Connection
		socket = new Socket(IPAddress,port);

		// Give the server our name
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(ID);
		out.flush();

		// Start our new socket
		myThread = new Thread(this);
		myThread.start();


		// Successful connection
		return true;
	}

	/**
	 * Attempts to connect to the given IPAddress and port number of the
	 * @param server The server to connect to
	 * @param ID Identification of this client
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(Server server, String ID) throws UnknownHostException, IOException{
		return connect(server.getIPAddress(), ID, server.getPort());
	}

	@Override
	public void run() {
		try{


			while( true ){

				// Wait for text
				try{
					inputStream = new ObjectInputStream(socket.getInputStream());
				}catch(IOException e ){ continue; }

				// Get data sent to us
				NetworkObject data;
				Object object = inputStream.readObject();
				try{
					data = (NetworkObject)object;
				}catch(Exception e){
					System.out.println("data: " + object);
					e.printStackTrace();
					continue;
				}

				// Check for a ping
				if( ((String)data.getData()).equals("/ping all") ){
					sendData(IPAddress, "/ping all");
					continue;
				}

				// Send object to client that is waiting for data
				retrieveObject(data);
			}


		}
		catch(SocketException e){
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
		}
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param data Object to sent to the server for processing
	 */
	protected boolean sendData(String name, Object data) throws IOException{

		// Check if we have a connection
		if( socket == null || socket.isClosed() ){
			return false;
		}

		// Send to server
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.writeObject(new NetworkObject(IPAddress, name, data));
		outputStream.flush();

		// Data sent successfully
		return true;
	}

	/**
	 * Servers send out objects as data to all their clients.
	 * This method is called once a single object is sent to a client to be handled by who is listening.
	 * Method MUST be synchronized
	 * @param data The data sent from a server to be handled
	 * @return
	 */
	public abstract void retrieveObject(NetworkObject data);
}
