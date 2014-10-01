package GUI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import rendering.Direction;
import networking.ChatMessage;
import networking.Move;

public class KeyBoard implements KeyListener{

	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

	private DrawingPanel panel;

	public KeyBoard(DrawingPanel d){
		panel = d;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (!keysDown.contains(e.getKeyCode()))
			keysDown.add(new Integer(e.getKeyCode()));
		if( panel.isChatMode() ){
			currentMessage+=e.getKeyChar();
		}
		actionKeys();
		//System.out.println("bla");
		panel.repaint();
	}


	private void actionKeys() {

		if (keysDown.contains(KeyEvent.VK_ALT)){
			//chatMode = !chatMode;
			boolean b = !panel.isChatMode();
			panel.setChatMode(b);
		}
		if ( panel.isChatMode() ){
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
			if(keysDown.contains(KeyEvent.VK_A)){
				moveLeft();
			}
			if(keysDown.contains(KeyEvent.VK_S)){
				moveBack();
			}
			if(keysDown.contains(KeyEvent.VK_D)){
				moveRight();
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

	private void moveRight() {
		System.out.println(player);
		Move move = new Move(player, "D", Direction.get(direction));

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

	private void moveBack() {
		System.out.println(player);
		Move move = new Move(player, "S", Direction.get(direction));

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

	private void moveLeft() {
		System.out.println(player);
		Move move = new Move(player, "A", Direction.get(direction));

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

	private void moveForward() {

		System.out.println(player);
		Move move = new Move(player, "W", Direction.get(direction));

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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
