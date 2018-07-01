package Core;

import java.util.ArrayList;

public class Rules {

	// Rules from: http://tafl.cyningstan.com/page/20/a-rule-book-for-hnefatafl

	/**
	 * Check if the player has captured a pawn
	 * @param board 2D array of Tile type
	 * @param pawns ArrayList of all the Pawn objects
	 * @param pawn The Pawn object that initiated the check
	 * @param game The Game object
	 */
	public void checkCapture(Tile[][] board,ArrayList<Pawn> pawns, Pawn pawn, Game game) {
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

//			System.out.println(l + ", " + k);
			try {
				try {
					// Check the tile next to the pawn
					if(board[xPos + l][yPos + k].isOccupied()) {
						
						// Get pawn next to it, if there is any
						enemyPawn = game.findPawn(xPos + l, yPos + k);
						
						// Special case for the king
						if(enemyPawn.isKing()) {
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
								break;
							}
						}
					}
				}
				
				
				catch(NullPointerException e) {
					// Check if the tile 2 tiles away from the pawn is a restricted tile
					if( board[xPos + 2*l][yPos + 2*k].isRestricted()) {
						removePawn(board[xPos + l][yPos + k],pawns,enemyPawn);
						break;
					}
				}


			}
			catch (ArrayIndexOutOfBoundsException e) {
				// Do nothing
			}
		}
		/*
		try {
			// Downwards
			try {
				if(board[xPos][yPos + 1].isOccupied()) {
					enemyPawn = game.findPawn(xPos, yPos + 1);
					alliedPawn = game.findPawn(xPos, yPos + 2);
					if(enemyPawn.getPlayer() != player) {
						if(alliedPawn.getPlayer() == player) {
							//TODO delete the pawn
							removePawn(board[xPos][yPos + 1],pawns,enemyPawn);
						}
					}
				}
			} 
			catch(NullPointerException e) {

				if( board[xPos][yPos + 2].isRestricted()) {
					removePawn(board[xPos][yPos + 1],pawns,enemyPawn);
				}
			}

			// Upwards
			try {
				if(board[xPos][yPos - 1].isOccupied()) {
					enemyPawn = game.findPawn(xPos, yPos - 1);
					alliedPawn = game.findPawn(xPos, yPos - 2);
					if(enemyPawn.getPlayer() != player) {

						if(alliedPawn.getPlayer() == player) {
							//TODO delete the pawn
							removePawn(board[xPos][yPos - 1],pawns,enemyPawn);
						}
					}
				}
			} 
			catch(NullPointerException e) {
				if( board[xPos][yPos - 2].isRestricted()) {
					removePawn(board[xPos][yPos - 1],pawns,enemyPawn);
				}
			}

			// To the right
			try {
				if(board[xPos + 1][yPos].isOccupied()) {
					enemyPawn = game.findPawn(xPos + 1, yPos);
					alliedPawn = game.findPawn(xPos + 2, yPos);
					if(enemyPawn.getPlayer() != player) {
						if(alliedPawn.getPlayer() == player) {
							//TODO delete the pawn
							removePawn(board[xPos + 1][yPos],pawns,enemyPawn);
						}
					}
				}
			} 
			catch(NullPointerException e) {
				if( board[xPos + 2][yPos].isRestricted()) {
					removePawn(board[xPos + 1][yPos],pawns,enemyPawn);
				}
			}

			// To the left
			try {
				if(board[xPos - 1][yPos].isOccupied()){
					enemyPawn = game.findPawn(xPos - 1, yPos);
					alliedPawn = game.findPawn(xPos - 2, yPos);
					if(enemyPawn.getPlayer() != player) {
						if(alliedPawn.getPlayer() == player) {
							//TODO delete the pawn
							removePawn(board[xPos - 1][yPos],pawns,enemyPawn);
						}
					}
				}
			} 
			catch(NullPointerException e) {
				if( board[xPos - 2][yPos].isRestricted()) {
					removePawn(board[xPos - 1][yPos],pawns,enemyPawn);
				}
			}
		} 

		// In case the pawn reached the edge of the board
		catch (ArrayIndexOutOfBoundsException e) {
			// Do nothing
		}*/
	}

	/**
	 * Special check for the king Pawn
	 * @param king Pawn object which is the king
	 * @param board 2D array of Tile type
	 * @param pawns ArrayList of all the Pawn objects
	 */
	private void checkCaptureKing(Pawn king, Tile[][] board, ArrayList<Pawn> pawns) {
		int xPos = king.getPosX();
		int yPos = king.getPosY();
		Player player = king.getPlayer();
		Pawn enemyPawn = null;
		int count = 0;
		
		
		// Check all 4 sides adjacent of the king
		for(int i = 0; i < 4; i++) {
			int l = (int) ( i * Math.pow(-1,i))/2;
			int k = (int) (-l + Math.pow(-1,i));

			try {
				try {
					if(board[xPos + l][yPos + k].isOccupied()) {
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
			System.out.println("THE KING IS DEAD");
			removePawn(board[xPos][yPos],pawns,king);
		}
		
	}

	public boolean checkEnd() {
		//TODO
		return false;
	}

	private void removePawn(Tile tile,ArrayList<Pawn> pawns, Pawn enemyPawn) {
		tile.setPawn(null);
		tile.setText("");
		tile.setOccupied(false);
		tile.setEnabled(true);
		pawns.remove(enemyPawn);
	}

}

