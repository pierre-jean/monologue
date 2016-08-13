package fr.baraud.codurance.monologue;

import fr.baraud.codurance.monologue.console.ConsoleInterface;

/**
 * Monologue is a social networking application.
 * To interact with the App, you can create a new Instance of this class
 * and call #listenInstructions()
 */
public class Monologue {

	private final UserInterface userInterface;

    /**
     * Create a new instance of the App
     * @param userInterface the User Interface to interact with the users of the App.
     */
    public Monologue(UserInterface userInterface){
        this.userInterface = userInterface;
    }

    /**
     * listenInstructions will wait and loop on user instructions.
     * It will execute the instructions and block until the EXIT command is received
     * from any user.
     */
    public void listenInstructions(){
        Instruction instruction = userInterface.getNextInstruction();
        while (Action.EXIT != instruction.getAction()){
            switch (instruction.getAction()){
                default:
                    userInterface.writeAnswer("Action not supported yet");
            }
            instruction = userInterface.getNextInstruction();
        }
        userInterface.close();
    }

    /**
     * Runs a new instance a the app with a console interface handler
     * @param args not used for the moment
     */
	public static void main(String[] args) {
	    Monologue monologue = new Monologue(new ConsoleInterface(System.in, System.out));
        monologue.listenInstructions();
    }

}
