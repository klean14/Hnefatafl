package CommandLine;

import Core.Pawn;
import Core.Tile;

public class TileCLI implements Tile{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int posX;
	private int posY;
	private boolean occupied = false;
	private boolean restricted = false;
	private Pawn pawn;
	
	public TileCLI(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.pawn = null;
	}
	
	/****************Getters and setters******************/
	
	public int getPosX() {return posX;}
	public void setPosX(int posX) {this.posX = posX;}

	public int getPosY() {return posY;}
	public void setPosY(int posY) {this.posY = posY;}

	public boolean isOccupied() {return occupied;}
	public void setOccupied(boolean occupied) {this.occupied = occupied;}

	public Pawn getPawn() {return pawn;}
	public void setPawn(Pawn pawn) {this.pawn = pawn;}
	
	public boolean isRestricted() {return restricted;}
	public void setRestricted(boolean restricted) {this.restricted = restricted;}

	/*****************************************************/

	public String toString() {
		return ("Pos x: " + this.posX + " Pos y: " + this.posY);
	}
}
