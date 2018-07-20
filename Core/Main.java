package Core;

import CommandLine.MainMenuCMD;
import GUI.MainMenuGUI;

public class Main {
	public static void main(String[] args) {
		
		for (String arg : args) {
			
			if (arg.equalsIgnoreCase("-g"))  new MainMenuGUI();
			if (arg.equalsIgnoreCase("-c"))  new MainMenuCMD();
			
		}
		
//		MainMenuGUI menu = new MainMenuGUI();
	}
}
