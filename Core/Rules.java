package Core;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

/**
 * This class has all the necessary checks for capturing a pawn (or the king) and if the game has ended
 *
 */
public class Rules implements java.io.Serializable {

	// Rules from: http://tafl.cyningstan.com/page/20/a-rule-book-for-hnefatafl

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Stack<Pawn> undoablePawnList = new Stack<Pawn>();
	private static ArrayList<Pawn> pawnsList;
	
	/**
	 * Check if the player has captured a pawn
	 * @param board 2D array of Tile type
	 * @param pawns ArrayList of all the Pawn objects
	 * @param pawn The Pawn object that initiated the check
	 */
	public static boolean checkCapture(Tile[][] board,ArrayList<Pawn> pawns, Pawn pawn) {
		boolean capture = false;
		pawnsList = pawns;
		int xPos = pawn.getPosX();
		int yPos = pawn.getPosY();
		Player player = pawn.getPlayer();
		Pawn enemyPawn = null, alliedPawn = null;
		
		// Check for the pattern XOX
		// where X is allied pawns and O is enemy pawns

		
		for(int i = 0; i < 4; i++) {
			// l and k give us the pattern 1 0, -1 0, 0 1, 0 -1
			// We use this pattern to check all 4 sides of that pawn
			int l = (int) ( i * Math.pow(-1,i))/2;
			int k = (int) (-l + Math.pow(-1,i));

			try {
				try {
					// Check the tile next to the pawn
					if(board[xPos + l][yPos + k].isOccupied()) {
						
						// Get pawn next to it, if there is any
						enemyPawn = GameLogic.findPawn(xPos + l, yPos + k,pawnsList);
						
						// Special case for the king
						if(enemyPawn instanceof KingPawn) {
							checkCaptureKing(enemyPawn,board,pawns);
						}
						else {
							// Get the pawn 2 tiles away, if there is any
							alliedPawn = GameLogic.findPawn(xPos + 2*l, yPos + 2*k,pawnsList);
							
							// If the pawn next to it belongs to the other player
							if(enemyPawn.getPlayer() != player) {
								// If the pawn 2 tiles away in the same direction is of the same player
								if(alliedPawn.getPlayer() == player) {
									// Remove that pawn
									removePawn(board[xPos + l][yPos + k],pawns,enemyPawn);
									capture = true;
								}
							}
						}
					}
				}
				
				
				catch(NullPointerException e) {
					// Check if the tile 2 tiles away from the pawn is a restricted tile
					if( board[xPos + 2*l][yPos + 2*k].isRestricted()) {
						removePawn(board[xPos + l][yPos + k],pawns,enemyPawn);
						capture = true;
					}
				}


			}
			catch (ArrayIndexOutOfBoundsException e) {
				// Do nothing
			}
			
		}
		return capture;
	}

	/**
	 * Special check for the king Pawn
	 * @param king Pawn object which is the king
	 * @param board 2D array of Tile type
	 * @param pawns ArrayList of all the Pawn objects
	 */
	private static void checkCaptureKing(Pawn king, Tile[][] board, ArrayList<Pawn> pawns) {
		int xPos = king.getPosX();
		int yPos = king.getPosY();
		Player player = king.getPlayer();
		
		int count = 0;
		
		
		// Check all 4 sides adjacent of the king
		for(int i = 0; i < 4; i++) {
			Pawn enemyPawn = null;
			int l = (int) ( i * Math.pow(-1,i))/2;
			int k = (int) (-l + Math.pow(-1,i));

			try {
				try {
					if(board[xPos + l][yPos + k].isOccupied() || board[xPos + l][yPos + k].isRestricted()) {
						enemyPawn = board[xPos + l][yPos + k].getPawn();
						if(enemyPawn.getPlayer() != player) {
							count++;
						}
					}
				}	
				
				catch(NullPointerException e) {
					if( board[xPos + l][yPos + k].isRestricted()) {
						count++;
					}
				}


			}
			catch (ArrayIndexOutOfBoundsException e) {
				// Do nothing
			}
		}
		
		// If surrounded by all 4 sides
		if(count == 4) {
			removePawn(board[xPos][yPos],pawns,king);
		}
		
	}

