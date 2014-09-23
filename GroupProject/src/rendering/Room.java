package rendering;

public class Room {
	Tile[][] tiles;

	public Room(){
		tiles = new Tile[][]{
				{new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
				{new Door(), new Lava(), new Lava(), new Carpet(), new Wall() },
				{new Wall(), new Lava(), new Carpet(), new Carpet(), new Wall() },
				{new Wall(), new Carpet(), new Carpet(), new Carpet(), new Wall() },
				{new Wall(), new Wall(), new Wall(), new Door(), new Wall(), }
		};

		tiles[3][3].getItems().add(new Rattle());
	}

	public Tile[][] getTiles(){
		return tiles;
	}

}
