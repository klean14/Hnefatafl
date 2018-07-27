package CommandLine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import Core.GameLogic;
import Core.Pawn;
import Core.Rules;

public class TableTopCMD implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TileCLI[][] board;
	
	private int boardSize;
	
	private GameLogic game;
	
	private Rules rules = new Rules();
	
	private static String filename = "save_game.ser";
	
	private ArrayList<Pawn> pawn;
	
	/**
	 * Default constructor
	 * @param pawns the arraylist of pawns
	 * @param game GameLogic instance
	 * @param size The size of the board
	 */
	public TableTopCMD(ArrayList<Pawn> pawns,GameLogic game, int size) {
		
		this.game = game;
		this.boardSize = size;
		this.board = new TileCLI[size][size];
		this.pawn = pawns;
		
		gameSequence();
	}

	/**
	 * Method that follows the logic sequence of the game
	 */
	public void gameSequence() {
		createBoard();
		
		while(true) {
			int round = game.getRound();
			try {
				System.out.println("Round: " + round);
				System.out.println("Player " + ((round % 2)+1) + " plays");
				
				printBoard(boardSize);
				
				
				if(rules.checkEnd(pawn, board)) {
					System.out.println("Congratulations Player " + ((--round % 2) + 1));
					new MainMenuCMD();
				}
				
				System.out.println("Input <yx> <yx> of the old and new position of the pawn respectively, separated by as space or type \"exit\" to exit. You can also save the game by typing \"save\": ");
				// Read user input
				Scanner reader = new Scanner(System.in);
				String userInput = reader.nextLine();
				
				if(userInput.equals("exit")) {
					new MainMenuCMD();
				}
				else if(userInput.equals("save")) {
					saveGame();
				}
				else {
					// Split into 2 Strings
					String[] token = userInput.split(" ");
					
					// Extract the position of the pawn to be moved
					int oldPosY =  (int)token[0].charAt(0) - 97;
					int oldPosX = Integer.parseInt(token[0].substring(1, token[0].length())) - 1;
					
					game.nextRound(board,oldPosX,oldPosY);
					
					//Extract the position of the new position
					int newPosY = (int)token[1].charAt(0) - 97;
					int newPosX = Integer.parseInt(token[1].substring(1, token[1].length())) - 1;
					
					game.nextRound(board,newPosX,newPosY);
				}
				
			} 
			catch (NullPointerException e) {
				System.out.println("No pawn found");
			}
			catch (ArrayIndexOutOfBoundsException e1) {
				System.out.println("Wrong input. Add the old AND new position of the pawn.");
			}
//			catch (NumberFormatException e1) {
//				System.out.println("Wrong input. Type first a letter and second a number.");
//			}
		}
	}

	/**
	 * Saves the game state
	 */
	private void saveGame() {
		FileOutputStream file = null;
		ObjectOutputStream out = null;
		try {
			file = new FileOutputStream(filename);
			out = new ObjectOutputStream(file);
			
			out.writeObject(this);
			System.out.println("Game saved..");
			
			file.close();
			out.close();
		} 
		catch (FileNotFoundException e) {

//			e.printStackTrace();
		} 
		catch (IOException e) {

//			e.printStackTrace();
		}
	}

	/**
	 * Instantiate the tiles and set the restricted ones
	 */
	private void createBoard() {
		
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row] = new TileCLI(col,row);
				
				//The tiles that are restricted to the pawns except for the king
				if(row == 0 && col == 0 || row == boardSize-1 && col == 0 || row == 0 && col == boardSize-1 || row == boardSize-1 && col == boardSize-1 || row == (boardSize-1)/2 && col == (boardSize-1)/2) {
					board[col][row].setRestricted(true);
				}
	
			}
		}
	}
	
	/**
	 * Method to print out the board in a command line
	 * @param size is the size of the board
	 */
	private void printBoard(int size) {
		
		String boardString = new String();
		
		
		boardString += "  ";
		for(int i = 1; i <= size; i++) 
		{
			boardString += "  ";
			boardString += i;
			boardString += " ";
		}
		
		boardString += System.lineSeparator();
		boardString += "  ";
		
		for(int i = 0; i <= size*4; i++) 
		{
			boardString += "-";
		}
		
		boardString += System.lineSeparator();
		
		for(int row = 0; row < size; row ++) {
			// Print out the characters of the alphabet
			boardString += Character.toString ((char) (row + 97));;
			for(int col = 0; col <= size; col++) {
				boardString += " | ";
				
				// Check if there is a pawn in this position of the board
				Pawn tilePawn = game.findPawn(col, row);
					if(tilePawn != null) {
						board[col][row].setPawn(tilePawn);
						board[col][row].setOccupied(true);
						if(tilePawn.isKing()) {
							boardString += "k";
						}
						else if(tilePawn.getPlayer().getID() == 1) {
							boardString += "o";
						}
						else {
							boardString += "a";
						}
					}
					else {
						try {
							board[col][row].setPawn(null);
							board[col][row].setOccupied(false);
						} catch (ArrayIndexOutOfBoundsException e) {
							//Do nothing
						}
					}
				
				
				try {
					if(!board[col][row].isOccupied()) {
						boardString += " ";
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//Do nothing
				}
				
			}
			boardString += System.lineSeparator();
		}
		boardString += "  ";
		for(int i = 0; i <= size*4; i++) 
		{
			boardString += "-";
		}
		
		System.out.println(boardString);
		
	}
	
	
}
