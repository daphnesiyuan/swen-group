package GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotion implements MouseMotionListener{

	private DrawingPanel panel;

	public MouseMotion(DrawingPanel d){
		panel = d;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * A method helper for mouse hovering/movement which registers when the cursor is moved and sends this change to our panel class
	 */
	@Override
    public void mouseMoved(MouseEvent e){
    	panel.sendHover(e.getX(), e.getY()); //maybe change to sendHover
    }

}
