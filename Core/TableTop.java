package Core;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TableTop extends JFrame {

	private JPanel contents,topPane;
	private JLabel roundLabel;
	private JLabel playerTurnLabel;
	
	private Tile[][] board = new Tile[11][11];
	private ArrayList<Pawn> pawn;
	
	public TableTop(ArrayList<Pawn> pawns,Game game) {
		this.pawn = pawns;
		this.setTitle("Hnefatafl");
		this.setSize(800, 800);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		contents = new JPanel(new GridLayout(11,11));
		
		topPane = new JPanel(new GridLayout(2,1));
		topPane.setPreferredSize(new Dimension(this.getWidth(),100));
		
		roundLabel = new JLabel("Round: 1", SwingConstants.CENTER);
		playerTurnLabel = new JLabel("Player 1 plays", SwingConstants.CENTER);
		topPane.add(roundLabel);
		topPane.add(playerTurnLabel);
		
		this.add(topPane, BorderLayout.PAGE_START);
		
		//Add the tiles on the board
		for(int j = 0; j < 11; j++) {
			for(int i = 0; i < 11; i++) {
				board[i][j] = new Tile(i,j);
				
				//The tiles that are restricted to the pawns except for the king
				if(j == 0 && i == 0 || j == 10 && i == 0 || j == 0 && i == 10 || j == 10 && i == 10 || j == 5 && i == 5)
					board[i][j].setRestricted(true);
				
				board[i][j].addActionListener(game.new ButtonHandler(board,i,j));
				
				contents.add(board[i][j]);
			}
		}
		
		this.add(contents);
		drawBoard();
		clearBackground();
		
	}
	
	public void disablePlayerPawns(int player) {
		int xPos,yPos;
		
		if(player == 2) {
			for(Pawn tilePawn : pawn) {
				xPos = tilePawn.getPosX();
				yPos = tilePawn.getPosY();
				
				if(tilePawn.getPlayer().getID() == 1) {
					board[xPos][yPos].setEnabled(false);
				}
				else {
					board[xPos][yPos].setEnabled(true);
				}
			}
		}
		else {
			for(Pawn tilePawn : pawn) {
				xPos = tilePawn.getPosX();
				yPos = tilePawn.getPosY();
				
				if(tilePawn.getPlayer().getID() == 2) {
					board[xPos][yPos].setEnabled(false);
				}
				else {
					board[xPos][yPos].setEnabled(true);
				}
			}
		}
	}
	
	/**
	 * Draw the pawns on the corresponding tiles
	 */
	public void drawBoard() {
		for(Pawn tilePawn : pawn) {
			int i = tilePawn.getPosX();
			int j = tilePawn.getPosY();
			
			board[i][j].setText("o");
			board[i][j].setOccupied(true);
			board[i][j].setPawn(tilePawn);
			
			if((tilePawn.getPlayer().getID() == 2)) {
				board[i][j].setForeground(Color.WHITE);
			}
			else {
				board[i][j].setForeground(Color.BLACK);
			}
		}
	}
	
	/**
	 * Set the background color of the tiles 
	 */
	public void clearBackground() {
		for(int j = 0; j < 11; j++) {
			for(int i = 0; i < 11; i++) {
				board[i][j].setBackground(new Color(219,139,71));
			}
		}
	}
	
	public void displayPlayer(int player) {
		playerTurnLabel.setText("Player " + (player) + " plays");
	}
	
	public void displayRound(int round) {
		roundLabel.setText("Round: " + (round + 1));
	}
	
}
