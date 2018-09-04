package Core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import CommandLine.TableTopCLI;
import GUI.TableTopGUI;


public class GameLogic implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The tile that was selected first **/
	private Tile selectedTile = null;
	/** The pawn that was on the selected tile **/
	transient private Pawn selectedPawn = null;
	
	/** An array of pawns **/
	private ArrayList<Pawn> pawn;
	
	/** The number of rounds played **/
	private int round;
	
	private Player[] player = new Player[2];
	
	private String gameName = new String();
	
	private int boardSize;
	
	
	/****************Getters and setters******************/
	
	public int getRound() {return round;}
	public void decrementRound() {this.round--;}
	
	public String getGameName() {return gameName;}
	
	public int getBoardSize() {return boardSize;}
	
	public ArrayList<Pawn> getPawn() {return pawn;}
	public void setPawn(ArrayList<Pawn> pawn) {this.pawn = pawn;}
	
	public Player[] getPlayer() {return player;}
	public void setPlayer(Player[] player) {this.player = player;}
	
	public Pawn getSelectedPawn() {return selectedPawn;}
	
	/*****************************************************/

	public GameLogic() {}
	
	public GameLogic(String game, boolean gui) {
		gameName = game;
		round = 0;
		pawn = new ArrayList<Pawn>();
		player[0] = new Player("Player 1",1);
		player[1] = new Player("Player 2",2);
		
		new PawnFactory();
		
		switch(game) {
		case "Hnefatafl":
			boardSize = 11;
			pawn = PawnFactory.generatePawnsHnefatafl(player[0],player[1]);
			break;
		case "Tablut":
			boardSize = 9;
			pawn = PawnFactory.generatePawnsTablut(player[0],player[1]);
			break;
		case "Tawlbawrdd":
			boardSize = 11;
			
			pawn = PawnFactory.generatePawnsTawlbawrdd(player[0],player[1]);
			break;
		}
		
		if(gui) {
			new TableTopGUI(pawn,this,boardSize,game);
		}
		else {
			new TableTopCLI(pawn,this,boardSize);
		}
	}

	/**
	 * Find the Pawn object that sits on a specific tile
	 * @param x X coordinate of the tile
	 * @param y Y coordinate of the tile
	 * @return the Pawn object if found, otherwise return null
	 */
	public static Pawn findPawn(int x, int y, ArrayList<Pawn> pawn) {
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
	public void nextRound(Tile[][] board,int x, int y) {
		try { 
			// If there is a pawn in the tile pressed, save the selected tile for later use 
			if(board[x][y].isOccupied()) {
				selectedTile = board[x][y];
				selectedPawn = findPawn(x,y,pawn);
			}
			
			//Restricted tile 
			else if(Rules.pawnAccessedRestrictedTile(board[x][y],selectedPawn)) {
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
							board[x][y].setPawn(selectedPawn);
							board[x][y].setOccupied(true);
							
							Rules.checkCapture(board, pawn, selectedPawn);
							
							// Increment round when a player made a move
							round++;
							
							// Reset the selection
//							selectedPawn = null;
//							selectedTile = null;
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
	
	public void saveGame(String filename) {
		try {
			
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			out.writeObject(this);
			System.out.println("Game saved..");
			
			file.close();
			out.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public GameLogic loadGame(String filename) {

		GameLogic gl = null;

		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			
			gl = (GameLogic) in.readObject();
			
			file.close();
			in.close();
			
		} catch (FileNotFoundException e1) {
//			return null;
			e1.printStackTrace();
		} catch (IOException e1) {
//			return null;
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return gl;
	}
	
	
}
