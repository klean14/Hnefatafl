package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import Core.Player;

public class EditPlayers extends JFrame implements ActionListener{
	JButton updateButton,chooseP1Image,chooseP2Image;
	
	JLabel p1Image,p2Image,p1ImageMain,p2ImageMain, p1NameMain,p2NameMain;
	
	JPanel p1Contents,p2Contents;
	
	JTextField p1NameText,p2NameText;
	
	Player p1,p2;
	
	public EditPlayers(Player p1, Player p2, JLabel p1Image, JLabel p2Image, JLabel p1Name, JLabel p2Name) {
		this.p1 = p1;
		this.p2 = p2;
		this.p1ImageMain = p1Image;
		this.p2ImageMain = p2Image;
		this.p1NameMain = p1Name;
		this.p2NameMain = p2Name;
		
		this.setTitle("Edit Players");
		this.setSize(500, 300);
		this.setResizable(false);
		this.setVisible(true);
		
		//Show the grid in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		// JPanels
		p1Contents = new JPanel();
		p1Contents.setLayout(new BoxLayout(p1Contents,BoxLayout.PAGE_AXIS));
		p2Contents = new JPanel();
		p2Contents.setLayout(new BoxLayout(p2Contents,BoxLayout.PAGE_AXIS));
		
		// JLabels
		this.p1Image = new JLabel();
		this.p2Image = new JLabel();
		this.p1Image.setIcon(p1.getPlayerImage());
		this.p2Image.setIcon(p2.getPlayerImage());
		
		// JButtons
		updateButton = new JButton("Update");
		chooseP1Image = new JButton("Select Image");
		chooseP2Image = new JButton("Select Image");
		
		// Listeners
		updateButton.addActionListener(this);
		chooseP1Image.addActionListener(this);
		chooseP2Image.addActionListener(this);
		
		// JTextFields
		p1NameText = new JTextField(p1.getName());
		p2NameText = new JTextField(p2.getName());
		
		// Adding to the frame and panels
		p1Contents.add(this.p1Image);
		p1Contents.add(chooseP1Image);
		p1Contents.add(p1NameText);
		
		p2Contents.add(this.p2Image);
		p2Contents.add(chooseP2Image);
		p2Contents.add(p2NameText);
		
		this.add(updateButton,BorderLayout.SOUTH);
		this.add(p1Contents,BorderLayout.WEST);
		this.add(p2Contents,BorderLayout.EAST);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Opens JFileChooser to get image for player 1
		if(e.getSource() == chooseP1Image) {

			JFileChooser saveFile = new JFileChooser("res/");
			
			// Set extension filters to accept only png and jpg
			saveFile.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png","jpg");
			saveFile.addChoosableFileFilter(filter);
			
			int returnValue = saveFile.showOpenDialog(null);

			// If file selected is a valid picture
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				
				// Resize image
				ImageIcon img = resizeImage(saveFile);
				
				p1.setPlayerImage(img);
				p1Image.setIcon(p1.getPlayerImage());
			}
		}
		else if(e.getSource() == chooseP2Image) {
			JFileChooser saveFile = new JFileChooser("res/");
			
			saveFile.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png","jpg");
			saveFile.addChoosableFileFilter(filter);
			
			int returnValue = saveFile.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				
				ImageIcon img = resizeImage(saveFile);
				
				p2.setPlayerImage(img);
				p2Image.setIcon(p2.getPlayerImage());
			}
		}
		// Updates the images and names of the players and closes this frame
		else if(e.getSource() == updateButton) {
			p1.setName(p1NameText.getText());
			p1NameMain.setText(p1.getName());
			p1ImageMain.setIcon(p1.getPlayerImage());
			
			p2.setName(p2NameText.getText());
			p2NameMain.setText(p2.getName());
			p2ImageMain.setIcon(p2.getPlayerImage());
			dispose();
		}
		
	}

	/**
	 * Method to resize the image to 64 x 64
	 * @param saveFile
	 * @return the resized Image
	 */
	private ImageIcon resizeImage(JFileChooser saveFile) {
		File selectedFile = saveFile.getSelectedFile();
		String filename = selectedFile.getAbsolutePath();
		
		File inputFile = new File(filename);
		BufferedImage inputImage = null;
		try {
			inputImage = ImageIO.read(inputFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
		
		Image newImage = inputImage.getScaledInstance(64, 64, Image.SCALE_DEFAULT);
		ImageIcon img = new ImageIcon(newImage);
		return img;
	}

}

