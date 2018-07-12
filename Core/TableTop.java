package Core;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TableTop extends JFrame {

	private JPanel contents,topPane;
	
	private JLabel roundLabel, playerTurnLabel;
	
	private Tile[][] board;
	
	private int boardSize;
	
	private ArrayList<Pawn> pawn;
	
	private Game game;
	
	public TableTop(ArrayList<Pawn> pawns,Game game, int size) {

		this.game = game;
		this.boardSize = size;
		this.board = new Tile[size][size];
		this.pawn = pawns;
		
		this.setTitle("Hnefatafl");
		this.setSize(800, 800);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		// JLabels
		roundLabel = new JLabel("Round: 1", SwingConstants.CENTER);
		playerTurnLabel = new JLabel("Player 1 plays", SwingConstants.CENTER);
		
		// JPanels
		contents = new JPanel(new GridLayout(boardSize,boardSize));
		
		topPane = new JPanel(new GridLayout(2,1));
		topPane.setPreferredSize(new Dimension(this.getWidth(),100));
		topPane.add(roundLabel);
		topPane.add(playerTurnLabel);
		
		createBoard();
		
		this.add(topPane, BorderLayout.PAGE_START);
		this.add(contents);

		drawBoard();
		clearBackground();
		
	}
	

	private void createBoard() {
		//Add the tiles on the board
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row] = new Tile(col,row);
				board[col][row].setLayout(new BorderLayout());
				
				//The tiles that are restricted to the pawns except for the king
				// Temporary solution for the restricted to be the corners and the center of the board. not always the case
				if(row == 0 && col == 0 || row == boardSize-1 && col == 0 || row == 0 && col == boardSize-1 || row == boardSize-1 && col == boardSize-1 || row == (boardSize-1)/2 && col == (boardSize-1)/2) {
					board[col][row].setRestricted(true);
					board[col][row].setText("X");
				}

				board[col][row].addActionListener(game.new ButtonHandler(board,col,row));
				
				contents.add(board[col][row]);
			}
		}
	}

	/**
	 * Disables the player's pawns
	 * @param player The player whose pawns should be disabled
	 */
	public void disablePlayerPawns(int player) {
		
		/*for(Tile[] row: board) {
			for(Tile tile: row) {
				if(tile.isOccupied()) {
					// Removing the action listeners of the opponent's pawns
						if(tile.getPawn().getPlayer().getID() != player) {
							for( ActionListener al : tile.getActionListeners() ) {
								tile.removeActionListener(al);
							}
//							board[row][col].setEnabled(false);
						}
						else {
//							tile.addActionListener(game.new ButtonHandler(board,tile.getPosX(),tile.getPosY()));
							
//								board[row][col].setEnabled(true);
						}
					}
				}
			}*/
		

		int row,col;
		for(Pawn tilePawn : pawn) {
			row = tilePawn.getPosX();
			col = tilePawn.getPosY();
			
			if(tilePawn.getPlayer().getID() != player) {
//				for( ActionListener al : board[row][col].getActionListeners() ) {
//					board[row][col].removeActionListener(al);
//				}
				board[row][col].setEnabled(false);
			}
			else {
//				board[col][row].addActionListener(game.new ButtonHandler(board,col,row));
				
				board[row][col].setEnabled(true);
			}
		}
	
	}
	
	/**
	 * Draw the pawns on the corresponding tiles
	 */
	public void drawBoard() {
		for(Pawn tilePawn : pawn) {
			int row = tilePawn.getPosX();
			int col = tilePawn.getPosY();
			
			board[row][col].setPawn(tilePawn);
			board[row][col].setOccupied(true);
			
			if(tilePawn.isKing())
				board[row][col].setText("K");
			else
				board[row][col].setText("o");
			
			
			if((tilePawn.getPlayer().getID() == 2)) {
				board[row][col].setForeground(Color.WHITE);
			}
			else {
				board[row][col].setForeground(Color.BLACK);
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
	
}
