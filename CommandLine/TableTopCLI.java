package CommandLine;

import java.util.ArrayList;
import java.util.Scanner;

import Core.GameLogic;
import Core.KingPawn;
import Core.Pawn;
import Core.PawnFactory;
import Core.Player;
import Core.Rules;
import Core.TableTop;


public class TableTopCLI implements TableTop {

	private TileCLI[][] board;
	
	private int boardSize;
	
	private GameLogic game;
	
	private ArrayList<Pawn> pawn;
	
	private Player[] player;
	
	transient private Scanner userInput;
	/**
	 * Default constructor
	 * @param pawns the arraylist of pawns
	 * @param game GameLogic instance
	 * @param size The size of the board
	 */
	public TableTopCLI(ArrayList<Pawn> pawns,GameLogic game, int size) {
		
		this.game = game;
		this.boardSize = size;
		this.board = new TileCLI[size][size];
		this.pawn = pawns;
		this.player = game.getPlayer();
		
		userInput = new Scanner(System.in);
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
				System.out.println();
				System.out.println("/****************/");
				System.out.println(player[0].getName() + "'s pawn: o");
				System.out.println(player[1].getName() + "'s pawn: a");
				System.out.println("/****************/");
				System.out.println();
				System.out.println("Round: " + (round + 1));
				System.out.println(player[(round % 2)].getName() + " plays");
				
				printBoard();
				
				
				if(Rules.checkEnd(pawn, board)) {
					System.out.println("Congratulations Player " + ((--round % 2) + 1));
					new MainMenuCLI();
				}
				
				System.out.println("Input <yx> <yx> of the old and new position of the pawn respectively, separated by a space or type \"help\" for extra commands: ");
				// Read user input
				String input = userInput.nextLine();
				
				switch(input) {
				case "exit":
					new MainMenuCLI();
					break;
					
				case "save":
					saveGame();
					break;
					
				case "load":
					loadGame();
					break;
					
				case "undo":
					if(PawnFactory.getUndoManager().canUndo()) {
						PawnFactory.getUndoManager().undo();
						game.decrementRound();
					}
					break;
					
				case "edit":
					editPlayer();
					break;
				case "rules":
					
					System.out.println(Rules.showRules());
					break;
				case "help":
					System.out.println("=========" + System.lineSeparator()
									 + "Commands " + System.lineSeparator()
									 + "=========" + System.lineSeparator()
									 + " \"rules\" to show the rules" + System.lineSeparator()
									 + " \"exit\" to exit" + System.lineSeparator()
									 + " \"save\" to save the game "  + System.lineSeparator()
									 + " \"load\" to load game "  + System.lineSeparator()
									 + " \"undo\" to undo the previous move" + System.lineSeparator()
									 + " \"edit\" to edit the names of players" + System.lineSeparator());
					break;
					
				default:
					// Split into 2 Strings
					String[] token = input.split(" ");
					
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
//				e.printStackTrace();
			}
			catch (ArrayIndexOutOfBoundsException e1) {
				System.out.println("Wrong input. Add the old AND new position of the pawn.");
			}
			catch (NumberFormatException e1) {
				System.out.println("Wrong input. Type first a letter and second a number.");
			}
		}
	}

	private void editPlayer() {
		System.out.print("Type the name of player 1: ");
		player[0].setName(userInput.nextLine());
		
		System.out.print("Type the name of player 2: ");
		player[1].setName(userInput.nextLine());
		
	}

	/**
	 * Saves the game state
	 */
	public void saveGame() {
		
		System.out.println("Specify the name of the file:");
		
		String filename = userInput.nextLine() + ".ser";
		game.saveGame(filename);
	}

	
	public void loadGame() {
	
		// Skip the newline
		System.out.println("Specify the name of the file: ");
		
		String filename = userInput.nextLine() +".ser";
		
		GameLogic gl = game.loadGame(filename);
		if(gl == null) {
			System.out.println("There was an error with the file");
		}
		else {
			new TableTopCLI(gl.getPawn(),gl,gl.getBoardSize());
		}
	}
	/**
	 * Instantiate the tiles and set the restricted ones
	 */
	public void createBoard() {
		
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
	 */
	public void printBoard() {
		
		String boardString = new String();
		
		
		boardString += "  ";
		for(int i = 1; i <= boardSize; i++) 
		{
			boardString += "  ";
			boardString += i;
			boardString += " ";
		}
		
		boardString += System.lineSeparator();
		boardString += "  ";
		
		for(int i = 0; i <= boardSize*4; i++) 
		{
			boardString += "-";
		}
		
		boardString += System.lineSeparator();
		
		for(int row = 0; row < boardSize; row ++) {
			// Print out the characters of the alphabet
			boardString += Character.toString ((char) (row + 97));;
			for(int col = 0; col <= boardSize; col++) {
				boardString += " | ";
				
				// Check if there is a pawn in this position of the board
				Pawn tilePawn = GameLogic.findPawn(col, row,pawn);
					if(tilePawn != null) {
						board[col][row].setPawn(tilePawn);
						board[col][row].setOccupied(true);
						if(tilePawn instanceof KingPawn) {
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
		for(int i = 0; i <= boardSize*4; i++) 
		{
			boardString += "-";
		}
		
		System.out.println(boardString);
		
	}
	
	
}
