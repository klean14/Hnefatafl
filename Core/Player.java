package Core;

public class Player {
	private String name;
	
	//1 -> Player 1 / 2 Player 2
	private int ID;
	
	public Player(String name,int ID) {
		this.name=name;
		this.ID = ID;
	}
	
	/****************Getters and setters******************/
	
	public int getID() {return ID;}

	public void setName(String name) {this.name = name;}
	public String getName() {return name;}

	/*****************************************************/
	
	public String toString() {return this.name;}
}
