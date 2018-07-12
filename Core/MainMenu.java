package Core;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame implements ActionListener{
	private JButton newGame,quit;
	private JPanel panel;
	private JComboBox<String> gamesDrop;
	
	public MainMenu() {
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
		this.setVisible(true);
		this.setSize(300, 300);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		String[] gameStrings = {"Hnefatafl", "Tablut", "Tawlbawrdd"};
		
		// JComboBox
		gamesDrop = new JComboBox<String>(gameStrings);
		
		// JButtons
		newGame = new JButton("New Game");
		newGame.setAlignmentX(CENTER_ALIGNMENT);
		quit = new JButton("Quit");
		quit.setAlignmentX(CENTER_ALIGNMENT);
		
		// Action Listeners
		newGame.addActionListener(this);
		quit.addActionListener(this);
		
		panel.add(gamesDrop,gbc);
		panel.add(newGame,gbc);
		panel.add(quit,gbc);
		
		this.add(panel);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// If newGame button was pressed
		if(e.getSource() == newGame) {
			
			new Game(gamesDrop.getSelectedItem().toString());
			this.setVisible(false);
			
		}else if (e.getSource()==quit) {
			System.exit(0);
		}
		
	}

}
