package fr.baraud.codurance.monologue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.memory.MemorySocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;
import fr.baraud.codurance.monologue.ui.console.ConsoleInterface;

/**
 * Monologue is a social networking application.
 * To interact with the App, you can create a new Instance of this class
 * and call #listenInstructions(UserInterface userInterface, SocialStack socialStack)
 */
public class Monologue {

    private static final String CONSOLE_PROPERTIES = "console-interface.properties";

    /**
     * listenInstructions will wait and loop on user instructions.
     * It will execute the instructions and block until the EXIT command is received
     * from any user.
     * @param userInterface the user interface that interact with user
     * @param socialStack the database of users and timelines
     * @see fr.baraud.codurance.monologue.timelines.SocialStack
     * @see fr.baraud.codurance.monologue.ui.UserInterface
     */
    void listenInstructions(UserInterface userInterface, SocialStack socialStack){
        Instruction instruction;
        SocialStack newSocialStack = socialStack;
        do {
            instruction = userInterface.getNextInstruction();
            newSocialStack = instruction.apply(newSocialStack, userInterface, new Date());
        } while (Action.EXIT != instruction.getAction());
        userInterface.close();
    }

    /**
     * Runs a new instance a the app with a console interface handler
     */
    public static void main(String[] args) {
        Monologue monologue = new Monologue();
        Properties consoleProps = new Properties();
        Logger logger = Logger.getLogger(Monologue.class.getCanonicalName());
        try {
            consoleProps = Monologue.loadProperties(CONSOLE_PROPERTIES);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        monologue.listenInstructions(new ConsoleInterface(System.in, System.out, consoleProps), new MemorySocialStack());
    }

    /**
     * Helper to load a property within the classpath from its filename
     * @param filename the file name if it is in the resource folder, or the file path
     * @return a property file
     * @throws IOException if something goes wrong when reading the property file
     */
    static Properties loadProperties(String filename) throws IOException{
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(filename)) {
            props.load(resourceStream);
        }
        return props;
    }

}
