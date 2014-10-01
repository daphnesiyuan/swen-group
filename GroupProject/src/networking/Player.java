package networking;

/**
 * A Player object that contains the name of the player and IP in order to determine who is performing a move on which server
 * @author veugeljame
 *
 */
public class Player {
	private String name; // Name of the Player
	private String IPAddress; // IP Address of who is controlling this playher

	public Player(String name){
		this.setName(name);
	}

	public Player(String IP, String name){
		this.setIPAddress(IP);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}


}
