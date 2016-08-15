package fr.baraud.codurance.monologue;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.memory.MemorySocialStack;
import fr.baraud.codurance.monologue.ui.console.ConsoleInterface;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * Monologue is a social networking application.
 * To interact with the App, you can create a new Instance of this class
 * and call #listenInstructions()
 */
public class Monologue {

    private final static String CONSOLE_PROPERTIES = "console-interface.properties";

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
                    userInterface.writeTimeline(socialStack.getTimeline(instruction.getUser()), new Date());
                    break;
                case SHOW_WALL:
                    userInterface.writeWall(socialStack.getWall(instruction.getUser()), new Date());
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
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(CONSOLE_PROPERTIES)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Monologue monologue = new Monologue(new ConsoleInterface(System.in, System.out, props), new MemorySocialStack(new HashMap<>()));
        monologue.listenInstructions();
    }

}
