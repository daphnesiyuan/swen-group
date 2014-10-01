package networking;

public class Move extends NetworkData {

	/**
	 *
	 */
	private static final long serialVersionUID = 5277768659149235735L;

	public final String interaction;
	public final Player player;
	public final String renderDirection;



	public Move(Player player, String interaction, String renderDirection){
		this.interaction = interaction;
		this.player = player;
		this.renderDirection = renderDirection;
	}

	public String getInteraction() {
		return interaction;
	}

	public Player getPlayer(){
		return player;
	}

	@Override
	public String toString(){
		return player.getName() + " performs " + interaction;
	}

	public String getRenderDirection() {
		return renderDirection;
	}

}
