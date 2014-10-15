package GUI;

import gameLogic.Item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import networking.Move;
import rendering.Direction;

/**
 * This class takes care of the small click-able menu when you want to interact with the items in the inventory
 * @author wangdaph
 */
public class InvoPopup extends JPopupMenu implements ActionListener{

	JMenuItem Open;
	JMenuItem Drop;
	Item item;
	int itemIndex;

	DrawingPanel panel;

	int count;

	/**
	 * Constructor which takes care of every new instance of an inventory popup menu
	 * The boolean is necessary to distinguish whether an invopopup is necessary or not based
	 * on factors like whether the mouse was clicked on an actual inventory space, and if
	 * the inventory space actually has an item in it
	 *
	 * @param p DrawingPanel
	 * @param isOpenable boolean to distinguish whether an invopopup is valid
	 *
	 * @author Daphne Wang
	 */
	public InvoPopup(DrawingPanel p, boolean isOpenable){
		panel = p;
		Drop = new JMenuItem("Drop");
		add(Drop);
		Drop.addActionListener(this);

		if(isOpenable){
			Open = new JMenuItem("Open");
			add(Open);
			Open.addActionListener(this);
		}

		count = 0;
	}

	/**
	 *Since the class is an action listener this works in conjunction to make appropriate reactions to opening or dropping
	 *items in the inventory.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getActionCommand()).equals("Open")) {
			Move move = new Move((panel.getGameClient()).getPlayer(), "open", itemIndex );
			try {
				panel.getGameClient().sendMoveToServer(move);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}


		else if ( (e.getActionCommand()).equals("Drop") ){
			if(count == 0){
				Move move = new Move((panel.getGameClient()).getPlayer(), "drop", itemIndex );

				try {
					panel.getGameClient().sendMoveToServer(move);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				count++;
			}
		}
	}

	/**
	 * Helper method used from the MouseListener class which detects the latest item chosen to be interacted
	 * with via clicking
	 * @param i The actual item that was clicked on
	 * @param num The index of that item in the inventory storage
	 *
	 * @author Daphne Wang
	 */
	public void sendItem(Item i, int num){
		item=i;
		itemIndex=num;
	}


}


