package GUI;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			
			g.drawImage(ImageIO.read(getClass().getResource(("board.jpg"))),0,0,this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			System.err.println("File error");
		}
	}

}
