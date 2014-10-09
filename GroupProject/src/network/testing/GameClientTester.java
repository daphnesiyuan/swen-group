package network.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Scanner;

import networking.ChatMessage;
import networking.GameClient;
import networking.Move;
import rendering.Direction;


public class GameClientTester {

	private GameClient gc;

	public GameClientTester(){
		gc = new GameClient("James", null);
		try {
			gc.connect("130.195.4.178", 32768);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Server listens for input directly to servers terminal Thread
		Thread inputListener = new Thread(new ClientInputListener());
		inputListener.start();

		System.out.println("Waiting for input...");


		// Displays any chanegs made to our chat history if there are any
		Thread historyListener = new Thread(){
			private int size = 0;
			@Override
			public void run(){

				while( true ){

					if( gc.getChatHistory().size() != size ){

						// Display all messages
						if( size < gc.getChatHistory().size() ){
							for( int i = 0; i < gc.getChatHistory().size(); i++ ){
								System.out.println(gc.getChatHistory().get(i));
							}
						}

						// Update our new size
						size = gc.getChatHistory().size();
					}


					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		historyListener.start();
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
	 *
	 * @param scan
	 * @return
	 */
	private boolean attemptChatMessage(Scanner scan){

		String message = "TESTING!";
		if( scan.hasNext() ){
			message = scan.nextLine();
		}

		try {
			boolean sent = gc.sendChatMessageToServer(message);
			return sent;
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


						if( scan.hasNext("!move") ){
							scan.next();
							if( !attemptMove(scan) ){
								System.out.println("Move failed...");
							}
							continue;
						}
						else if( scan.hasNext("!room") ){
							scan.next();
							System.out.println(gc.getRoom());
							continue;
						}
						else if( scan.hasNext("!avatar") ){
							scan.next();
							System.out.println(gc.getAvatar());
							continue;
						}
						else if( scan.hasNext("!history") ){ // 20
							scan.next();
							int size = scan.hasNextInt() ? scan.nextInt() : gc.getChatHistory().size();

							System.out.println("== HISTORY ==");
							for( ChatMessage cm : gc.getChatHistory(size) ){
								System.out.println(cm.toString());
							}
							continue;
						}


						// No command, so talk to server
						if( scan.hasNext()){
							if( !attemptChatMessage(scan) ){
								System.out.println("Chat Message failed...");
							}
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
