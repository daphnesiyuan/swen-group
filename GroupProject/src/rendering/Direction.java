package rendering;

/**
 *
 * @author northleon
 *
 */
public class Direction {

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
		if (direction == 2){
			return "West";
		}
		//should never get here
		return null;
	}
}
