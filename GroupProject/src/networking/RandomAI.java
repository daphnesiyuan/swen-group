package networking;

import gameLogic.Game;
import gameLogic.Room;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import rendering.Direction;

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

		int nextMoveDirection = getRandomDirection();
		String direction = "";
		for(int counter = 0; counter < 4; counter++ ){

			// First letter of the string to move
			direction = getMovementKey(nextMoveDirection);

			// Attempt to move
			if( game.moveAvatar(new Move(getPlayer(), direction, Direction.get(0))) ){
				game.moveAvatar(new Move(getPlayer(), direction, Direction.get(0)));
				break;
			}

			// Rotate clockwise
			nextMoveDirection = getRotatedDirection(nextMoveDirection);
		}

		// Remember when we need to think next
		NEXTTHINK = System.currentTimeMillis() + THINKDELAY;
	}

	public int getRotatedDirection(int direction){
		return (direction+1)%4;
	}

	/**
	 * Gets a random direction
	 * @return String containing a direction
	 */
	public int getRandomDirection(){
		Random rand = new Random();


		int randomMove = rand.nextInt(4);
		return randomMove;
	}

	/**
	 * Returns the opposite direction of any outcome from getRandomDirection
	 * @param direction What to get the inverse of
	 * @return Inverse of direction
	 */
	public String getMovementKey(int direction){
		switch(direction){
		case 0:
			return "W";
		case 1:
			return "D";
		case 2:
			return "S";
		}

		return "A";
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
