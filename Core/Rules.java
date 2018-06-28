package Core;

import java.util.ArrayList;

public class Rules {
	// Rules from: http://tafl.cyningstan.com/page/20/a-rule-book-for-hnefatafl
		public void checkCapture(Tile[][] board,ArrayList<Pawn> pawns, Pawn pawn, Game game) {
			int xPos = pawn.getPosX();
			int yPos = pawn.getPosY();
			Player player = pawn.getPlayer();
			Pawn enemyPawn = null, alliedPawn = null;
			
			// Check for the pattern XOX
			// where X is allied pawns and O is enemy pawns
			
			// Downwards
			try {
				if(board[xPos][yPos + 1].isOccupied()) {
					enemyPawn = game.findPawn(xPos, yPos + 1);
					alliedPawn = game.findPawn(xPos, yPos + 2);
					if(enemyPawn.getPlayer() != player) {
						if(alliedPawn.getPlayer() == player) {
							//TODO delete the pawn
//							System.out.println("NOICE!");
							removePawn(board[xPos][yPos + 1],pawns,enemyPawn);
						}
					}
				}
			} 
			// In case the pawn reached the edge of the board
			catch(ArrayIndexOutOfBoundsException e) { 
				// Do nothing
			}
			catch(NullPointerException e) {
				if( board[xPos][yPos + 2].isRestricted()) {
					removePawn(board[xPos][yPos + 1],pawns,enemyPawn);
//					System.out.println("pale NOICE!");
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
//							System.out.println("NOICE!");
						}
					}
				}
			} 
			// In case the pawn reached the edge of the board
			catch(ArrayIndexOutOfBoundsException e) { 
				// Do nothing
			}
			catch(NullPointerException e) {
				if( board[xPos][yPos - 2].isRestricted()) {
					removePawn(board[xPos][yPos - 1],pawns,enemyPawn);
//					System.out.println("pale NOICE!");
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
//							System.out.println("NOICE!");
						}
					}
				}
			} 
			// In case the pawn reached the edge of the board
			catch(ArrayIndexOutOfBoundsException e) { 
				// Do nothing
			}
			catch(NullPointerException e) {
				if( board[xPos + 2][yPos].isRestricted()) {
					removePawn(board[xPos + 1][yPos],pawns,enemyPawn);
//					System.out.println("pale NOICE!");
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
//							System.out.println("NOICE!");
						}
					}
				}
			} 
			// In case the pawn reached the edge of the board
			catch(ArrayIndexOutOfBoundsException e) { 
				// Do nothing
			}
			catch(NullPointerException e) {
				if( board[xPos - 2][yPos].isRestricted()) {
					removePawn(board[xPos - 1][yPos],pawns,enemyPawn);
//					System.out.println("pale NOICE!");
				}
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

