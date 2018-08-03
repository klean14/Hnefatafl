package Core;

import java.util.ArrayList;
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

	/**
	 * Check if the player has captured a pawn
	 * @param board 2D array of Tile type
	 * @param pawns ArrayList of all the Pawn objects
	 * @param pawn The Pawn object that initiated the check
	 * @param game The Game object
	 */
	public void checkCapture(TileInterface[][] board,ArrayList<Pawn> pawns, Pawn pawn, GameLogic game) {
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
						enemyPawn = game.findPawn(xPos + l, yPos + k);
						
						// Special case for the king
						if(enemyPawn instanceof KingPawn) {
							checkCaptureKing(enemyPawn,board,pawns);
							break;
						}
						
						// Get the pawn 2 tiles away, if there is any
						alliedPawn = game.findPawn(xPos + 2*l, yPos + 2*k);
						
						// If the pawn next to it belongs to the other player
						if(enemyPawn.getPlayer() != player) {
							// If the pawn 2 tiles away in the same direction is of the same player
							if(alliedPawn.getPlayer() == player) {
								// Remove that pawn
								removePawn(board[xPos + l][yPos + k],pawns,enemyPawn);
							}
						}
					}
				}
				
				
				catch(NullPointerException e) {
					// Check if the tile 2 tiles away from the pawn is a restricted tile
					if( board[xPos + 2*l][yPos + 2*k].isRestricted()) {
						removePawn(board[xPos + l][yPos + k],pawns,enemyPawn);
					}
				}


			}
			catch (ArrayIndexOutOfBoundsException e) {
				// Do nothing
			}
		}
	}

	/**
	 * Special check for the king Pawn
	 * @param king Pawn object which is the king
	 * @param board 2D array of Tile type
	 * @param pawns ArrayList of all the Pawn objects
	 */
	private void checkCaptureKing(Pawn king, TileInterface[][] board, ArrayList<Pawn> pawns) {
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
		if(count == 3) {
//			JOptionPane.showMessageDialog(null, "THE KING IS DEAD");
//			System.out.println("THE KING IS DEAD");
			removePawn(board[xPos][yPos],pawns,king);
		}
		
	}

	public boolean checkEnd(ArrayList<Pawn> pawns, TileInterface[][] board) {
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

	private void removePawn(TileInterface tile,ArrayList<Pawn> pawns, Pawn enemyPawn) {
		tile.setPawn(null);
		tile.setOccupied(false);
		pawns.remove(enemyPawn);
	}

	public boolean legalMove(GameLogic gameLogic, int x, int y) {
		return gameLogic.selectedTile.getPosX() == x ^ gameLogic.selectedTile.getPosY() == y;
	}

	public boolean playerTurn(GameLogic gameLogic) {
		return (gameLogic.round % 2 )+ 1 == gameLogic.selectedPawn.getPlayer().getID();
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
	public boolean pawnsBetween(TileInterface[][] board, int thisX, int thatX, int thisY, int thatY) {
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

}

