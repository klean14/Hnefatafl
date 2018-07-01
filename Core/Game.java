package Core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Game {
	/** The tile that was selected first **/
	private Tile selectedTile = null;
	/** The pawn that was on the selected tile **/
	private Pawn selectedPawn = null;
	
	private TableTop tt;
	
	/** An array of pawns **/
	private ArrayList<Pawn> pawn;
	
	/** The number of rounds played **/
	private int round = 0;
	
	private Rules rules = new Rules();
	
	/****************Getters and setters******************/
	
	public int getRound() {return round;}
	public void setRound(int round) {this.round = round;}
	
	/*****************************************************/

	public Game(String game) {
		
		Player p1 = new Player("",1);
		Player p2 = new Player("",2);
		
		switch(game) {
		case "Hnefatafl":
			pawn = PawnGenerator.generatePawnsHnefatafl(p1,p2);
			tt = new TableTop(pawn,this,11);
			break;
		case "Tablut":
			pawn = PawnGenerator.generatePawnsTablut(p1,p2);
			tt = new TableTop(pawn,this,9);
			break;
		}
		
		
		tt.setVisible(true);
		tt.disablePlayerPawns(1);
	}

	/**
	 * Check if there are any pawns between the 2 tiles
	 * @param thisX X coordinate of old tile
	 * @param thatX X coordinate of new tile
	 * @param thisY Y coordinate of old tile
	 * @param thatY Y coordinate of new tile
	 * @return true if there are pawns between, otherwise false
	 */
	public boolean pawnsBetween(Tile[][] tile, int thisX, int thatX,int thisY,int thatY) {
		int resultX = thisX - thatX;
		int resultY = thisY - thatY;
		// Moves to the right
		if(resultX < 0) {
			for(int i = thisX+1; i < thatX; i++) {
				if(tile[i][thisY].isOccupied()) {return true;}
			}
		}
		//Moves to the left
		else if(resultX > 0) {
			for(int i = thisX-1; i > thatX; i--) {
				if(tile[i][thisY].isOccupied()) {return true;}
			}
		}
		//Moves downwards
		else if(resultY < 0) {
			for(int j = thisY+1; j < thatY; j++) {
					if(tile[thisX][j].isOccupied()) {return true;}
			}
		}
		//Moves upwards
		else {
			for(int j = thisY-1; j > thatY; j--) {
				if(tile[thisX][j].isOccupied()) {return true;}
			}
		}
		
		return false;
	}

	
	/**
	 * Find the Pawn object that sits on a specific tile
	 * @param x X coordinate of the tile
	 * @param y Y coordinate of the tile
	 * @return the Pawn object if found, otherwise return null
	 */
	public Pawn findPawn(int x, int y) {
		for(Pawn tilePawn : pawn) {
			if(tilePawn.getPosX() == x && tilePawn.getPosY() == y) {
				return tilePawn;
			}
		}
		
		return null;
	}
	
	/********* Button Handler class *********/
	public class ButtonHandler implements ActionListener{
		private Tile[][] board;
		private int x;
		private int y;
		
		
		public ButtonHandler(Tile[][] board,int x, int y) {
			this.board = board;
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			
			tt.clearBackground();
			
			try { 
				// If there is a pawn in the tile pressed, save the selected tile for later use 
				if(board[x][y].isOccupied()) {
					selectedTile = board[x][y];
					highlightTiles();
					selectedTile.setBackground(Color.YELLOW);
					selectedPawn = findPawn(selectedTile.getPosX(),selectedTile.getPosY());
				}
				
				//Restricted tile 
				else if((board[x][y].isRestricted() && !selectedPawn.isKing())) {
					System.out.println("Warning! Restricted tile tried to be accessed");
				} 
				
				// If there is no pawn on the tile pressed
				else { 
					// Only allowed to move in a straight line ( ^ is xor operation)
					if(selectedTile.getPosX() == x ^ selectedTile.getPosY() == y) {
						if(!pawnsBetween(board, selectedTile.getPosX(),x,selectedTile.getPosY(),y)) {
							selectedTile.setText("");
							
							selectedTile.setOccupied(false);
							selectedTile.setPawn(null);
							
							// Move the selectedPawn to the new location
							selectedPawn.move(x, y);
							
							rules.checkCapture(board, pawn, selectedPawn, Game.this);
							
							// Increment round when a player made a move
							round++;
							tt.displayRound(round);
							tt.displayPlayer((round % 2)+1);
							tt.drawBoard();
							
							// Disable the previous player's pawns based on which round it is
							tt.disablePlayerPawns((round % 2)+1);
							
							
							
							// Reset the selection
							selectedPawn = null;
							selectedTile = null;
						}else {
							System.out.println("Pawn detected in between");
						}
					}
				}
			}
			catch(NullPointerException error) {
				System.out.println("No pawn selected");
			}
		}
		
		/**
		 * Highlight the available tiles the pawn can move to
		 */
		private void highlightTiles() {
			for(int i = 0; i < board.length; i++) {
				board[x][i].setBackground(new Color(244,164,96));
				board[i][y].setBackground(new Color(244,164,96));
			}
			
		}
	}
	
	/*********************************************/
}
