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
	private static int round = 0;
	
	private Player[] player = new Player[2];
	
	private String gameName = new String();
	
	private int boardSize;
	
	
	/****************Getters and setters******************/
	
	public static int getRound() {return round;}
	public static void setRound(int round) {GameLogic.round = round;}
	
	public String getGameName() {return gameName;}
	
	public int getBoardSize() {return boardSize;}
	
	public ArrayList<Pawn> getPawn() {return pawn;}
	
	public Player[] getPlayer() {return player;}
	public void setPlayer(Player[] player) {this.player = player;}
	
	public Pawn getSelectedPawn() {return selectedPawn;}
	
	/*****************************************************/

	public GameLogic(String game, boolean gui) {
		gameName = game;
		pawn = null;
		player[0] = new Player("Player 1",1);
		player[1] = new Player("Player 2",2);
		
		new PawnGenerator();
		
		switch(game) {
		case "Hnefatafl":
			boardSize = 11;
			pawn = PawnGenerator.generatePawnsHnefatafl(player[0],player[1]);
			break;
		case "Tablut":
			boardSize = 9;
			pawn = PawnGenerator.generatePawnsTablut(player[0],player[1]);
			break;
		case "Tawlbawrdd":
			boardSize = 11;
			
			pawn = PawnGenerator.generatePawnsTawlbawrdd(player[0],player[1]);
			break;
		}
		
		if(gui) {
			new TableTopGUI(pawn,this,boardSize,game);
		}
		else {
			new TableTopCMD(pawn,this,boardSize);
		}
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
				selectedPawn = findPawn(x,y);
			}
			
			//Restricted tile 
			else if((board[x][y].isRestricted() && !(selectedPawn instanceof KingPawn))) {
				System.out.println("Warning! Restricted tile tried to be accessed");
			} 
			
			// If there is no pawn on the tile pressed
			else { 
				// Check that the correct player played
				if(Rules.playerTurn(round,selectedPawn.getPlayer().getID())) {
					
					// Only allowed to move in a straight line
					if(Rules.legalMove(selectedTile.getPosX(),selectedTile.getPosY(), x, y)) {
						if(!Rules.pawnsBetween(board, selectedTile.getPosX(),x,selectedTile.getPosY(),y)) {
		
							selectedTile.setOccupied(false);
							selectedTile.setPawn(null);
							
							// Move the selectedPawn to the new location
							selectedPawn.move(x, y);
							
							Rules.checkCapture(board, pawn, selectedPawn, GameLogic.this);
							
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
	
	
}
