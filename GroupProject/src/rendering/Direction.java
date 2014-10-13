package rendering;

/**
 * A Class that acts as a utility.
 * Given w, a, s, d it will return 0, 3, 2, 1 respectively
 * Given North, East, South, West it will return 0, 1, 2, 3 respectively
 * Given 0, 1, 2, 3 it will return North, East, South, West respectively
 *
 * @author Leon North
 *
 */
public class Direction {

	/**
	 * Given North, East, South, West it will return 0, 1, 2, 3 respectively
	 * @param direction
	 * @return int
	 * @author Leon North
	 */
	public static int get(String direction){
		if(direction.toLowerCase().equals("north")){
			return 0;
		}
		if(direction.toLowerCase().equals("east")){
			return 1;
		}
		if(direction.toLowerCase().equals("south")){
			return 2;
		}
		else if(direction.toLowerCase().equals("west")){
			return 3;
		}
		//should never get here
		return -1;
	}

	/**
	 * Given w, a, s, d it will return 0, 3, 2, 1 respectively
	 * @param direction
	 * @return
	 * @author Leon North
	 */
	public static int getKeyDirection(String direction){
		if(direction.toLowerCase().equals("w")){
			return 0;
		}
		if(direction.toLowerCase().equals("d")){
			return 1;
		}
		if(direction.toLowerCase().equals("s")){
			return 2;
		}
		else if(direction.toLowerCase().equals("a")){
			return 3;
		}
		//should never get here
		return -1;
	}

	/**
	 * Given 0, 1, 2, 3 it will return North, East, South, West respectively
	 * @param direction
	 * @return
	 * @author Ryan Gryffin
	 */
	public static String get(int direction){
		if (direction == 0){
			return "North";
		}
		if (direction == 1){
			return "East";
		}
		if (direction == 2){
			return "South";
		}
		if (direction == 3){
			return "West";
		}
		//should never get here
		return null;
	}
}
