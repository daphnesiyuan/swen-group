package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Scanner;

import rendering.Direction;


public class GameClientTester {

	GameClient gc;

	public GameClientTester(){
		try {
			gc = new GameClient("James", null, "130.195.7.141", 32768);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Server listens for input directly to servers terminal Thread
		Thread inputListener = new Thread(new ClientInputListener());
		inputListener.start();

		System.out.println("Waiting for input...");
	}

	/**
	 * move "W", Direction.get(direction)
	 * @param scan
	 * @return
	 */
	private boolean attemptMove(Scanner scan){
		if( !scan.hasNext() ){
			System.out.println("Missing interaction!");
			return false;
		}
		String interaction = scan.next();

		// Direction
		if( !scan.hasNext() ){
			System.out.println("Missing Direction!");
			return false;
		}

		String direction = scan.next();

		try {
			return gc.sendMoveToServer(new Move(gc.getPlayer(), interaction, direction));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * A class that listens for input from the console and sends the input to
	 * processServerMessage
	 *
	 * @author veugeljame
	 *
	 */
	private class ClientInputListener implements Runnable {
		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String text;

			while (true) {
				try {
					text = (String) br.readLine();

					if (text != null) {
						Scanner scan = new Scanner(text);


						if( scan.hasNext("move") ){
							scan.next();
							if( attemptMove(scan) ){
								System.out.println("Move success!");
							}
							else{
								System.out.println("Move failed...");
							}
						}
						else if( scan.hasNext("printRoom") ){
							scan.next();
							System.out.println(gc.getRoom());
						}
						else if( scan.hasNext("printAvatar") ){
							scan.next();
							System.out.println(gc.getAvatar());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args){
		new GameClientTester();
	}
}
