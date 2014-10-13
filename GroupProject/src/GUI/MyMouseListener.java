package GUI;

import gameLogic.Item;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import rendering.InvoPopup;

public class MyMouseListener implements MouseListener {

	private DrawingPanel panel;
	InvoPopup pop;

	public MyMouseListener(DrawingPanel dp) {
		panel = dp;
	}

	/**
	 * A helper method which registers when a mouse has been clicked and then
	 * sends the location of the mouse click to the panel
	 */
	public void mouseClicked(MouseEvent e) {
		panel.sendClick(e.getX(), e.getY());

		if (!panel.isStartMode()) { // must be in game mode for this to work

			int i = panel.getInvo().findBox(e.getX(), e.getY());
			checkInventory(i);
		}

	}

	public void checkInventory(int i){

		if (i == 0) { // box 1
			if (i <= (panel.getGameClient().getAvatar().getInventory()
					.size())
					&& (panel.getGameClient().getAvatar().getInventory()
							.get(0)) != null) {
				System.out.println("IN BOX 0");
				Item item = panel.getGameClient().getAvatar()
						.getInventory().get(0);
			}
		} else if (i == 1) {
			if (i <= (panel.getGameClient().getAvatar().getInventory()
					.size())
					&& (panel.getGameClient().getAvatar().getInventory()
							.get(1)) != null) {
				System.out.println("IN BOX 1");
				Item item = panel.getGameClient().getAvatar()
						.getInventory().get(1);
			}
		} else if (i == 2) {
			if (i <= (panel.getGameClient().getAvatar().getInventory()
					.size())
					&& (panel.getGameClient().getAvatar().getInventory()
							.get(2)) != null) {
				System.out.println("IN BOX 2");
				Item item = panel.getGameClient().getAvatar()
						.getInventory().get(2);
			}
		} else if (i == 3) {
			if (i <= (panel.getGameClient().getAvatar().getInventory()
					.size())
					&& (panel.getGameClient().getAvatar().getInventory()
							.get(3)) != null) {
				System.out.println("IN BOX 3");
				Item item = panel.getGameClient().getAvatar()
						.getInventory().get(3);
			}
		} else if (i == 4) {
			if (i <= (panel.getGameClient().getAvatar().getInventory()
					.size())
					&& (panel.getGameClient().getAvatar().getInventory()
							.get(4)) != null) {
				System.out.println("IN BOX 4");
				Item item = panel.getGameClient().getAvatar()
						.getInventory().get(4);
			}
		}


	}

	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) // for inventory interactions
			doPop(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			doPop(e);
	}

	/**
	 * Method to deal with the right clicking mini menu interacting with
	 * inventory
	 *
	 * @param e
	 */
	private void doPop(MouseEvent e) {
		InvoPopup menu = new InvoPopup();
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	public void mouseEntered(MouseEvent e) {
		// System.out.println("Mouse entered at " + e.getX() + ", " + e.getY());
	}

	public void mouseExited(MouseEvent e) {
		// System.out.println("Mouse exited at " + e.getX() + ", " + e.getY());
	}

}