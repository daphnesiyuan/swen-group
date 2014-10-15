package gameLogic.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import gameLogic.Avatar;
import gameLogic.Box;
import gameLogic.Game;
import gameLogic.Item;
import gameLogic.Light;
import gameLogic.Shoes;
import gameLogic.Tile2D;
import networking.Move;
import networking.Player;

import org.junit.Before;
import org.junit.Test;

public class GameLogicGameTests {

	private Game game;
	private final Player player = new Player("IP1", "Alice");
	private Avatar avatar;


	@Before
	public void setUp(){
		game = new Game();
		game.addPlayer("Alice");
		avatar = game.getAvatar("Alice");
	}

	@Test
	public void TestGetAvatar(){
		assertFalse (avatar==null);
	}

	@Test
	public void TestUpdateLocation(){
		Tile2D tile = new Tile2D(1, 1);
		avatar.updateLocations( tile, avatar.getCurrentRoom() );

		//System.out.println("alice location="+game.getAvatar("Alice"). + " "+game.getAvatar("Alice").getTileYPos() );

		assertTrue(tile.equals( avatar.getCurrentTile() ));
	}

	@Test
	public void testUseItemInvalid(){
		Item item = null;
		assertFalse( avatar.useItem(item) );
	}

	@Test
	public void testUseItemValid(){
		Light item = new Light( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);

		assertFalse( avatar.useItem(item) );
	}

	@Test
	public void testDropItemUp(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D upTile = avatarTile.getTileUp();

		Light item = new Light( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "drop", 0 );
		avatar.dropItem(move);

		assertTrue(upTile.itemOnTile());
	}

	@Test
	public void testDropItemLeft(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D leftTile = avatarTile.getTileLeft();

		Shoes item = new Shoes( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "drop", 0 );
		avatar.dropItem(move);

		assertTrue(leftTile.itemOnTile());
	}

	@Test
	public void testDropItemDown(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D downTile = avatarTile.getTileDown();

		Shoes item = new Shoes( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "drop", 0 );
		avatar.dropItem(move);

		assertTrue(downTile.itemOnTile());
	}

	@Test
	public void testDropItem(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D upTile = avatarTile.getTileUp();

		Light item = new Light( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "drop", 0 );

		assertTrue(avatar.dropItem(move));
	}


	@Test
	public void testDropItemBadIndex(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D upTile = avatarTile.getTileUp();

		Light item = new Light( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "drop", 1 );

		assertFalse(avatar.dropItem(move));
	}

	@Test
	public void testEmptyInventory(){
		Light item = new Light( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		int before = inventory.size();
		System.out.println("Before: " + before);

		game.getAvatar("Alice").setInventory(inventory);
		System.out.println("Size: " + game.getAvatar("Alice").getInventory().size());

		System.out.println("size of inventory before="+ game.getAvatar("Alice").getInventory().size() );

		avatar.emptyInventory();
		assertTrue(before>avatar.getInventory().size());
	}

	@Test
	public void testOpenItemEmpty(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D upTile = avatarTile.getTileUp();

		Box item = new Box( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "open", 0 );

		assertFalse( avatar.openItem(move) );
	}

	@Test
	public void testOpenItemShoes(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D upTile = avatarTile.getTileUp();

		Box item = new Box( avatar.getStartTile() );
		Shoes shoes = new Shoes(upTile);
		item.getContains().add(shoes);
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "open", 0 );

		assertTrue( avatar.openItem(move) );
	}

	@Test
	public void testOpenItemInvalid(){
		Tile2D avatarTile = avatar.getCurrentTile();
		Tile2D upTile = avatarTile.getTileUp();

		Light item = new Light( avatar.getStartTile() );
		List<Item> inventory = new ArrayList<Item>();
		inventory.add(item);
		avatar.setInventory(inventory);
		Move move = new Move(player, "open", 0 );

		assertFalse( avatar.openItem(move) );
	}

	@Test
	public void testTakeDamage(){
		int before = avatar.getCell().getBatteryLife();
		avatar.takeDamage(10);

		assertTrue( avatar.getCell().getBatteryLife()==(before-10) );

	}

	@Test
	public void testTakeDamageDie(){
		int before = avatar.getCell().getBatteryLife();
		avatar.takeDamage(600);

		assertTrue( avatar.getCell().getBatteryLife()==500 );

	}

	@Test
	public void testAddKill(){
		int before = avatar.getScore();
		avatar.addKill();

		assertEquals( avatar.getScore(),(before+1) );
	}

	@Test
	public void testMoveUp(){
		avatar.setPlayerName("Daphne");
		assertEquals(avatar.getPlayerName(),"Daphne");
	}


}
