package GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener{

	private DrawingPanel panel;

	public MyMouseListener(DrawingPanel dp){
		panel = dp;
	}

	/**
	 * A helper method which registers when a mouse has been clicked and then sends the location of the mouse click to the panel
	 */
    public void mouseClicked(MouseEvent e) {
        panel.sendClick(e.getX(), e.getY());


    }
    public void mousePressed(MouseEvent e) {
        //System.out.println("Mouse pressed at " + e.getX() + ", " + e.getY());
    	}

    public void mouseReleased(MouseEvent e) {
        //System.out.println("Mouse released at " + e.getX() + ", " + e.getY());
    	}

    public void mouseEntered(MouseEvent e) {
        //System.out.println("Mouse entered at " + e.getX() + ", " + e.getY());
    	}

    public void mouseExited(MouseEvent e) {
        //System.out.println("Mouse exited at " + e.getX() + ", " + e.getY());
    	}

}