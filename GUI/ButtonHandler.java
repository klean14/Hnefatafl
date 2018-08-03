package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonHandler implements ActionListener{
	private int x;
	private int y;
	private TableTopGUI tt;
	
	/**
	 * Constructor specifically for TileButton instances
	 * @param board The 2D array of TileButtons
	 * @param x x position of the TileButton
	 * @param y y position of the TileButton
	 */
	public ButtonHandler(TileButton[][] board,int x, int y, TableTopGUI tt) {
		this.x = x;
		this.y = y;
		this.tt = tt;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {			
//		if(e.getSource() == saveItem) {saveGame();}
//		else {
			tt.gameSequence(x,y);
//		}
	}

}
