package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Console interface is an implementation of a UserInterface that provides a command line
 * interface to interact with the application Monologue
 * @see UserInterface
 * @see fr.baraud.codurance.monologue.Monologue
 */
public class ConsoleInterface implements UserInterface{

    /**
     * A message displayed to welcome user when launching the interface and provide some guidance
     */
    public static final String WELCOME_MESSAGE = "%nWelcome to Monologue "
            + "%n------------------"
            + "%nUsage:"
            + "%n"
            + "%n - To post:"
            + "%n   <username> -> <message>"
            + "%n"
            + "%n - To read:"
            + "%n   <user name>"
            + "%n"
            + "%n - To follow:"
            + "%n   <user name> follows <another user>"
            + "%n"
            + "%n - To display wall:"
            + "%n   <user name> wall"
            + "%n"
            + "%n - To quit:"
            + "%n   quit"
            + "%n------------------"
            + "%n";

    /**
     * A message sent when closing the interface (and the app)
     */
    public static final String GOODBYE_MESSAGE = "%n%nThank you for using Monologue%n%n";

    /**
     * A warning message for unrecognized command pattern
     */
    public static final String UNKNOWN_COMMAND_WARNING = "Unknown command";

    /**
     * Some pattern elements to recognized which action is triggered by the user
     */
    private final String INSTRUCTION_KEY_POST = "->";
    private final String INSTRUCTION_KEY_FOLLOW = "follows";
    private final String INSTRUCTION_KEY_WALL = "wall";
    private final String INSTRUCTION_KEY_EXIT= "quit";

    /**
     * A scanner to read from the user input, typically the standard input
     */
    private final Scanner userInputScanner;

    /**
     * The output to display information to the user, typically the standard output
     */
    private final OutputStream userDisplayStream;

    /**
     * Create an instance of ConsoleInterface, that will read from the provided input
     * the user instructions and push information received to the provided output
     * @param in the input stream from where to read input, typically System.in
     * @param out the output stream to where write output, typically System.out
     */
    public ConsoleInterface(InputStream in, OutputStream out){
        this.userInputScanner = new Scanner(in);
        this.userDisplayStream = out;
        sayHello();
    }

    /**
     * Returns the next instruction entered for the input stream
     * If the instruction is not recognized, wait for a next one.
     * @see UserInterface#getNextInstruction()
     */
    public Instruction getNextInstruction() {
        Instruction userInstruction;
        do {
            userInstruction = parseInstruction(userInputScanner.nextLine());
        } while (userInstruction == null);
        return userInstruction;
    }

    /**
     * display the text sent by the App to the user interface
     * @param information the text to display to the user
     * @see UserInterface#writeInformation(String)
     */
    public void writeInformation(String information) {
        try {
            userDisplayStream.write(String.format(information+"%n").getBytes());
            userDisplayStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTimeline(Timeline timeline) {
        if (timeline != null){
            writeInformation(timeline.getMessage());
            writeTimeline(timeline.getNext());
        }
    }

    public void writeWall(Timeline wall) {
        if (wall != null){
            writeInformation(wall.getUser()+" - "+wall.getMessage());
            writeWall(wall.getNext());
        }

    }

    /**
     * Parse a user entry, that represents a line written in the terminal
     * before hitting return key. Depending on the pattern of the entry,
     * will create an Instruction of different Action type and will set
     * associated characteristics such as the author or the content
     * @param userEntry (a full line of an user entry)
     * @return a representation of the instruction entered, or null if
     * the pattern could not be mapped to any known Instruction
     */
    private Instruction parseInstruction(String userEntry){
        if (userEntry == null || userEntry.isEmpty()){
            writeInformation(UNKNOWN_COMMAND_WARNING);
        }
        String[] instructionParts = userEntry.split(" ");
        if (instructionParts.length == 1) {
            if (instructionParts[0].equals(INSTRUCTION_KEY_EXIT)){
                return new Instruction(Action.EXIT, null, null);
            }
            if (instructionParts[0].length() == 0){
                return null;
            }
            return new Instruction(Action.SHOW_TIMELINE, instructionParts[0], null);
        }
        if (instructionParts.length == 2 && instructionParts[1].equals(INSTRUCTION_KEY_WALL)) {
            return new Instruction(Action.SHOW_WALL, instructionParts[0], null);
        }
        if (instructionParts.length > 2 && instructionParts[1].equals(INSTRUCTION_KEY_POST)){
            return new Instruction(Action.POST, instructionParts[0], userEntry.replaceFirst(instructionParts[0]+" "+instructionParts[1]+" ",""));
        }
        if (instructionParts.length > 2 && instructionParts[1].equals(INSTRUCTION_KEY_FOLLOW)){
            return new Instruction(Action.FOLLOW, instructionParts[0], instructionParts[2]);
        }
        writeInformation(UNKNOWN_COMMAND_WARNING);
        return null;
    }

    /**
     * Display to the interface the welcome message
     * @see ConsoleInterface#WELCOME_MESSAGE
     */
    private void sayHello() {
        writeInformation(WELCOME_MESSAGE);
    }

    /**
     * Display to the interface the goodbye message
     * @see ConsoleInterface#GOODBYE_MESSAGE
     */
    private void sayBye() {
        writeInformation(GOODBYE_MESSAGE);
    }

    /**
     * In the case of the ConsoleInterface, there is nothing to close
     * @see UserInterface#close()
     */
    public void close() {
        sayBye();
    }
}
