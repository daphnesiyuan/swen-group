package GUI;

import gameLogic.Item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InvoPopup extends JPopupMenu implements ActionListener{

	JMenuItem Use;
	JMenuItem Drop;
	Item item;

	DrawingPanel panel;

    public InvoPopup(DrawingPanel p){
    	panel = p;
        Use = new JMenuItem("Use");
        Drop = new JMenuItem("Drop");
        add(Use);
        add(Drop);
    }

	@Override
	public void actionPerformed(ActionEvent e) {

		if ((e.getActionCommand()).equals("Use")) {
			panel.getGameClient().getAvatar().useItem(item); //for item
	    }

	    else if ( (e.getActionCommand()).equals("Drop") ){
	    	panel.getGameClient().getAvatar().dropItem(item);
	    }
	}

	public void getItem(Item i){
		item=i;
	}
}


