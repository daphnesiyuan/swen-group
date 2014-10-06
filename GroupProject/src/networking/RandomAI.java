package networking;

import gameLogic.Avatar;
import gameLogic.Game;
import gameLogic.Room;

import java.util.Random;

public class RandomAI extends AI {

	public RandomAI(Room room, Player player) {
		super(room, player);
	}

	@Override
	public void think(Game game) {
		Random rand = new Random();

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
