package fr.baraud.codurance.monologue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.timelines.memory.MemorySocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;
import fr.baraud.codurance.monologue.ui.console.ConsoleInterface;

/**
 * Monologue is a social networking application.
 * To interact with the App, you can create a new Instance of this class
 * and call #listenInstructions()
 */
public class Monologue {

    public final static String CONSOLE_PROPERTIES = "console-interface.properties";

    /**
     * Create a new instance of the App
     */
    public Monologue(){
    }

    /**
     * listenInstructions will wait and loop on user instructions.
     * It will execute the instructions and block until the EXIT command is received
     * from any user.
     */
    public void listenInstructions(UserInterface userInterface, SocialStack socialStack){
        Instruction instruction;
        do {
            instruction = userInterface.getNextInstruction();
            switch (instruction.getAction()){
            case POST:
                socialStack = socialStack.post(instruction.getUser(), instruction.getContent(), new Date());
                break;
            case SHOW_TIMELINE:
                Timeline timeline = socialStack.getTimeline(instruction.getUser());
                if (timeline == null){
                    userInterface.writeWarningUnknownUser(instruction.getUser());
                } else {
                    userInterface.writeTimeline(timeline, new Date());
                }
                break;
            case SHOW_WALL:
                Timeline wall = socialStack.getWall(instruction.getUser());
                if (wall == null){
                    userInterface.writeWarningUnknownUser(instruction.getUser());
                } else {
                    userInterface.writeWall(wall, new Date());                		
                }
                break;
            case FOLLOW:
                SocialStack after = socialStack.follow(instruction.getUser(), instruction.getContent());
                if (after == socialStack){
                    userInterface.writeWarningUnknownUser(instruction.getUser());
                } else{
                    socialStack = after;
                }
                break;
            case HELP:
                userInterface.writeHelp();
            case EXIT:
                break;
            } 
        } while (Action.EXIT != instruction.getAction());
        userInterface.close();
    }

    /**
     * Runs a new instance a the app with a console interface handler
     * @param args not used for the moment
     */
    public static void main(String[] args) {
        Monologue monologue = new Monologue();
        Properties consoleProps = new Properties();
        try {
            consoleProps = Monologue.loadProperties(CONSOLE_PROPERTIES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        monologue.listenInstructions(new ConsoleInterface(System.in, System.out, consoleProps), new MemorySocialStack(new HashMap<>()));
    }

    public static Properties loadProperties(String filename) throws IOException{
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(filename)) {
            props.load(resourceStream);
        }
        return props;
    }

}
