package Core;

import CommandLine.MainMenuCMD;

public class Main {
	public static void main(String[] args) {
		
		for (String arg : args) {
			
			if (arg.equalsIgnoreCase("-g"))  new GameLogic("Hnefatafl",true);
			if (arg.equalsIgnoreCase("-c"))  new MainMenuCMD();
			
		}
	}
}
