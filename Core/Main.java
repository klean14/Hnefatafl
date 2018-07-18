package Core;

import GUI.MainMenuGUI;

public class Main {
	public static void main(String[] args) {
		
		for (String arg : args) {
			
			if (arg.equalsIgnoreCase("-g"))  new MainMenuGUI();
			if (arg.equalsIgnoreCase("-c"))  new GameLogic("Hnefatafl",false);
			
		}
		
//		MainMenuGUI menu = new MainMenuGUI();
	}
}
