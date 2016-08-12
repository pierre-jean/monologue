package fr.baraud.codurance.monologue;

import java.util.Scanner;

public class Monologue {
	
	public enum Command { EXIT, OTHER }
	
	public static final String WELCOME_MESSAGE = "\nWelcome to Mologue "
			+ "\n------------------"
			+ "\nUsage:"
			+ "\n"
			+ "\n - To post:"
			+ "\n   <username> -> <message>"
			+ "\n"
			+ "\n - To read:"
			+ "\n   <user name>"
			+ "\n"
			+ "\n - To follow:"
			+ "\n   <user name> follows <another user>"
			+ "\n"
			+ "\n - To display wall:"
			+ "\n   <user name> wall"
			+ "\n"
			+ "\n - To quit:"
			+ "\n   quit"
			+ "\n------------------"
			+ "\n";
	
	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE);
		Scanner inputScanner = new Scanner(System.in);
		Command userCommand;
		do {
			String userInput = inputScanner.nextLine();
			userCommand = parseCommand(userInput);
		} while (userCommand != Command.EXIT);
		inputScanner.close();
		System.out.println("\nThank you for using Monologue");
	}
	
	private static Command parseCommand(String command){
		if ("quit".equals(command)){
			return Command.EXIT;
		}
		System.out.println("Command not yet implemented");
		return Command.OTHER;
	}

}
