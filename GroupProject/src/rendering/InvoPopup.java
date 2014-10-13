package rendering;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InvoPopup extends JPopupMenu implements ActionListener{

	JMenuItem Use;
	JMenuItem Drop;

    public InvoPopup(){
        Use = new JMenuItem("Use");
        Drop = new JMenuItem("Drop");
        add(Use);
        add(Drop);
    }

	@Override
	public void actionPerformed(ActionEvent e) {

		if ((e.getActionCommand()).equals("Use")) {
			
	    }

	    else if ( (e.getActionCommand()).equals("Drop") ){

	    }
	}
}


