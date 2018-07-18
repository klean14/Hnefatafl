package CommandLine;

import java.util.ArrayList;
import java.util.Scanner;

import Core.GameLogic;
import Core.Pawn;

public class TableTopCMD {
	public TableTopCMD(ArrayList<Pawn> pawns,GameLogic game, int size) {
		printBoard(size,pawns);
	}

	private void printBoard(int size, ArrayList<Pawn> pawns) {
		
		while(true) {
			String board = new String();
			boolean kakka;
			board += "  ";
			for(int i = 1; i <= size; i++) 
			{
				board += "  ";
				board += i;
				board += " ";
			}
			
			board += System.lineSeparator();
			board += "  ";
			
			for(int i = 0; i <= size*4; i++) 
			{
				board += "-";
			}
			
			board += System.lineSeparator();
			
			for(int row = 0; row < size; row ++) {
				board += Character.toString ((char) (row + 97));;
				for(int col = 0; col <= size; col++) {
					kakka = false;
					board += " | ";
					for(Pawn pawn : pawns) {
						if(pawn.getPosX() == row && pawn.getPosY() == col) {
							if(pawn.isKing()) {
								board += "k";
							}
							else if(pawn.getPlayer().getID() == 1) {
								board += "o";
							}
							else {
								board += "a";
							}
							
							kakka = true;
						}
						
					}
					
					if(!kakka) {
						board += " ";
					}
					
				}
				board += System.lineSeparator();
			}
			board += "  ";
			for(int i = 0; i <= size*4; i++) 
			{
				board += "-";
			}
			
			System.out.println(board);
			
			Scanner reader = new Scanner(System.in);
			String userInput = reader.nextLine();
		}
	}
}
