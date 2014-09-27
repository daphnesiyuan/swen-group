package GUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowFrame extends JFrame {

	private JPanel mainPanel;
	private TopMenu topMenu;
	private JPanel startPanel;
	private Graphics2D graphics; //for leon later?
	//private Rendering render=new Rendering(); //Leon's object
	//private Direction: n s e w

	//buttons
	JButton startButton;
	JButton loadButton;
	JButton helpButton;

	//action handler
	private Handler handler;

	public WindowFrame() {
		super("Game title");
		JFrameSetUp();
		JPanelStuff();

		mainPanel = new JPanel();
		//add the graphics to the panel
		buttons();
	}

	public void JFrameSetUp() {
		topMenu = new TopMenu(this);
		handler = new Handler();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1000, 700);

		setResizable(true); // prevent us from being resizeable
		setVisible(true); // make sure we are visible!

	}

	public void refreshGraphics(){
		//render.redraw(graphics, room, character, direction); //TO BE FILLED LATER: we get room and char from client: jimmy
	}

	public void JPanelStuff() {
		startPanel = new JPanel( new BorderLayout() );
		startPanel.setBackground(Color.WHITE);

		this.getContentPane().add(startPanel);
	}

	public void buttons(){
		ImageIcon start = new ImageIcon(WindowFrame.class.getResource("startMenuImages/start.png"));
		ImageIcon load = new ImageIcon(WindowFrame.class.getResource("startMenuImages/load.png"));
		ImageIcon help = new ImageIcon(WindowFrame.class.getResource("startMenuImages/help.png"));

		startButton = new JButton(start);
		loadButton = new JButton(load);
		helpButton = new JButton(help);

		setButton(startButton);


	}

	public void setButton(JButton button){
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setLocation(1000, 500);
		button.addActionListener(handler);


		startPanel.add(button, BorderLayout.EAST);
		startPanel.validate();

	}






	/*
	 * Private inner class to handle action listeners - dealing with buttons
	 */

	private class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){

			if(e.getSource()==startButton){

				System.out.println("Pressed start button");
			}



		}
	}


}
