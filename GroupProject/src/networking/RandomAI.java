package networking;

import gameLogic.Game;
import gameLogic.Room;

import java.util.Random;

/**
 * Random AI that will walk around In a random direction everytime it's told to think
 * @author veugeljame
 *
 */
public class RandomAI extends AI {

	public RandomAI(Room room, Player player) {
		super(room, player);
	}

	@Override
	public void think(Game game) {
		Random rand = new Random();

		// Get random direction
		String direction;
		switch(rand.nextInt(4)){
			case 0:
				direction = "North";
				break;

			case 1:
				direction = "East";
				break;

			case 2:
				direction = "West";
				break;

			default: direction = "South";

		}

		// Make the move
		Move move = new Move(player, "W", direction);

		// Attempt to move
		game.moveAvatar(move);
	}

}
