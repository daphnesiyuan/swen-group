package GUI;

import gameLogic.Item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InvoPopup extends JPopupMenu implements ActionListener{

	JMenuItem Open;
	JMenuItem Drop;
	Item item;

	DrawingPanel panel;

    public InvoPopup(DrawingPanel p, boolean isOpenable){
    	panel = p;
        Drop = new JMenuItem("Drop");

        add(Drop);

        if(isOpenable){
        	Open = new JMenuItem("Use");
        	add(Open);
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {

		if ((e.getActionCommand()).equals("Open")) {
			panel.getGameClient().getAvatar().useItem(item); //for item
	    }

	    else if ( (e.getActionCommand()).equals("Drop") ){
	    	System.out.println("items in invo before="+ panel.getGameClient().getAvatar().getInventory().size() );
	    	panel.getGameClient().getAvatar().dropItem(item);
	    	System.out.println("items in invo after="+ panel.getGameClient().getAvatar().getInventory().size() );

	    	panel.repaint();
	    }
	}

	public void getItem(Item i){
		item=i;
	}
}


