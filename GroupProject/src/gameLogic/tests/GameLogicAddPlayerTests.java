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

		// Room has our Avatar
		assertTrue(game.getRoom(player.getName()).getAvatar(player.getName()) != null);
	}

	@Test
	public void invalidRoom(){
		addValidPlayer();

		// Have a room
		Room playersRoom = game.getRoom(player.getName());

		// Get Different Room
		Room otherRoom = null;
		for(Room r : game.getRoomsInGame()){
			if( !r.equals(playersRoom) ){
				otherRoom = r;
				break;
			}
		}

		// Shouldn't be equal
		assertFalse(playersRoom.equals(otherRoom));

		// Player shouldn't be in this room
		assertFalse(otherRoom.getAvatar(player.getName()) != null);

		// Room has our Avatar
		assertTrue(game.getRoom(player.getName()).getAvatar(player.getName()) != null);
	}

	@Test
	public void validAvatar(){
		addValidPlayer();

		assertTrue(game.getAvatar(player.getName()) != null );

		// Get Player Avatar
		Avatar avatar = game.getAvatar(player.getName());
		assertTrue(avatar != null);
	}

	@Test
	public void validTile(){
		addValidPlayer();

		// Get Player Avatar
		Avatar avatar = game.getAvatar(player.getName());
		Room room = avatar.getCurrentRoom();
		Tile2D tile = avatar.getCurrentTile();
		Point point = new Point(tile.getyPos(), tile.getxPos());

		// Avatars Tile
		assertTrue(tile != null);
		assertTrue(tile.getAvatar() != null);
		assertTrue(tile.getAvatar() == avatar);

		// Room containing the Tile
		Point roomPoint = AvatarPositionIn2DArray(room.getTiles(), avatar);
		Tile2D roomTile = room.getTiles()[roomPoint.y][roomPoint.x];
		assertTrue(tile2DContainsAvatar(room.getTiles(),avatar));
		// Rooms Tile
		assertTrue(point.equals(roomPoint));
		assertTrue(roomTile != null);
		assertTrue(roomTile.getAvatar() != null);
		assertTrue(roomTile.getAvatar() == avatar);

	}

	private boolean tile2DContainsAvatar(Tile2D[][] tiles, Avatar avatar){
		return AvatarPositionIn2DArray(tiles, avatar) != null;
	}

	private Point AvatarPositionIn2DArray(Tile2D[][] tiles, Avatar avatar){
		for(int x = 0; x < tiles[0].length; x++ ){
			for(int y = 0; y < tiles.length; y++ ){
				Tile2D tile = tiles[y][x];
				if(tile != null && tile.getAvatar() != null && tile.getAvatar().equals(avatar)){
					return new Point(y,x);
				}
			}
		}

		return null;
	}
}