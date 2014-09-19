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
	private final String name; // Name of the client that sent the message
	private final Calendar calendar; // When the file was sent

	/**
	 * Creates a new NetworkObject and attaches the IPAddress of the client that sends the data
	 * @param name IP of the client sending the data
	 * @param data The data wanting to be sent through the network
	 */
	public NetworkObject(String name, Calendar calendar, Object data){
		this.data = data;
		this.name = name;
		this.calendar = calendar;
	}

	/**
	 * Returns the IP Address of the client that sent the object
	 * @return
	 */
	public String getName() {
		return name;
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
	 * Displays the information about the data being sent through the network wit ha timestamp
	 * "time" "name": "data"
	 */
	public String toString(){
		String timeText = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
		return timeText + " " + name + ": " + data;
	}
}
