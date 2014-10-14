package GUI;

import gameLogic.Item;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;

public class MyMouseListener implements MouseListener {

	Item item;
	int itemIndex;

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

			itemIndex = panel.getInvo().findBox(e.getX(), e.getY());

			if(checkInventory(itemIndex)){ //something is here
				if (e.isPopupTrigger()) // for inventory interactions
					item=item = panel.getGameClient().getAvatar().getInventory().get(itemIndex);
					doPop(e);
			}
		}

	}

	public void mousePressed(MouseEvent e) {
		if (!panel.isStartMode()) { // must be in game mode for this to work

			itemIndex = panel.getInvo().findBox(e.getX(), e.getY()); //gets index of item/box

			if(checkInventory(itemIndex)){ //something is here
				if (e.isPopupTrigger()) // for inventory interactions
					item=item = panel.getGameClient().getAvatar().getInventory().get(itemIndex);
					doPop(e);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (!panel.isStartMode()) { // must be in game mode for this to work

			itemIndex = panel.getInvo().findBox(e.getX(), e.getY());

			if(checkInventory(itemIndex)){ //something is here
				if (e.isPopupTrigger()) // for inventory interactions
					item=item = panel.getGameClient().getAvatar().getInventory().get(itemIndex);
					doPop(e);
			}
		}
	}

	/**
	 * Method to deal with the right clicking mini menu interacting with
	 * inventory
	 *
	 * @param e
	 */
	private void doPop(MouseEvent e) {
		boolean b = item.getClass().equals(Box.class);
		InvoPopup menu = new InvoPopup(panel, b);
		menu.show(e.getComponent(), e.getX(), e.getY());
		menu.sendItem(item, itemIndex);
	}

	public boolean checkInventory(int i){

		if (i == 0) { // box 1
			if (i <= (panel.getGameClient().getAvatar().getInventory().size())
					&& (panel.getGameClient().getAvatar().getInventory().get(0)) != null) {

				System.out.println("IN BOX 1; size of inventory="+panel.getGameClient().getAvatar().getInventory().size());

				System.out.println("IN BOX 0");
				item = panel.getGameClient().getAvatar().getInventory().get(0);
				return true;
			}
		} else if (i == 1) {
			if (i < (panel.getGameClient().getAvatar().getInventory().size())
					&& (panel.getGameClient().getAvatar().getInventory().get(1)) != null) {
				System.out.println("IN BOX 1; size of inventory="+panel.getGameClient().getAvatar().getInventory().size());
				item = panel.getGameClient().getAvatar().getInventory().get(1);
				return true;
			}
		} else if (i == 2) {
			if (i < (panel.getGameClient().getAvatar().getInventory().size())
					&& (panel.getGameClient().getAvatar().getInventory().get(2)) != null) {
				System.out.println("IN BOX 2");
				item = panel.getGameClient().getAvatar().getInventory().get(2);
				return true;
			}
		} else if (i == 3) {
			if (i < (panel.getGameClient().getAvatar().getInventory().size())
					&& (panel.getGameClient().getAvatar().getInventory().get(3)) != null) {
				System.out.println("IN BOX 3");
				item = panel.getGameClient().getAvatar().getInventory().get(3);
				return true;
			}
		} else if (i == 4) {
			if (i < (panel.getGameClient().getAvatar().getInventory().size())
					&& (panel.getGameClient().getAvatar().getInventory().get(4)) != null) {
				System.out.println("IN BOX 4");
				item = panel.getGameClient().getAvatar().getInventory().get(4);
				return true;
			}
		}

		return false;
	}


	public void mouseEntered(MouseEvent e) {
		// System.out.println("Mouse entered at " + e.getX() + ", " + e.getY());
	}

	public void mouseExited(MouseEvent e) {
		// System.out.println("Mouse exited at " + e.getX() + ", " + e.getY());
	}

}