import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import CommandLine.TileCLI;
import Core.Pawn;
import Core.Player;
import Core.Rules;

class Test {
	private Rules rules = new Rules();
	
	private Player p1 = new Player("Player 1",1);
	private Player p2 = new Player("Player 2",2);
	
	@org.junit.jupiter.api.Test
	void testLegalMove() {
		setupBoard();


		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		
		assertFalse(rules.legalMove(2 , 3 , pawn.get(0).getPosX(), pawn.get(0).getPosY()),"Not a legal move");
		
	}

	@org.junit.jupiter.api.Test
	void testPawnsBetween() {
		TileCLI[][] board = setupBoard();

		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		
		assertFalse(rules.pawnsBetween(board, pawn.get(0).getPosX(),pawn.get(0).getPosY(),1,3),"Pawns detected between");
		
	}
	
	
	private TileCLI[][] setupBoard() {
		int boardSize = 5;
		TileCLI[][] board = new TileCLI[boardSize][boardSize];
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				board[i][j] = new TileCLI(i,j);
				//The tiles that are restricted to the pawns except for the king
				if(i == 0 && j == 0 || i == boardSize-1 && j == 0 || i == 0 && j == boardSize-1 || i == boardSize-1 && j == boardSize-1 || i == (boardSize-1)/2 && j == (boardSize-1)/2) {
					board[i][j].setRestricted(true);
				}
			}
		}
		
		return board;
	}

}
