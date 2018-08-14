import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import CommandLine.TileCLI;
import Core.Pawn;
import Core.Player;
import Core.Rules;
import Core.PawnGenerator.MyUndoableEditListener;

public class Testing {
	
	private Player p1 = new Player("Player 1",1);
	private Player p2 = new Player("Player 2",2);
	private TileCLI[][] board;
	

	/*
	 * Test 1
	 * Check if move was in same cardinal direction
	 */
	@Test
	public void testLegalMove() {
		System.out.println("test1");
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		
		assertFalse("Not a legal move",Rules.legalMove(2 , 3 , pawn.get(0).getPosX(), pawn.get(0).getPosY()));
		
	}

	/*
	 * Test 2
	 * Check for pawns between 
	 */
	@Test
	public void testPawnsBetween() {
		System.out.println("test2");
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		
		assertFalse("Pawns detected between",Rules.pawnsBetween(board, pawn.get(0).getPosX(),pawn.get(0).getPosY(),1,3));
	}
	
	/*
	 * Test 3
	 * Check that the pawn didn't move at a tile where a pawn already exists
	 */
	@Test
	public void testPawnOnPawn() {
		System.out.println("test3");
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		pawn.get(0).move(1, 2);
		
		assertTrue(pawn.get(0).getPosY() == 2);
	}
	
	/*
	 * Test 4
	 * Check if normal pawn tries to access restricted tile
	 */
	@Test
	public void testRestricted() {
		System.out.println("test4");
		
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new Pawn(1,2,p2));
		
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		
		if(!Rules.PawnAccessedRestrictedTile(board[0][0], pawn.get(0)))
			fail("Pawn tried to access resticted tile");
		else
			pawn.get(0).move(0, 0);
		
	}
	
	
	
	@Before
	public void setupBoard() {
		System.out.println("before");
		int boardSize = 5;
		board = new TileCLI[boardSize][boardSize];
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				board[i][j] = new TileCLI(i,j);
				//The tiles that are restricted to the pawns except for the king
				if(i == 0 && j == 0 || i == boardSize-1 && j == 0 || i == 0 && j == boardSize-1 || i == boardSize-1 && j == boardSize-1 || i == (boardSize-1)/2 && j == (boardSize-1)/2) {
					board[i][j].setRestricted(true);
				}
			}
		}
	}
	
}