	public static boolean checkEnd(ArrayList<Pawn> pawns, Tile[][] board) {
		int nRows = board.length - 1;
		int nCols = board[0].length - 1;
		
		if(board[0][0].isOccupied() || board[0][nRows].isOccupied() || board[nCols][0].isOccupied() || board[nCols][nRows].isOccupied()) {
			System.out.println("The King has escaped!");
			return true;
		}
		
		// Check if the king pawn is still in the game
		for(Pawn pawn : pawns) {
			if(pawn instanceof KingPawn) {
				return false;
			}
		}
		
		System.out.println("The king is dead");
		return true;
	}

	private static void removePawn(Tile tile,ArrayList<Pawn> pawns, Pawn enemyPawn) {
		undoablePawnList.push((tile.getPawn()));
		Pawn.getListener().undoableEditHappened(new UndoableEditEvent(tile.getPawn(),new UndoableRemoveEdit(tile)));
		tile.setPawn(null);
		tile.setOccupied(false);
		pawns.remove(enemyPawn);
	}

	/**
	 * If the destination is within the cardinal directions of the origin  ( ^ is xor operation)
	 * @param destinationX The destination X position
	 * @param destinationY The destination Y position
	 * @param originX The origin X position
	 * @param originY The origin Y position
	 * @return True if the move is legal
	 */
	public static boolean legalMove(int destinationX, int destinationY, int originX, int originY) {
		return (destinationX == originX ^ destinationY == originY);
	}

	/**
	 * Check if the pawn selected is of the current player's turn
	 * @param round Number of rounds passed
	 * @param playerID The pawn's player id that belongs to
	 * @return true if the selected pawn belongs to the player
	 */
	public static boolean playerTurn(int round, int playerID) {
		return (round % 2 ) + 1 == playerID;
	}

	/**
	 * Check if there are any pawns between the 2 tiles
	 * @param board TODO
	 * @param thisX X coordinate of old tile
	 * @param thatX X coordinate of new tile
	 * @param thisY Y coordinate of old tile
	 * @param thatY Y coordinate of new tile
	 * @return true if there are pawns between, otherwise false
	 */
	public static boolean pawnsBetween(Tile[][] board, int thisX, int thatX, int thisY, int thatY) {
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
	
	private static class UndoableRemoveEdit extends AbstractUndoableEdit {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Tile pawnTile;

		public UndoableRemoveEdit(Tile pawnTile) {
			this.pawnTile = pawnTile;
		}

		// Return a reasonable name for this edit.
		public String getPresentationName() {
			return "PosX:" + pawnTile.getPosX() + "PosY:" + pawnTile.getPosY();
		}

		// Undo by regenerating the pawn and setting it to the tile
		public void undo() throws CannotUndoException {
			// First regenerate the pawn

			Pawn undoablePawn = undoablePawnList.pop();
			pawnsList.add(undoablePawn);
			pawnTile.setPawn(undoablePawn);
			pawnTile.setOccupied(true);
			
			// Call undo again to undo the last move
			PawnFactory.getUndoManager().undo();
		}
	}
	
	public static boolean pawnAccessedRestrictedTile(Tile board, Pawn selectedPawn) {
		return board.isRestricted() && !(selectedPawn instanceof KingPawn);
	}
	
	public static String showRules() {
		return   "- All pawns can move in a straight line horizontally or vertically (no diagonal)."  + System.lineSeparator()
				 + "- If nothing is in the way, a pawn can move indefinitely, within the board." + System.lineSeparator()
				 + "- Pawns cannot move to a space where it would require to go over another pawn." + System.lineSeparator()
				 + "- Attackers and defenders play alternatively, with one move at a time." + System.lineSeparator()
				 + "- A pawn is captured if it is surrounded by both sides in the same cardinal direction, by two pawns of the opposing team." + System.lineSeparator()
				 + "- A pawn is NOT captured if it moves to a space in between two pawns of the opposing team." + System.lineSeparator()
				 + "- The king pawn is captured ONLY if it is surrounded by all four sides." + System.lineSeparator()
				 + "- The square in the center of the board, where the king is placed by default, is restricted and cannot be accessed by any pawn except for the king itself. "  + System.lineSeparator()
				 + "- Any restricted tiles can be included in the capturing of a pawn." + System.lineSeparator()
				 + "- If the king is captured, then the attackers win." + System.lineSeparator()
				 + "- The defenders win only if the king manages to escape.";
	}
}

