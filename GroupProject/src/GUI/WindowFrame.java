package GUI;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowFrame extends JFrame {

	private Canvas leonsThing;
	private TopMenu topMenu;
	private JPanel startPanel;

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

		//startButton.setRolloverEnabled(true);
		//startButton.setRolloverIcon(new RolloverIcon(start));
	}

	public void setButton(JButton button){
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setLocation(1000, 500);
		button.addActionListener(handler);


		startPanel.add(button);
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
