package Core;

import java.util.ArrayList;

import CommandLine.TableTopCMD;
import GUI.TableTopGUI;

public class GameLogic implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The tile that was selected first **/
	private TileInterface selectedTile = null;
	/** The pawn that was on the selected tile **/
	private Pawn selectedPawn = null;
	
	/** An array of pawns **/
	private ArrayList<Pawn> pawn;
	
	/** The number of rounds played **/
	private int round = 0;
	
	private Rules rules = new Rules();
	
	/****************Getters and setters******************/
	
	public int getRound() {return round;}
	public void setRound(int round) {this.round = round;}
	
	/*****************************************************/

	public GameLogic(String game, boolean gui) {
		pawn = null;
		Player p1 = new Player("",1);
		Player p2 = new Player("",2);

		switch(game) {
		case "Hnefatafl":
			pawn = PawnGenerator.generatePawnsHnefatafl(p1,p2);
			if(gui)
				new TableTopGUI(pawn,this,11,game).setVisible(true);
			else {
				new TableTopCMD(pawn,this,11);
			}
			break;
		case "Tablut":
			pawn = PawnGenerator.generatePawnsTablut(p1,p2);
			if(gui)
				new TableTopGUI(pawn,this,9,game).setVisible(true);
			else {	
				new TableTopCMD(pawn,this,9);
			}
			break;
		case "Tawlbawrdd":
			pawn = PawnGenerator.generatePawnsTawlbawrdd(p1,p2);
			if(gui)
				new TableTopGUI(pawn,this,11,game).setVisible(true);
			else {
				new TableTopCMD(pawn,this,11);
			}
		}
	}

	/**
	 * Check if there are any pawns between the 2 tiles
	 * @param thisX X coordinate of old tile
	 * @param thatX X coordinate of new tile
	 * @param thisY Y coordinate of old tile
	 * @param thatY Y coordinate of new tile
	 * @return true if there are pawns between, otherwise false
	 */
	public boolean pawnsBetween(TileInterface[][] board, int thisX, int thatX,int thisY,int thatY) {
		int resultX = thisX - thatX;
		int resultY = thisY - thatY;
		// Moves to the right
		if(resultX < 0) {
			for(int i = thisX+1; i < thatX; i++) {
				if(board[i][thisY].isOccupied()) {return true;}
			}
		}
		//Moves to the left
		else if(resultX > 0) {
			for(int i = thisX-1; i > thatX; i--) {
				if(board[i][thisY].isOccupied()) {return true;}
			}
		}
		//Moves downwards
		else if(resultY < 0) {
			for(int j = thisY+1; j < thatY; j++) {
					if(board[thisX][j].isOccupied()) {return true;}
			}
		}
		//Moves upwards
		else {
			for(int j = thisY-1; j > thatY; j--) {
				if(board[thisX][j].isOccupied()) {return true;}
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
	
	/**
	 * 
	 * @param board
	 * @param x x Position of the selected Tile
	 * @param y y position of the selected Tile
	 */
	public void nextRound(TileInterface[][] board,int x, int y) {
		try { 
			// If there is a pawn in the tile pressed, save the selected tile for later use 
			if(board[x][y].isOccupied()) {
				selectedTile = board[x][y];
				selectedPawn = findPawn(selectedTile.getPosX(),selectedTile.getPosY());
			}
			
			//Restricted tile 
			else if((board[x][y].isRestricted() && !selectedPawn.isKing())) {
				System.out.println("Warning! Restricted tile tried to be accessed");
			} 
			
			// If there is no pawn on the tile pressed
			else { 
				// Check that the correct player played
				if(playerTurn()) {
					
					// Only allowed to move in a straight line ( ^ is xor operation)
					if(legalMove(x, y)) {
						if(!pawnsBetween(board, selectedTile.getPosX(),x,selectedTile.getPosY(),y)) {
		
							selectedTile.setOccupied(false);
							selectedTile.setPawn(null);
							
							// Move the selectedPawn to the new location
							selectedPawn.move(x, y);
							
							rules.checkCapture(board, pawn, selectedPawn, GameLogic.this);
							
							// Increment round when a player made a move
							round++;
							
							// Reset the selection
							selectedPawn = null;
							selectedTile = null;
						}else {
							System.out.println("Pawn detected in between");
						}
							
					}
					else {
						System.out.println("Invalid move");
					}
				}
				else {
					System.out.println("Wrong player's turn");
				}
			}
		}
		catch(NullPointerException error) {
			System.out.println("No pawn selected");
		}
	}
	public boolean legalMove(int x, int y) {
		return selectedTile.getPosX() == x ^ selectedTile.getPosY() == y;
	}
	
	public boolean playerTurn() {
		return (round % 2 )+ 1 == selectedPawn.getPlayer().getID();
	}
	
	
}
