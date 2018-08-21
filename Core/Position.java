package Core;
public class Position implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x,y;

	public int getX() {return x;}
	public void setX(int x) {this.x = x;}

	public int getY() {return y;}
	public void setY(int y) {this.y = y;}
	
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
