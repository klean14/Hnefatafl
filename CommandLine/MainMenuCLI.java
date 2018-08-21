package CommandLine;

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
			+ "4. Quit");	
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
				reader.close();
				System.exit(0);
			default:
				System.out.println("Wrong Input. Try again.");
			}
		}
	}
}
