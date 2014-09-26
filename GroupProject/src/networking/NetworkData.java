package networking;

import java.io.Serializable;
import java.util.Calendar;

public abstract class NetworkData implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4448513134913822794L;

	// Check if the data has been acknowledged if sent from a client to the server
	public boolean acknowledged = false;

	// When was this data sent?
	public Calendar dataSent = Calendar.getInstance();

	/**
	 * Gets the time that the packet was sent
	 * @return
	 */
	public String getTime(){
		return dataSent.get(Calendar.HOUR_OF_DAY) + ":" + dataSent.get(Calendar.MINUTE);
	}
}
