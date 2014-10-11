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

	private static final long THINKDELAY = 500;
	private long NEXTTHINK = System.currentTimeMillis() + THINKDELAY;

	public RandomAI(Room room, String name) {
		super(room, name);
	}

	@Override
	public void think(Game game) {

		// Only think if we need to
		if( NEXTTHINK > System.currentTimeMillis() ){
			return;
		}

		// Get random direction
		String direction = getRandomDirection();

		// Make the move
		Move move = new Move(getPlayer(), String.valueOf(getOppositeDirection(direction).charAt(0)), direction);

		// Attempt to move
		game.moveAvatar(move);

		// Remember when we need to think next
		NEXTTHINK = System.currentTimeMillis() + THINKDELAY;
	}

	/**
	 * Gets a random direction
	 * @return String containing a direction
	 */
	public String getRandomDirection(){
		Random rand = new Random();
		int randomMove = rand.nextInt(4);

		// Get random direction
		switch(randomMove){
			case 0: return "East";
			case 1: return "West";
			case 2: return "North";
			case 3: return "South";
		}
		return "Center";
	}

	/**
	 * Returns the opposite direction of any outcome from getRandomDirection
	 * @param direction What to get the inverse of
	 * @return Inverse of direction
	 */
	public String getOppositeDirection(String direction){
		if( direction.equals("West") ){ return "East"; }
		if( direction.equals("East") ){ return "West"; }
		if( direction.equals("South") ){ return "North"; }
		if( direction.equals("North") ){ return "South"; }

		return "Center";
	}

}
