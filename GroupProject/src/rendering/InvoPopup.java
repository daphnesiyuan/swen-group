package rendering;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InvoPopup extends JPopupMenu{

	JMenuItem Use;
	JMenuItem Drop;

    public InvoPopup(){
        Use = new JMenuItem("Use");
        Drop = new JMenuItem("Drop");
        add(Use);
        add(Drop);
    }
}


