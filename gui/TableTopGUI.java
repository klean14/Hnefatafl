package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Core.GameLogic;
import Core.Pawn;
import Core.Rules;

public class TableTopGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contents,topPane;
	
	private JLabel roundLabel, playerTurnLabel;
	
	private JButton saveGameButton;
	
	private TileButton[][] board;
	
	private int boardSize;
	
	private ArrayList<Pawn> pawn;
	
	private GameLogic game;
	
	private Rules rules = new Rules();

	private static String filename = "save_game_gui.ser";
	
	public TableTopGUI(ArrayList<Pawn> pawns,GameLogic game, int size, String title) {

		this.game = game;
		this.boardSize = size;
		this.board = new TileButton[size][size];
		this.pawn = pawns;
		
		this.setTitle(title);
		this.setSize(800, 800);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		// JLabels
		roundLabel = new JLabel("Round: 1", SwingConstants.CENTER);
		playerTurnLabel = new JLabel("Player 1 plays", SwingConstants.CENTER);
		
		// JPanels
		contents = new JPanel(new GridLayout(boardSize,boardSize));
		
		// JButtons
		saveGameButton = new JButton("Save Game");
		
		topPane = new JPanel(new GridLayout(3,1));
		topPane.setPreferredSize(new Dimension(this.getWidth(),100));
		topPane.add(saveGameButton,SwingConstants.CENTER);
		topPane.add(roundLabel);
		topPane.add(playerTurnLabel);
		
		createBoard();
		setUpListeners();
		
		this.add(topPane, BorderLayout.PAGE_START);
		this.add(contents);

		printBoard();
		clearBackground();
	}

	/**
	 * Method to set up the listeners for the buttons. This had to be a different method because listeners are not serializable
	 */
	public void setUpListeners() {
		addWindowListener(new WindowAdapter() {
			 @Override
			 public void windowClosing(WindowEvent e) {
				 new MainMenuGUI();
				 dispose();
			 }        
		});
		
		saveGameButton.addActionListener(new ButtonHandler());
		
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row].addActionListener(new ButtonHandler(board,col,row));
			}
		}
	}
	

	/**
	 * Creates the TileButtons
	 */
	private void createBoard() {
		//Add the tiles on the board
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row] = new TileButton(col,row);
				board[col][row].setLayout(new BorderLayout());
				
				//The tiles that are restricted to the pawns except for the king
				// Temporary solution for the restricted to be the corners and the center of the board. not always the case
				if(row == 0 && col == 0 || row == boardSize-1 && col == 0 || row == 0 && col == boardSize-1 || row == boardSize-1 && col == boardSize-1 || row == (boardSize-1)/2 && col == (boardSize-1)/2) {
					board[col][row].setRestricted(true);
					board[col][row].setText("X");
				}

				contents.add(board[col][row]);
			}
		}
	}

	/**
	 * Draw the pawns on the corresponding tiles
	 */
	public void printBoard() {
		for(TileButton[] tileRow : board) {
			for(TileButton tile : tileRow) {
				int row = tile.getPosX();
				int col = tile.getPosY();
				
				Pawn tilePawn = game.findPawn(row, col);
				if(tilePawn != null) {
					tile.setPawn(tilePawn);
					tile.setOccupied(true);
					
					if(tilePawn.isKing())
						tile.setText("K");
					else
						tile.setText("o");
					
					
					if((tilePawn.getPlayer().getID() == 2)) {
						tile.setForeground(Color.WHITE);
					}
					else {
						tile.setForeground(Color.BLACK);
					}
				}
				else if(!tile.isRestricted()){
					tile.setPawn(null);
					tile.setOccupied(false);
					tile.setText("");
					tile.setEnabled(true);
				}
			}
		}
	}
	
	/**
	 * Set the background color of the tiles 
	 */
	public void clearBackground() {
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row].setBackground(new Color(219,139,71));
			}
		}
	}
	
	/**
	 * Update the label for which player plays
	 * @param player Player whose turn is
	 */
	public void displayPlayer(int player) {
		playerTurnLabel.setText("Player " + (player) + " plays");
	}
	
	/**
	 * Update the label for how many rounds have passed
	 * @param round Number of round to display
	 */
	public void displayRound(int round) {
		roundLabel.setText("Round: " + (round + 1));
	}
	
	/**
	 * Method that follows the game sequence
	 * @param x x position of the Tile pressed
	 * @param y y position of the Tile pressed
	 */
	public void gameSequence(int x, int y) {
		clearBackground();
		
		game.nextRound(board, x, y);
		
		if(board[x][y].isOccupied() && game.playerTurn()) {
			highlightTiles(x,y);
			board[x][y].setBackground(Color.YELLOW);
		}
		
		
		int round = game.getRound();
		displayRound(round);
		displayPlayer((round % 2)+1);
		printBoard();
		

		if(rules.checkEnd(pawn, board)) {
			JOptionPane.showMessageDialog(null, "Congratulations Player " + ((--round % 2) + 1));
			new MainMenuGUI();
			dispose();
		}
	}
	
	/**
	 * Highlight the available tiles the pawn can move to
	 */
	private void highlightTiles(int x, int y) {

		for(int i = x+1; i < board.length; i++) {
			if(!board[i][y].isOccupied()) {
				board[i][y].setBackground(new Color(244,164,96));
			}
			else {
				break;
			}
		}
		for(int i = x-1; i < board.length; i--) {
			if(i < 0 || board[i][y].isOccupied() ) {
				break;
			}
			else {
				board[i][y].setBackground(new Color(244,164,96));
			}
		}
		for(int i = y+1; i < board.length; i++) {
			if(!board[x][i].isOccupied()) {
				board[x][i].setBackground(new Color(244,164,96));
			}
			else {
				break;
			}
		}
		for(int i = y-1; i < board.length; i--) {
			if(i < 0 || board[x][i].isOccupied() ) {
				break;
			}
			else {
				board[x][i].setBackground(new Color(244,164,96));
			}
		}
	}
	
	/********* Button Handler class *********/
	public class ButtonHandler implements ActionListener{
//		private TileButton[][] board;
		private int x;
		private int y;
		
		/**
		 * Default constructor
		 */
		public ButtonHandler() {
			
		}
		
		/**
		 * Constructor specifically for TileButton instances
		 * @param board The 2D array of TileButtons
		 * @param x x position of the TileButton
		 * @param y y position of the TileButton
		 */
		public ButtonHandler(TileButton[][] board,int x, int y) {
//			this.board = board;
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			
			if(e.getSource() == saveGameButton) {saveGame();}
			else {gameSequence(x,y);}

		}

		/**
		 * Saves the game state
		 */
		private void saveGame() {
			try {
				FileOutputStream file = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(file);
				
				out.writeObject(TableTopGUI.this);
				
				
				file.close();
				out.close();
				
				JOptionPane.showMessageDialog(null, "Game Saved!");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	}
	
	
	/*********************************************/
}
