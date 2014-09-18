package networking;

/**
 * Treats the given object as a listener from a client.
 * So when messages are sent from the server to a client, they are then sent to what ever is listening.
 * @author veugeljame
 *
 */
public interface ClientListener {

	/**
	 * Servers send out objects as data to all their clients.
	 * This method is called once a single object is sent to a client to be handled by who is listening.
	 * Method MUST be synchronized
	 * @param object The object sent from a server to be handled
	 */
	public void retrieveObject(Object object);
}
