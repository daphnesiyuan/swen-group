package networking;

import gameLogic.Game;
import gameLogic.Room;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Random AI that will walk around In a random direction everytime it's told to think
 * @author veugeljame
 *
 */
public class RandomAI extends AI {

	public RandomAI(Room room, String name) {
		super(room, name);
	}

	@Override
	public void think(Game game) {
		Random rand = new Random();

		System.out.println("THINKING!");

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
		Move move = new Move(getPlayer(), "W", direction);

		// Attempt to move
		game.moveAvatar(move);
	}

}
