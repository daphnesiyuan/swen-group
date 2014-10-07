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

	private DrawingPanel panel;

	public KeyBoard(DrawingPanel d){
		panel = d;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * A method for the key board class which responds to key pressing
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		if (!(panel.getKeysDown()).contains(e.getKeyCode()))
			(panel.getKeysDown()).add(new Integer(e.getKeyCode()));
		if(panel.isChatMode()){
			panel.addToCurrentMessage( Character.toString(e.getKeyChar()) );

		}
		actionKeys();
		//repaint();
	}


	private void actionKeys() {

		if ((panel.getKeysDown()).contains(KeyEvent.VK_ALT)){
			boolean b = panel.isChatMode();
			panel.setChatMode(!b);
		}
		if ( panel.isChatMode() ){
			if ((panel.getKeysDown()).contains(KeyEvent.VK_ENTER)){
				//chatMessages.add(new ChatMessage("Ryan", currentMessage, Color.RED));
				try {
					(panel.getGameClient()).sendChatMessageToServer( panel.getCurrentMessage() );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				panel.setCurrentMessage("");
			} else {

			}
		}
		else{
			if ((panel.getKeysDown()).contains(KeyEvent.VK_CONTROL)) {
				int d = panel.getDirection();
				//directionI = (directionI + 1) % 4;
				panel.setDirection( (d+1)%4 );
			}
			if((panel.getKeysDown()).contains(KeyEvent.VK_W)){
				moveForward();
			}
			if((panel.getKeysDown()).contains(KeyEvent.VK_A)){
				moveLeft();
			}
			if((panel.getKeysDown()).contains(KeyEvent.VK_S)){
				moveBack();
			}
			if((panel.getKeysDown()).contains(KeyEvent.VK_D)){
				moveRight();
			}
		}
		(panel.getKeysDown()).clear();

	}

	private void moveRight() {
		//System.out.println(player);
		Move move = new Move((panel.getGameClient()).getPlayer(), "D", Direction.get( panel.getDirection() ));

		try {
			panel.getGameClient().sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void moveBack() {
		//System.out.println(player);
		Move move = new Move(panel.getGameClient().getPlayer(), "S", Direction.get(panel.getDirection()));

		try {
			panel.getGameClient().sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void moveLeft() {
		//System.out.println(player);
		Move move = new Move(panel.getGameClient().getPlayer(), "A", Direction.get( panel.getDirection() ));

		try {
			panel.getGameClient().sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void moveForward() {

//		System.out.println(player);
		Move move = new Move(panel.getGameClient().getPlayer(), "W", Direction.get(panel.getDirection()));

		try {
			panel.getGameClient().sendMoveToServer(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sending move");

		try {
			Thread.sleep(50);
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
