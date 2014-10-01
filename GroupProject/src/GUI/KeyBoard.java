package GUI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import networking.ChatMessage;
import networking.Move;

public class KeyBoard implements KeyListener{

	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (!keysDown.contains(e.getKeyCode()))
			keysDown.add(new Integer(e.getKeyCode()));
		if(chatMode){
			currentMessage+=e.getKeyChar();
		}
		actionKeys();
		//System.out.println("bla");
		repaint();
	}


	private void actionKeys() {

		if (keysDown.contains(KeyEvent.VK_ALT)){
			chatMode = !chatMode;
		}
		if (chatMode){
			if (keysDown.contains(KeyEvent.VK_ENTER)){
				chatMessages.add(new ChatMessage("Ryan", currentMessage, Color.RED));
				currentMessage = "";
			} else {

			}
		}
		else{
			if (keysDown.contains(KeyEvent.VK_CONTROL)) {
				direction = (direction + 1) % 4;
			}
			if(keysDown.contains(KeyEvent.VK_W)){
				moveForward();
			}
		}
		keysDown.clear();
		System.out.println(gameClient.roomIsModified());
		while(!gameClient.roomIsModified()){
			System.out.println("checking modified");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gameClient.setRoomModified(false);
		repaint();
	}

	private void moveForward() {

		System.out.println(player);
		Move move = new Move(player, "W", "North");

		try {
			testrendering.gameClient.sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
