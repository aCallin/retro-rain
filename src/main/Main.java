package main;

import controller.Controller;

/**
 * The class containing the entry point of the program.
 * 
 * @author Adam
 */
public class Main {
	/**
	 * The entry point of the program. 
	 * @param args these aren't used as of now.
	 */
	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.start();
	}
}
