package rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import networking.ChatMessage;

/**
 * Draws the chatroom to the panel
 *
 * @author Leon North
 *
 */
public class DrawChat {

	private JPanel panel;

	public DrawChat(JPanel panel){
		this.panel = panel;
	}

	/**
	 * Draws the chatroom
	 *
	 * @param g : Graphics objects
	 * @param chatMessages : List of chatMessage objects
	 * @param currentMessage : the current message being created
	 */
	public void redraw(Graphics g, List<ChatMessage> chatMessages, String currentMessage){
		g.setColor(new Color(0f, 0f, 0f, 0.5f));
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

		int offset = 30;

		int fontSize = (int)((panel.getWidth() / 128)*2);
		g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

		for (int i = 0; i < chatMessages.size(); i++){
			g.setColor(chatMessages.get(i).color);
			String nameAndMessage = chatMessages.get(i).toString();
			int y = (fontSize * i)+offset;
			int x = 0 + offset;
			g.drawString(nameAndMessage, x, y);
		}

		int x = offset;
		int y = panel.getHeight() - offset - fontSize;

		g.setColor(Color.BLUE);
		g.drawString(currentMessage, x, y);
	}
}
