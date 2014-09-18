package networking;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Client implements Runnable{

	private ClientListener listener;

	private Socket socket;
	private String IPAddress = "null";
	private String clientName;

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private Thread myThread;

	/**
	 * Creates a new Client object with a link to who is waiting for information from the server
	 * @param listener Who is waiting for information from the server
	 */
	public Client(ClientListener listener){ this.listener = listener; }

	/**
	 * Changes the name of the client to be displayed through-out the game
	 * @param name
	 */
	public void setName(String name){
		this.clientName = name;
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
	 * @param IPAddress
	 * @param port Default: 32768
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(String IPAddress, int port) throws UnknownHostException, IOException{


		// Check if we have a current Socket, close if we do
		if( socket != null ){
			socket.close();
		}

		// Attempt Connection
		socket = new Socket(IPAddress,port);

		// Wait for us to be given the name
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(clientName);
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
	 * @return True if connection worked, otherwise a exception gets thrown
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connect(Server server) throws UnknownHostException, IOException{
		return connect(server.getIPAddress(), server.getPort());
	}

	/**
	 * Sends the given object to the server that the client is connected to
	 * @param object Object to sent to the server for processing
	 * @throws NetworkException
	 */
	public void sendObject(Object object) throws NetworkException{
		try {

			// Send to server
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(object);
			outputStream.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try{


			while( !socket.isClosed() ){

				// Wait for text
				try{
					inputStream = new ObjectInputStream(socket.getInputStream());
				}catch(IOException e ){ continue; }

				// Send object to client that is waiting for data
				listener.retrieveObject(inputStream.readObject());
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
	 * Returns the name placed as this client
	 * @return
	 */
	public String getName() {
		return clientName;
	}
}
