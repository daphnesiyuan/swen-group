package dataStorage;

import gameLogic.Avatar;
import gameLogic.Door;
import gameLogic.Floor;
import gameLogic.Room;
import gameLogic.Tile2D;
import gameLogic.Wall;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import networking.GameClient;
import networking.GameServer;
import networking.Player;
import rendering.Rendering;
import rendering.testRendering;


/**
 *
 * @author northleon
 *
 */
public class TestSave extends javax.swing.JFrame {

	public static Rendering canvas;
	public static GameClient gameClient;
	public static Player player;

	public TestSave () {
		GameServer gameServer = new GameServer();


		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		gameClient = new GameClient("Ryan");
		try {
			gameClient.connect(InetAddress.getLocalHost().getHostAddress(), "Ryan", 32768);
			//gameClient.connect("130.195.7.84", "Ryan", 32768);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.gameClient = gameClient;

		//System.out.println(gameClient.getChatHistory().get(0));
		System.out.println(gameClient.getName());


		Room room = gameClient.getRoom();
		while (room == null){
			room = gameClient.getRoom();
			System.out.println("room check");
		}

		Avatar avatar = gameClient.getAvatar();
		while (avatar == null){
			avatar = gameClient.getAvatar();
			System.out.println("avatar check");
		}

		Player player = gameClient.getPlayer();
		while (player == null){
			this.player = gameClient.getPlayer();
		}
		System.out.println(player);


		if( gameServer.saveGame() ){
			System.out.println("SAVED!!!!!!!!!!");
		}
		else{
			System.out.println("Not saved :(");
		}

	}

	public static void main(String[] args) {
		new TestSave();
	}
}
