package Core;
import java.util.Stack;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;


public class Pawn implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int posX;
	private int posY;
	private Player player;

	private Stack<Position> undoableMoveList;
	
	private static UndoableEditListener listener;
		
	public Pawn(int posX, int posY, Player player) {
		this.posX = posX;
		this.posY = posY;
		this.player = player;
		this.undoableMoveList = new Stack<Position>();
	}
	
	/****************Getters and setters******************/
	
	public int getPosX() {return posX;}
	public void setPosX(int posX) {this.posX = posX;}

	public int getPosY() {return posY;}
	public void setPosY(int posY) {this.posY = posY;}

	
	public Player getPlayer() {return player;}

	public static UndoableEditListener getListener() {return listener;}

	
	/*****************************************************/

	
	/**
	 * Move the pawn to the designated tile
	 * @param newPosX X coordinate of the new tile
	 * @param newPosY Y coordinate of the new tile
	 */
	public void move(int newPosX, int newPosY) {
		undoableMoveList.push(new Position(this.posX,this.posY));
		listener.undoableEditHappened(new UndoableEditEvent(this,new UndoableMoveEdit(this)));
		
		this.posX = newPosX;
		this.posY = newPosY;
		
	}
	
	// Set the UndoableEditListener.
	public void addUndoableEditListener(UndoableEditListener l) {
		listener = l; // Should ideally throw an exception if listener != null
	}


	public String toString() {
		return "PosX:" + posX + "PosY" + posY;
	}
	
	class UndoableMoveEdit extends AbstractUndoableEdit {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Pawn pawn;

		public UndoableMoveEdit(Pawn pawn) {
			this.pawn = pawn;
		}

		// Return a reasonable name for this edit.
		public String getPresentationName() {
			return "PosX:" + pawn.getPosX() + "PosY:" + pawn.getPosY();
		}


		// Undo by moving the pawn back to its previous position
		public void undo() throws CannotUndoException {
			Position unboableMove = undoableMoveList.pop();
			
			Pawn.this.setPosX(unboableMove.getX());
			Pawn.this.setPosY(unboableMove.getY());

		}
	}
}
