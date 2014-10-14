package gameLogic.tests;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gameLogic.Avatar;
import gameLogic.Game;
import gameLogic.Item;
import gameLogic.Light;
import gameLogic.Room;
import gameLogic.Tile2D;
import networking.Player;

import org.junit.Before;
import org.junit.Test;

public class GameLogicRoomTests {

	private Game game;
	private final Player player = new Player("IP1", "Alice");
	private Room room = null;

	@Before
	public void setUp(){
		game = new Game();

		// Try adding a player to the server
		game.addPlayer(player.getName());

		// Check the room
		room = game.getRoom(player.getName());
		assertTrue(room.getAvatar(player.getName()) != null);
	}

	@Test
	public void getAvatar(){

		// Valid Case
		Avatar avatar = room.getAvatar(player.getName());
		assertTrue(avatar != null);

		// Invalid case
		avatar = room.getAvatar("Not here");
		assertTrue(avatar == null);
	}

	@Test
	public void removeAvatar(){
		// Valid Case
		Avatar avatar = room.getAvatar(player.getName());
		assertTrue(room.getAvatars().contains(avatar));

		// Remove and check if it's removed
		room.removeAvatar(avatar);
		assertFalse(room.getAvatars().contains(avatar));
	}

	@Test
	public void getItemAt(){

		// Shouldn't contain an item
		assertTrue(room.getItemAt(1, 2) == null);

		// Add an item to the tile
		Tile2D tile = room.getTiles()[1][2];
		Item light = new Light(tile);
		tile.addItem(light);

		// Should be an item on this square
		assertTrue(room.getItemAt(1, 2) == light);
	}

	@Test public void equals(){

		/** Compare Tiles */
		Room original = game.getRoom(player.getName());
		Room room = new Room(original.getTiles(),null);

		assertFalse(original.equals(room));

	}

	@Test public void setGetTiles(){
		Tile2D[][] oldTiles = room.getTiles();
		assertTrue(oldTiles != null);

		Tile2D[][] newTiles = new Tile2D[oldTiles.length][oldTiles[0].length];
		newTiles[1][1] = new Tile2D(1,1);
		newTiles[1][2] = new Tile2D(1,2);
		newTiles[2][1] = new Tile2D(2,1);
		newTiles[2][2] = new Tile2D(2,2);

		// Aren't the same reference
		assertTrue( room.getTiles() != newTiles );

		// Assign new tiles
		room.setTiles(newTiles);

		assertTrue( room.getTiles() == newTiles );
	}
}
