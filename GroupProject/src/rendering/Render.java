package rendering;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Render extends javax.swing.JFrame{


	public static Drawing canvas;

	public Render () {



		canvas = new Drawing();
		setLayout(new BorderLayout()); // use border layour
		add(canvas, BorderLayout.CENTER); // add canvas
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // pack components tightly together
		//setResizable(false); // prevent us from being resizeable
		setVisible(true); // make sure we are visible!
		this.addKeyListener(canvas);

	}

	public void repaint(){
		canvas.repaint();
	}



	public static void main(String[] args) {
		new Render();

	}

}
