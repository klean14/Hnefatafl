package Core;
import CommandLine.MainMenuCLI;
import Core.GameLogic;

public class Main {
	public static void main(String[] args) {
		// command line switches
		boolean graphicalMode = false;
		boolean commandLineMode = false;
		
		for (String arg : args) {
			
			if (arg.equalsIgnoreCase("-g"))  graphicalMode = true;
			if (arg.equalsIgnoreCase("-c"))  commandLineMode = true;
			
		}
		
		if(graphicalMode && commandLineMode) {
			System.out.println("ERROR: Both graphical and command line mode selected, select one or the other!");
			System.exit(0);
		}
		
		if(graphicalMode) {
			new GameLogic("Hnefatafl",true);
		} 
		else if(commandLineMode) {
			new MainMenuCLI();
		}
	}
}
