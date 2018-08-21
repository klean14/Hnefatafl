package testing;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import org.junit.Before;
import CommandLine.TileCLI;
import Core.GameLogic;
import Core.KingPawn;
import Core.Pawn;
import Core.Player;
import Core.Rules;
import Core.PawnFactory.MyUndoableEditListener;

public class Testing {
	
	private Player p1 = new Player("Player 1",1);
	private Player p2 = new Player("Player 2",2);
	private TileCLI[][] board;
	private static UndoManager undoManager;

	/*
	 * Test 1
	 * Check if move was in same cardinal direction
	 */
	@Test
	public void testLegalMove() 
	{
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
	public void testPawnsBetween() 
	{
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
	public void testPawnOnPawn() 
	{
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
	public void testRestricted()
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new Pawn(1,2,p2));
		
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		
		if(!Rules.pawnAccessedRestrictedTile(board[0][0], pawn.get(0)))
			fail("Pawn tried to access resticted tile");
	}
	
	
	/*
	 * Test 5
	 * Check that the pawn didn't move at a tile outside the board borders
	 */
	@Test
	public void testPlayerTurn() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		
		if(!Rules.playerTurn(0, pawn.get(0).getPlayer().getID()))
			fail("Wrong player's turn");
	}
	
	/*
	 * Test 6
	 * Check that the pawn didn't move at a tile outside the board borders
	 */
	@Test
	public void testPawnCapture() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.add(new Pawn(2,3,p1));
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		
		pawn.get(2).move(1,3);
		if(GameLogic.findPawn(1, 2,pawn) == null)
			fail("Pawn not captured");
	}
	
	/*
	 * Test 7
	 * Check that the king was captured
	 */
	@Test
	public void testKingCaptured() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(1,1,p1));
		pawn.add(new KingPawn(1,2,p2));
		pawn.add(new Pawn(1,3,p1));
		pawn.add(new Pawn(0,2,p1));
		pawn.add(new Pawn(2,3,p1));
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		
		pawn.get(4).move(2,2);
		if(GameLogic.findPawn(1, 2,pawn) == null)
			fail("Pawn not captured");
	}
	
	/*
	 * Test 8
	 * Check that the pawn was captured
	 */
	@Test
	public void testRestrictedHelpCapture() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.add(new Pawn(0,3,p1));
		pawn.get(0).addUndoableEditListener(new MyUndoableEditListener());
		
		pawn.get(2).move(0,2);
		if(GameLogic.findPawn(1, 2,pawn) == null)
			fail("Pawn not captured");
	}
	

	/*
	 * Test 9
	 * Check that the pawn went back to its previous position
	 */
	@Test
	public void testUndoMove() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.add(new Pawn(0,3,p1));
		
		MyTestUndoableEditListener el = new MyTestUndoableEditListener();
		
		for(Pawn pawns: pawn)
			pawns.addUndoableEditListener(el);
		
		pawn.get(2).move(0,2);
		
		undoManager.undo();
		
		if(GameLogic.findPawn(0, 3,pawn) == null)
			fail("Undo did not work");
	}
	
	/*
	 * Test 10
	 * Check that the pawn that was captured got regenerated after undo
	 */
	@Test
	public void testUndoCapture() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.add(new Pawn(0,3,p1));
		
		MyTestUndoableEditListener el = new MyTestUndoableEditListener();
		
		for(Pawn pawns: pawn)
			pawns.addUndoableEditListener(el);
		
		pawn.get(2).move(0,2);
		
		undoManager.undo();
		
		if(GameLogic.findPawn(1, 2,pawn) == null)
			fail("Undo did not work");
	}
	
	/*
	 * Test 11
	 * Check that player 1 won the game since no King exists
	 */
	@Test
	public void testPlayer1Won() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new Pawn(1,2,p2));
		pawn.add(new Pawn(0,3,p1));

		if(!Rules.checkEnd(pawn, board))
			fail("checking for King did not work");
	}
	
	/*
	 * Test 12
	 * Check that player 1 won the game since no King exists
	 */
	@Test
	public void testPlayer2Won() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new KingPawn(0,0,p2));
		pawn.add(new Pawn(0,3,p1));
		
		if(Rules.checkEnd(pawn, board))
			fail("checking for King did not work");
	}
	
	/*
	 * Test 13
	 * Check that player 1 won the game since no King exists
	 */
	@Test
	public void testSave() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new KingPawn(0,0,p2));
		pawn.add(new Pawn(0,3,p1));
		
		if(Rules.checkEnd(pawn, board))
			fail("checking for King did not work");
	}
	
	/*
	 * Test 14
	 * Check that player 1 won the game since no King exists
	 */
	@Test
	public void testLoad() 
	{
		ArrayList<Pawn> pawn = new ArrayList<Pawn>();
		
		pawn.add(new Pawn(0,1,p1));
		pawn.add(new KingPawn(0,0,p2));
		pawn.add(new Pawn(0,3,p1));
		
		if(Rules.checkEnd(pawn, board))
			fail("checking for King did not work");
	}
	
	@Before
	public void setupBoard() 
	{
		undoManager = new UndoManager();
		int boardSize = 5;
		board = new TileCLI[boardSize][boardSize];
		
		for(int i = 0; i < boardSize; i++) 
		{
			for(int j = 0; j < boardSize; j++) 
			{
				board[i][j] = new TileCLI(i,j);
				if(i == 0 && j == 0 || i == boardSize-1 && j == 0 || i == 0 && j == boardSize-1 || i == boardSize-1 && j == boardSize-1 || i == (boardSize-1)/2 && j == (boardSize-1)/2) 
				{
					board[i][j].setRestricted(true);
				}
			}
		}
	}
	
	public static class MyTestUndoableEditListener implements UndoableEditListener {
		// When an UndoableEditEvent is generated (each time one of the pawns
		// is moved), we add it to the UndoManager
		public void undoableEditHappened(UndoableEditEvent ev) {
			undoManager.addEdit(ev.getEdit());
		}
	}
	
}
