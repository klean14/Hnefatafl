package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Core.GameLogic;
import Core.KingPawn;
import Core.Pawn;
import Core.Player;
import Core.Rules;
import Core.TableTop;

public class TableTopGUI extends JFrame implements TableTop {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImageIcon kPawn;
	private ImageIcon wPawn;
	private ImageIcon bPawn;
	private JPanel topPane,player1Panel,player2Panel;
	LineBorder lb = new LineBorder(Color.GRAY);
	
	private ImagePanel contents;
	
	private JLabel roundLabel, playerTurnLabel,p1Image,p1Name,p2Image,p2Name;
	
	private TileButton[][] board;
	
	private int boardSize;
	
	private ArrayList<Pawn> pawn;
	
	private GameLogic game;
	
	private Rules rules = new Rules();
	
	private Player[] player;
	
	private JMenuBar menuBar;
	private JMenu fileMenu,newGame,editMenu;
	private JMenuItem saveItem,loadItem,quitItem,hnefataflItem,tablutItem,tawlbawrddItem,editPlayers;
	
	public TableTopGUI(ArrayList<Pawn> pawns,GameLogic game, int size, String title) {

		this.game = game;
		this.boardSize = size;
		this.board = new TileButton[size][size];
		this.pawn = pawns;
		this.player = game.getPlayer();
		
		this.setTitle(title);
		this.setSize(800, 800);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		// ImageIcons
		kPawn = new ImageIcon("res/kPawn.png");
		wPawn = new ImageIcon("res/wPawn.png");
		bPawn = new ImageIcon("res/bPawn.png");
		
		// Menu Bar
		menuBar = new JMenuBar();
		
		// JMenu
		fileMenu = new JMenu("File");
		newGame = new JMenu("New");
		editMenu = new JMenu("Edit");
		
		// JMenuItem
		hnefataflItem = new JMenuItem("Hnefatafl");
		tablutItem = new JMenuItem("Tablut");
		tawlbawrddItem = new JMenuItem("Tawlbawrdd");
		saveItem = new JMenuItem("Save", new ImageIcon("res/save.png"));
		loadItem = new JMenuItem("Load", new ImageIcon("res/load.png"));
		quitItem = new JMenuItem("Quit");
		editPlayers = new JMenuItem("Edit Players");
		
		// Listeners
		saveItem.addActionListener(new MenuHandler());
		loadItem.addActionListener(new MenuHandler());
		hnefataflItem.addActionListener(new MenuHandler());
		tablutItem.addActionListener(new MenuHandler());
		tawlbawrddItem.addActionListener(new MenuHandler());
		quitItem.addActionListener(new MenuHandler());
		editPlayers.addActionListener(new MenuHandler());
		
//		newGame.addMenuListener(new MenuHandler());
//		fileMenu.addMenuListener(new MenuHandler());
		
		// Add to the menu
		fileMenu.add(newGame);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.add(loadItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);
		newGame.add(hnefataflItem);
		newGame.add(tablutItem);
		newGame.add(tawlbawrddItem);
		
		editMenu.add(editPlayers);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		this.setJMenuBar(menuBar);
		
		// JLabels
		roundLabel = new JLabel("Round: 1", SwingConstants.CENTER);
		roundLabel.setFont(new Font("Rockwell",Font.PLAIN,14));
		playerTurnLabel = new JLabel("Player 1 plays", SwingConstants.CENTER);
		playerTurnLabel.setFont(new Font("Rockwell",Font.BOLD,14));
		
		p1Image = new JLabel();
		p2Image = new JLabel();
		
		p1Image.setIcon(player[0].getPlayerImage());
		p2Image.setIcon(player[1].getPlayerImage());
		
		p1Name = new JLabel(player[0].getName());
		p1Name.setFont(new Font("Rockwell",Font.PLAIN,12));
		p2Name = new JLabel(player[1].getName());
		p2Name.setFont(new Font("Rockwell",Font.PLAIN,12));
		
		// JPanels
		contents = new ImagePanel();
		contents.setLayout(new GridLayout(boardSize,boardSize));
		player1Panel = new JPanel();
		player2Panel = new JPanel();
		
		
		player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.PAGE_AXIS));
		player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.PAGE_AXIS));
				
		topPane = new JPanel(new BorderLayout());
		topPane.setPreferredSize(new Dimension(this.getWidth(),100));
		player1Panel.add(p1Image);
		player1Panel.add(p1Name);
		
		
		player2Panel.add(p2Image);
		player2Panel.add(p2Name);
		
		topPane.add(player1Panel,BorderLayout.WEST);
		topPane.add(player2Panel,BorderLayout.EAST);
		topPane.add(roundLabel,BorderLayout.NORTH);
		topPane.add(playerTurnLabel,BorderLayout.CENTER);
		
		createBoard();
		setUpListeners();
		
		this.add(topPane, BorderLayout.PAGE_START);
		this.add(contents);

		printBoard();
		clearBackground();
	}

	/**
	 * Method to set up the listeners for the buttons. This had to be a different method because listeners are not serializable
	 */
	public void setUpListeners() {
		
//		saveGameButton.addActionListener(new ButtonHandler());
		
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row].addActionListener(new ButtonHandler(board,col,row,this));
			}
		}
	}
	

	/**
	 * Creates the TileButtons
	 */
	public void createBoard() {
		//Add the tiles on the board
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row] = new TileButton(col,row);
				board[col][row].setLayout(new BorderLayout());

				//The tiles that are restricted to the pawns except for the king
				// Temporary solution for the restricted to be the corners and the center of the board. not always the case
				if(row == 0 && col == 0 || row == boardSize-1 && col == 0 || row == 0 && col == boardSize-1 || row == boardSize-1 && col == boardSize-1 || row == (boardSize-1)/2 && col == (boardSize-1)/2) {
					board[col][row].setRestricted(true);
					board[col][row].setText("X");
				}

				contents.add(board[col][row]);
			}
		}
	}

	/**
	 * Draw the pawns on the corresponding tiles
	 */
	public void printBoard() {
		for(TileButton[] tileRow : board) {
			for(TileButton tile : tileRow) {
				int row = tile.getPosX();
				int col = tile.getPosY();
				
				Pawn tilePawn = game.findPawn(row, col);
				if(tilePawn != null) {
					tile.setPawn(tilePawn);
					tile.setOccupied(true);
					
					if(tilePawn instanceof KingPawn) {
						tile.setText("");
						tile.setIcon(kPawn);
					}
					else if((tilePawn.getPlayer().getID() == 2)) {
						tile.setIcon(wPawn);
					}
					else {
						tile.setIcon(bPawn);
					}
					
				}
				else if(!tile.isRestricted()){
					tile.setPawn(null);
					tile.setOccupied(false);
					tile.setText("");
					tile.setIcon(null);
					tile.setEnabled(true);
				}
				else if(tile.isRestricted()) {
					tile.setPawn(null);
					tile.setOccupied(false);
					tile.setIcon(null);
					tile.setText("X");
					tile.setEnabled(true);
				}
			}
		}
	}
	
	/**
	 * Set the background color of the tiles 
	 */
	public void clearBackground() {
		
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				board[col][row].setOpaque(false);
				board[col][row].setContentAreaFilled(false);
				board[col][row].setBorder(lb);
				
//				board[col][row].setBackground(new Color(219,139,71));
			}
		}
	}
	
	/**
	 * Update the label for which player plays
	 * @param player Player whose turn is
	 */
	public void displayPlayer(int player) {
		if(player == 1)
			playerTurnLabel.setText(this.player[0].getName() + " plays");
		else
			playerTurnLabel.setText(this.player[1].getName() + " plays");
		
		this.repaint();
		this.revalidate();
	}
	
	/**
	 * Update the label for how many rounds have passed
	 * @param round Number of round to display
	 */
	public void displayRound(int round) {
		roundLabel.setText("Round: " + (round + 1));
	}
	
	/**
	 * Method that follows the game sequence
	 * @param x x position of the Tile pressed
	 * @param y y position of the Tile pressed
	 */
	public void gameSequence(int x, int y) {
		clearBackground();
		
		game.nextRound(board, x, y);
		
		if(board[x][y].isOccupied() && game.rules.playerTurn(game)) {
			highlightTiles(x,y);
			board[x][y].setBorder(new LineBorder(Color.YELLOW));
		}
		
		
		int round = game.getRound();
		displayRound(round);
		displayPlayer((round % 2)+1);
		printBoard();
		

		if(rules.checkEnd(pawn, board)) {
			JOptionPane.showMessageDialog(null, "Congratulations Player " + ((--round % 2) + 1));
			contents.removeAll();
		}
	}

	
	/**
	 * Highlight the available tiles the pawn can move to in all cardinal directions until it finds another pawn
	 */
	private void highlightTiles(int x, int y) {
		Color c=new Color(244,164,110,100);
		
		for(int i = x+1; i < board.length; i++) {
			if(!board[i][y].isOccupied()) {
//				board[i][y].setBackground(new Color(244,164,96));
//				board[i][y].setBorder(new LineBorder(Color.WHITE));

//				board[i][y].setOpaque(true);
				board[i][y].setContentAreaFilled(true);
				board[i][y].setBackground(c);
			}
			else {
				break;
			}
		}
		for(int i = x-1; i < board.length; i--) {
			if(i < 0 || board[i][y].isOccupied() ) {
				break;
			}
			else {
//				board[i][y].setBackground(new Color(244,164,96));
//				board[i][y].setBorder(new LineBorder(Color.WHITE));

				board[i][y].setContentAreaFilled(true);
				board[i][y].setBackground(c);
			}
		}
		for(int i = y+1; i < board.length; i++) {
			if(!board[x][i].isOccupied()) {
//				board[x][i].setBackground(new Color(244,164,96));
//				board[x][i].setBorder(new LineBorder(Color.WHITE));

				board[x][i].setContentAreaFilled(true);
				board[x][i].setBackground(c);
			}
			else {
				break;
			}
		}
		for(int i = y-1; i < board.length; i--) {
			if(i < 0 || board[x][i].isOccupied() ) {
				break;
			}
			else {
//				board[x][i].setBackground(new Color(244,164,96));
//				board[x][i].setBorder(new LineBorder(Color.WHITE));

				board[x][i].setContentAreaFilled(true);
				board[x][i].setBackground(c);
			}
		}
	}




	
	public class MenuHandler implements MenuListener, ActionListener{
		
		
		@Override
		public void menuSelected(MenuEvent e) {
			
			
		}

	
		@Override
		public void menuDeselected(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == saveItem) {
				saveGame();
			}
			else if(e.getSource() == loadItem) {
				loadGame();
			}
			else if(e.getSource() == quitItem) {
				System.exit(0);
			}
			else if(e.getSource() == hnefataflItem) {
				dispose();
				new GameLogic("Hnefatafl",true);
			}
			else if(e.getSource() == tablutItem) {
				dispose();
				new GameLogic("Tablut",true);
			}
			else if(e.getSource() == tawlbawrddItem) {
				dispose();
				new GameLogic("Tawlbawrdd",true);
			}
			else if(e.getSource() == editPlayers) {
				new EditPlayers(player[0],player[1],p1Image,p2Image,p1Name,p2Name);
//				TableTopGUI.this.revalidate();
//				TableTopGUI.this.repaint();
			}
			
		}
		

	}

	/**
	 * Saves the game state
	 */
	public void saveGame() {

		String filename = new String();
		JFileChooser saveFile = new JFileChooser("saves/");
		
		saveFile.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tafl save games", "ser");
		saveFile.addChoosableFileFilter(filter);
		
		int returnValue = saveFile.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = saveFile.getSelectedFile();
			filename = selectedFile.getAbsolutePath() + ".ser";
			
			try {
				FileOutputStream file = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(file);
				
				out.writeObject(game);
				
				
				file.close();
				out.close();
				
				JOptionPane.showMessageDialog(null, "Game Saved!");
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "File not found", "File Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "There was an error with the file", "File Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Loads the game state
	 */
	private void loadGame() {
		String filename = new String();
		JFileChooser saveFile = new JFileChooser("saves/");
		
		// Set filter for the extension
		saveFile.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tafl save games", "ser");
		saveFile.addChoosableFileFilter(filter);
		
		int returnValue = saveFile.showOpenDialog(null);

		// If the user selected a valid file
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = saveFile.getSelectedFile();
			filename = selectedFile.getAbsolutePath();
		
			GameLogic gl = null;
			
			try {
				FileInputStream file = new FileInputStream(filename);
				ObjectInputStream in = new ObjectInputStream(file);
				
				gl = (GameLogic) in.readObject();
				
				dispose();
				new TableTopGUI(gl.getPawn(),gl,gl.getBoardSize(),gl.getGameName());
				
				file.close();
				in.close();
				
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "File not found", "File Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "There was an error with the file", "File Error", JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
	//			e.printStackTrace();
			}
		}
	}

}
