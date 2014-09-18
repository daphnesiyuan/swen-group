package networking;

/**
 * Exception thrown once something has failed in the network
 * @author veugeljame
 *
 */
public class NetworkException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8691952864944569751L;

	public NetworkException(Exception e, String string){
		super(string + "\n" + e.getStackTrace());
	}
}
