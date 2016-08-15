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
    public final static String MONOLOGUE_PROPERTIES = "monologue.properties";

    private final String property_message_user_not_found = "monologue.user.not.found";

    private final Properties monologueProperties;

    /**
     * Create a new instance of the App
     */
    public Monologue(Properties props){
        this.monologueProperties = props;
    }

    /**
     * listenInstructions will wait and loop on user instructions.
     * It will execute the instructions and block until the EXIT command is received
     * from any user.
     */
    public void listenInstructions(UserInterface userInterface, SocialStack socialStack){
        Instruction instruction = userInterface.getNextInstruction();
        while (Action.EXIT != instruction.getAction()){
            switch (instruction.getAction()){
            case POST:
                socialStack = socialStack.post(instruction.getUser(), instruction.getContent(), new Date());
                break;
            case SHOW_TIMELINE:
                Timeline timeline = socialStack.getTimeline(instruction.getUser());
                if (timeline == null){
                    userInterface.writeInformation(monologueProperties.getProperty(property_message_user_not_found));
                } else {
                    userInterface.writeTimeline(timeline, new Date());
                }
                break;
            case SHOW_WALL:
                Timeline wall = socialStack.getWall(instruction.getUser());
                if (wall == null){
                    userInterface.writeInformation(monologueProperties.getProperty(property_message_user_not_found));
                } else {
                    userInterface.writeWall(wall, new Date());                		
                }
                break;
            case FOLLOW:
                SocialStack after = socialStack.follow(instruction.getUser(), instruction.getContent());
                if (after == socialStack){
                    userInterface.writeInformation(monologueProperties.getProperty(property_message_user_not_found));
                } else{
                    socialStack = after;
                }
                break;
            case HELP:
                userInterface.writeHelp();
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
        Monologue monologue = new Monologue(loadProperties(MONOLOGUE_PROPERTIES));
        monologue.listenInstructions(new ConsoleInterface(System.in, System.out,Monologue.loadProperties(CONSOLE_PROPERTIES)), new MemorySocialStack(new HashMap<>()));
    }

    public static Properties loadProperties(String filename){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(filename)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

}
