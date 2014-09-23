package rendering;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {
	List items = new ArrayList<Items>();

	public abstract List<Items> getItems();

}
