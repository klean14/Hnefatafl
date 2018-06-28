package Core;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainMenu extends JFrame implements ActionListener{
	private JButton newGame,quit;
	
	
	public MainMenu() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		this.setVisible(true);
		this.setSize(300, 300);
		this.setResizable(false);
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		quit = new JButton("Quit");
		quit.addActionListener(this);
		newGame.setAlignmentX(CENTER_ALIGNMENT);
		quit.setAlignmentX(CENTER_ALIGNMENT);
		add(newGame);
		add(quit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// If newGame button was pressed
		if(e.getSource() == newGame) {
			
			Game game = new Game();
			this.setVisible(false);
			
		}else if (e.getSource()==quit) {
			System.exit(0);
		}
		
	}

}
