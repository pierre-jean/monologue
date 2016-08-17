package fr.baraud.codurance.monologue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.memory.MemorySocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;
import fr.baraud.codurance.monologue.ui.console.ConsoleInterface;
import fr.baraud.codurance.monologue.ui.console.TextAndValues;

/**
 * Monologue is a social networking application.
 * To interact with the App, you can create a new Instance of this class
 * and call #listenInstructions(UserInterface userInterface, SocialStack socialStack)
 */
public class Monologue {

    private final static String CONSOLE_PROPERTIES = "console-interface.properties";

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
        do {
            instruction = userInterface.getNextInstruction();
            switch (instruction.getAction()){
                case POST:
                    socialStack = socialStack.post(instruction.getUser(), instruction.getContent(), new Date());
                    break;
                case SHOW_TIMELINE:
                    if (!socialStack.userExist(instruction.getUser())){
                        userInterface.writeWarningUnknownUser(instruction.getUser());
                    } else {
                        userInterface.writeTimeline(socialStack.getTimeline(instruction.getUser()), new Date());
                    }
                    break;
                case SHOW_WALL:
                    if (!socialStack.userExist(instruction.getUser())){
                        userInterface.writeWarningUnknownUser(instruction.getUser());
                    } else {
                        userInterface.writeWall(socialStack.getWall(instruction.getUser()), new Date());
                    }
                    break;
                case FOLLOW:
                    if (!socialStack.userExist(instruction.getUser())){
                        userInterface.writeWarningUnknownUser(instruction.getUser());
                    } else if (!socialStack.userExist(instruction.getContent())){
                        userInterface.writeWarningUnknownUser(instruction.getContent());
                    } else{
                        socialStack = socialStack.follow(instruction.getUser(), instruction.getContent());
                    }
                    break;
                case HELP:
                    userInterface.writeHelp();
                case EXIT:
                    break;
                default:
                    System.out.println("Unrecognized action: "+instruction.getAction());
            }
        } while (Action.EXIT != instruction.getAction());
        userInterface.close();
    }

    /**
     * Runs a new instance a the app with a console interface handler
     */
    public static void main(String[] args) {
        Monologue monologue = new Monologue();
        Properties consoleProps = new Properties();
        try {
            consoleProps = TextAndValues.loadProperties(CONSOLE_PROPERTIES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        monologue.listenInstructions(new ConsoleInterface(System.in, System.out, consoleProps), new MemorySocialStack());
    }


}
