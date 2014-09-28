package networking;

public class Move extends NetworkData {

	/**
	 *
	 */
	private static final long serialVersionUID = 5277768659149235735L;

	public final String interaction;
	public final Player player;



	// To Move a player after a key is pressed for example, construct a new Move object with the moving players relative identification class (would that be GameClient?)
	// Constructor takes a gameClient now - but may be something else. Just need to determine which player/ character the movment is happening to, so please change if
	// its wrong.
	// Constructor also takes 1 of 4 strings depending on what direction key is pressed - Daphne let Ryan know what these strings will be.
	// I.E. "W", "A", "S" or "D". but could be anything - this class could also be extended to pressing interaction keys to pickup / interact with objects?
	// The new Move object should be sent to the method moveCharacterTo(Move); in the Game class (inside gameLogic.gameState). This method will return a relative success
	// boolean - this will depend on contextual factors - ie is the player trying to move into a wall etc..
	// When the game is first started, can assume that a players initial facing direction is north, So Game Logic will keep track of movments made and update direction
	// faced accordingly.

	// NB: would be easier / more helpful for game logic to recieve the identifier for the move to happen in, stored in this class.
	//
	public Move(Player player, String interaction){
		this.interaction = interaction;
		this.player = player;
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

}
