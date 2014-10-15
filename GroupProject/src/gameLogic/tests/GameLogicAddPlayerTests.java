package gameLogic.tests;

import static org.junit.Assert.*;
import gameLogic.Avatar;
import gameLogic.Game;
import gameLogic.Room;
import gameLogic.Tile2D;

import java.awt.Point;

import networking.Player;

import org.junit.Before;
import org.junit.Test;

public class GameLogicAddPlayerTests {

	private Game game;
	private final Player player = new Player("IP1", "Alice");

	@Before
	public void setUp(){
		game = new Game();
	}

	@Test
	public void addValidPlayer(){

		// Try adding a player to the server
		assertTrue(game.addPlayer(player.getName()) != null);
	}

	@Test
	public void validRoom(){
		addValidPlayer();

		// Have a room
		assertTrue(game.getRoom(player.getName()) != null );

		// Room has the Avatar in it
		assertTrue(game.getRoom(player.getName()).getAvatar(player.getName()) != null);
	}

	@Test
	public void validAvatar(){
		addValidPlayer();

		// Player has an Avatar
		Avatar avatar = game.getAvatar(player.getName());
		assertTrue(avatar != null);

		// Room has the avatar
		assertTrue(game.getRoom(player.getName()).getAvatars().contains(avatar));
	}

	@Test
	public void validTile(){
		addValidPlayer();

		// Get Player Avatar
		Avatar avatar = game.getAvatar(player.getName());
		Room room = avatar.getCurrentRoom();
		Tile2D tile = avatar.getCurrentTile();

		assertTrue(tile != null);
		assertTrue(tile.getAvatar() == avatar);
	}
}