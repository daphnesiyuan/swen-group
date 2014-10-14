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

    public InvoPopup(DrawingPanel p, boolean isOpenable){
    	panel = p;
        Drop = new JMenuItem("Drop");
        add(Drop);
        Drop.addActionListener(this);

        System.out.println("in invo pop up constructor");

        if(isOpenable){
        	Open = new JMenuItem("Use");
        	add(Open);
        	Drop.addActionListener(this);
        }

    }

	@Override
	public void actionPerformed(ActionEvent e) {

		System.out.println("action listener");
		if ((e.getActionCommand()).equals("Open")) {
			panel.getGameClient().getAvatar().useItem(item); //for item
	    }

	    else if ( (e.getActionCommand()).equals("Drop") ){
	    	Move move = new Move((panel.getGameClient()).getPlayer(), "drop", itemIndex );

	    	try {
				panel.getGameClient().sendMoveToServer(move);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	    }
	}

	public void sendItem(Item i, int num){
		item=i;
		itemIndex=num;
	}


}


