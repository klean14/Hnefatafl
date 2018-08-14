package CommandLine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import Core.GameLogic;

public class MainMenuCLI {

	public MainMenuCLI() {

		while(true) {
			System.out.println("Input a number to start playing or quit");
			System.out.println("1. Hnefatafl" 	+ System.lineSeparator()
			+ "2. Tablut" 		+ System.lineSeparator()
			+ "3. Tawlbawrdd" 	+ System.lineSeparator()
			+ "4. Load Game"	+ System.lineSeparator()
			+ "5. Quit");	
			Scanner reader = new Scanner(System.in);
			int userInput = 0;

			try {
				userInput = reader.nextInt();
			}
			catch(InputMismatchException e) {

			}

			switch(userInput) {
			case 1: 
				new GameLogic("Hnefatafl",false);
				break;
			case 2:
				new GameLogic("Tablut",false);
				break;
			case 3:
				new GameLogic("Tawlbawrdd",false);
				break;
			case 4:

				// Skip the newline
				reader.nextLine();
				System.out.println("Specify the name of the file: ");

				String filename = "saves/"+ reader.nextLine() +".ser";

				GameLogic gl = null;

				try {

					FileInputStream file = new FileInputStream(filename);
					ObjectInputStream in = new ObjectInputStream(file);

					gl = (GameLogic)in.readObject();
					new TableTopCLI(gl.getPawn(),gl,gl.getBoardSize());

					file.close();
					in.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("No game saved found");
				} 
				catch (ClassNotFoundException e) {
				} 
				catch (IOException e) {
				}
				break;
			case 5:
				reader.close();
				System.exit(0);
			default:
				System.out.println("Wrong Input. Try again.");
			}
		}
	}
}
