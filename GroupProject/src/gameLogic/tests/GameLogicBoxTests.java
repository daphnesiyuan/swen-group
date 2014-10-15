package gameLogic.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gameLogic.Avatar;
import gameLogic.Box;
import gameLogic.Game;
import gameLogic.Tile2D;

import org.junit.Before;
import org.junit.Test;

public class GameLogicBoxTests {

	Tile2D tile;
	Box box;

	@Before
	public void setUp(){
		tile = new Tile2D(0, 0);
		box = new Box(tile);
	}

	@Test
	public void getTile() {
		Tile2D otherTile = tile;
		assertTrue(box.getTile() == otherTile);
	}

	@Test
	public void moveItemTo(){
		assertFalse(box.moveItemTo(null));

		Tile2D otherTile = new Tile2D(1,1);
		Tile2D oldTile = box.getTile();

		// Check placement
		assertFalse(otherTile.getTopItem() == box);

		// Move
		assertTrue(box.moveItemTo(otherTile));

		// Check placement
		assertTrue(otherTile.itemOnTile());
	}

	@Test
	public void InteractWith(){
		Game game = new Game();
		game.addPlayer("P");

		Avatar a = game.getAvatar("P");

		assertTrue(box.interactWith(a));
		assertTrue(box.interactWith(a));
		assertTrue(box.interactWith(a));
		assertTrue(box.interactWith(a));
		assertTrue(box.interactWith(a));
	}
}
