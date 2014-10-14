package GUI;

import gameLogic.Item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import networking.Move;
import rendering.Direction;

public class InvoPopup extends JPopupMenu implements ActionListener{

	JMenuItem Open;
	JMenuItem Drop;
	Item item;
	int itemIndex;

	DrawingPanel panel;

	int count;

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

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());

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

	public void sendItem(Item i, int num){
		item=i;
		itemIndex=num;
	}


}


