package rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import networking.ChatMessage;

public class DrawChat {

	JPanel panel;

	public DrawChat(JPanel panel){
		this.panel = panel;
	}

	public void redraw(Graphics g, List<ChatMessage> chatMessages, String currentMessage){
		drawChat(g, chatMessages, currentMessage);
	}

	private void drawChat(Graphics g, List<ChatMessage> chatMessages, String currentMessage) {

		//draw a box to make the background dark
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

		g.setColor(Color.GRAY);
		int x = offset;
		int y = panel.getHeight() - offset - fontSize;
		g.drawString(currentMessage, x, y);
	}
}
