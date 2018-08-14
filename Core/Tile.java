package Core;

public interface Tile extends java.io.Serializable{
	
	
	/****************Getters and setters******************/
	
	public int getPosX();
	public void setPosX(int posX);

	public int getPosY();
	public void setPosY(int posY);

	public boolean isOccupied();
	public void setOccupied(boolean occupied);

	public Pawn getPawn();
	public void setPawn(Pawn pawn);
	
	public boolean isRestricted();
	public void setRestricted(boolean restricted);

	/*****************************************************/

	public String toString();
}
