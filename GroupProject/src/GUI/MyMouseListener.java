package GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import rendering.InvoPopup;

public class MyMouseListener implements MouseListener{

	private DrawingPanel panel;
	InvoPopup pop;

	public MyMouseListener(DrawingPanel dp){
		panel = dp;
	}

	/**
	 * A helper method which registers when a mouse has been clicked and then sends the location of the mouse click to the panel
	 */
    public void mouseClicked(MouseEvent e) {
        panel.sendClick(e.getX(), e.getY());

    }


    public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger()) //for inventory interactions
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    /**
     * Helper method to deal with the right clicking mini menu interacting with inventory
     * @param e
     */
    private void doPop(MouseEvent e){
        InvoPopup menu = new InvoPopup();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    public void mouseEntered(MouseEvent e) {
        //System.out.println("Mouse entered at " + e.getX() + ", " + e.getY());
    	}

    public void mouseExited(MouseEvent e) {
        //System.out.println("Mouse exited at " + e.getX() + ", " + e.getY());
    	}



}