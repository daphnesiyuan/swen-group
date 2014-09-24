package networking;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Object of which is sent through the network.
 * @author veugeljame
 *
 */
public class NetworkObject implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 3965982154655961725L;
	private final Object data; // What's being sent through the network
	private final String name; // Name of who sent the packet
	private final String IPAddress; // IP of who sent the data
	private final Calendar calendar; // When the file was sent

	/**
	 * Creates a new NetworkObject and attaches the IPAddress of the client that sends the data
	 * @param IPAddress IP of the client sending the data
	 * @param name Name of the client sending the data
	 * @param data The data wanting to be sent through the network
	 */
	public NetworkObject(String IPAddress, String name, Object data){
		this.data = data;
		this.IPAddress = IPAddress;
		this.name = name;
		this.calendar = Calendar.getInstance();
	}

	/**
	 * Creates a new NetworkObject and attaches the IPAddress of the client that sends the data
	 * @param IPAddress IP of the client sending the data
	 * @param name Name of the client sending the data
	 * @param data The data wanting to be sent through the network
	 * @param calendar Date on the calendar for when the object was sent through the network
	 */
	public NetworkObject(String IPAddress, String name, Object data, Calendar calendar){
		this.data = data;
		this.name = name;
		this.IPAddress = IPAddress;
		this.calendar = calendar;
	}

	/**
	 * Gets the data that was sent through the network
	 * @return data the data sent from a server to the given client
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Returns when the message was sent
	 * @return Calendar containing the information of when the data was sent
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * Displays whatever is stored inside data
	 */
	public String toString(){
		return getTime() + " " + name + ": " + data.toString();
	}

	/**
	 * Gets the time that the packet was sent
	 * @return
	 */
	public String getTime(){
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
	}

	/**
	 * Gets the name of the person that sent the packet
	 * @return Name String of the name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets the IPAddress of the client that sent the packet
	 * @return String containing the IP of the sender
	 */
	public String getIPAddress() {
		return IPAddress;
	}
}
