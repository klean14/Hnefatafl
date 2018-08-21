package Core;
import javax.swing.ImageIcon;

public class Player implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private ImageIcon playerImage = new ImageIcon(getClass().getClassLoader().getResource("people.png"));
	
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

	public ImageIcon getPlayerImage() {return playerImage;}
	public void setPlayerImage(ImageIcon playerImage) {this.playerImage = playerImage;}

	/*****************************************************/
	
	public String toString() {return this.name;}
}
