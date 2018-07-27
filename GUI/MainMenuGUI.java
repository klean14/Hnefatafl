package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Core.GameLogic;

public class MainMenuGUI extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton newGameButton,quitButton,loadGameButton;
	private JPanel panel;
	private JComboBox<String> gamesDrop;
	private static String filename = "save_game_gui.ser";
	
	public MainMenuGUI() {
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
		this.setVisible(true);
		this.setSize(300, 300);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Main Menu");
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		String[] gameStrings = {"Hnefatafl", "Tablut", "Tawlbawrdd"};
		
		// JComboBox
		gamesDrop = new JComboBox<String>(gameStrings);
		
		// JButtons
		newGameButton = new JButton("New Game");
		newGameButton.setAlignmentX(CENTER_ALIGNMENT);
		quitButton = new JButton("Quit");
		quitButton.setAlignmentX(CENTER_ALIGNMENT);
		loadGameButton = new JButton("Load Game");
		loadGameButton.setAlignmentX(CENTER_ALIGNMENT);
		
		// Action Listeners
		newGameButton.addActionListener(this);
		quitButton.addActionListener(this);
		loadGameButton.addActionListener(this);
		
		panel.add(gamesDrop,gbc);
		panel.add(newGameButton,gbc);
		panel.add(loadGameButton,gbc);
		panel.add(quitButton,gbc);
		
		this.add(panel);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// If newGame button was pressed
		if(e.getSource() == newGameButton) {
			
			new GameLogic(gamesDrop.getSelectedItem().toString(),true);
			dispose();
			
		}else if (e.getSource() == quitButton) {
			System.exit(0);
		}
		else if (e.getSource() == loadGameButton) {
			loadGame();
		}
		
	}

	/**
	 * Loads the game state
	 */
	private void loadGame() {
		TableTopGUI tt = null;
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			
			tt = (TableTopGUI) in.readObject();
			tt.setUpListeners();
			tt.setVisible(true);
			
			file.close();
			in.close();
			
			dispose();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "File not found", "File Error", JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
	}

}
