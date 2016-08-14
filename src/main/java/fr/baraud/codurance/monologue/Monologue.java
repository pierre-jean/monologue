package fr.baraud.codurance.monologue;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.memory.MemorySocialStack;
import fr.baraud.codurance.monologue.ui.console.ConsoleInterface;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;
import java.util.HashMap;

/**
 * Monologue is a social networking application.
 * To interact with the App, you can create a new Instance of this class
 * and call #listenInstructions()
 */
public class Monologue {

	private final UserInterface userInterface;
    private SocialStack socialStack;

    /**
     * Create a new instance of the App
     * @param userInterface the User Interface to interact with the users of the App.
     */
    public Monologue(UserInterface userInterface, SocialStack socialStack){
        this.userInterface = userInterface;
        this.socialStack = socialStack;
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
                case POST:
                    socialStack = socialStack.post(instruction.getUser(), instruction.getContent(), new Date());
                    break;
                case SHOW_TIMELINE:
                    userInterface.writeTimeline(socialStack.getTimeline(instruction.getUser()));
                    break;
                case SHOW_WALL:
                    userInterface.writeWall(socialStack.getWall(instruction.getUser()));
                    break;
                case FOLLOW:
                    socialStack = socialStack.follow(instruction.getUser(), instruction.getContent());
                    break;
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
	    Monologue monologue = new Monologue(new ConsoleInterface(System.in, System.out), new MemorySocialStack(new HashMap<>()));
        monologue.listenInstructions();
    }

}
