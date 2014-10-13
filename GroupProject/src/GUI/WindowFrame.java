package GUI;

import java.awt.BorderLayout;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import networking.GameClient;
import networking.Player;


public class WindowFrame extends JFrame {
	private TopMenu topMenu;
	private DrawingPanel panel;
	private Graphics2D graphics; // for leon later?
	// private Rendering render=new Rendering(); //Leon's object
	// private Direction: n s e w


	public static DrawingPanel canvas;
	public static GameClient gameClient;
	public static Player player;


	public WindowFrame() {
		super("Game title");
		JFrameSetUp();
		JPanelStuff();

		//
		this.addKeyListener(panel.getKeyB());
	}

	/**
	 * @author Daphne Wang and Leon North
	 */
	public void JFrameSetUp() {
		topMenu = new TopMenu(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1280, 720);
		setVisible(true); // make sure we are visible!
	}

	/*
	 * public void refreshGraphics(){ //render.redraw(graphics, room, character,
	 * direction); //TO BE FILLED LATER: we get room and char from client: jimmy
	 * }
	 */
	/**
	 * @author Daphne Wang and Leon North
	 */
	public void JPanelStuff() {
		System.out.println("In JPanel setups");
		panel = new DrawingPanel(this);
		add(panel, BorderLayout.CENTER); // add canvas
		setVisible(true); // make sure we are visible!
		panel.repaint(); // repaint susses the graphics object
	}
}