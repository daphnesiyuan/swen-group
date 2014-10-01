package gameLogic;

import java.io.IOException;
import java.net.UnknownHostException;

import networking.GameClient;

public class gameClientTest {

	public static void main(String[]args){

		new gameClientTest();
	}

	public gameClientTest(){
		GameClient gc = new GameClient("Ryan");
		try {
			gc.connect("130.195.7.84",32768);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Room room = gc.getRoom();
		System.out.println(room);

	}

}
