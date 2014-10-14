package networking;

/**
 * Interaction associated when a Client has requested a move on their side and is sent through to the gamelogic for processing
 * @author veugeljame
 *
 */
public class Move extends NetworkData {

	/**
	 *
	 */
	private static final long serialVersionUID = 5277768659149235735L;

	public final String interaction;
	public final Player player;
	public final String renderDirection;
	public final int index;

	public Move(Player player, String interaction, String renderDirection){
		this.interaction = interaction;
		this.player = player;
		this.renderDirection = renderDirection;
		index = 0;
	}

	public Move(Player player, String interaction, int index){
		this.interaction = interaction;
		this.player = player;
		this.index = index;
		renderDirection = null;
	}


	/**
	 * What to do when the move is performed
	 * @return Interaction
	 */
	public String getInteraction() {
		return interaction;
	}

	/**
	 * Gets the player that is performing this move
	 * @return Player object of who to move
	 */
	public Player getPlayer(){
		return player;
	}

	@Override
	public String toString(){
		return player.getName() + " performs " + interaction;
	}

	/**
	 * Which direction the rendering is currently facing
	 * @return North, East, South, West
	 */
	public String getRenderDirection() {
		return renderDirection;
	}

	public int getIndex() {
		return index;
	}

}
