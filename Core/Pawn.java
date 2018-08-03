package Core;

public class Pawn implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int posX;
	private int posY;
	private Player player;
	
		
	public Pawn(int posX, int posY, Player player) {
		this.posX = posX;
		this.posY = posY;
		this.player = player;
	}
	
	/****************Getters and setters******************/
	
	public int getPosX() {return posX;}
	public void setPosX(int posX) {this.posX = posX;}

	public int getPosY() {return posY;}
	public void setPosY(int posY) {this.posY = posY;}
	
	public Player getPlayer() {return player;}

	
	/*****************************************************/

	/**
	 * Move the pawn to the designated tile
	 * @param newPosX X coordinate of the new tile
	 * @param newPosY Y coordinate of the new tile
	 */
	public void move(int newPosX, int newPosY) {
		this.posX = newPosX;
		this.posY = newPosY;
	}
}
