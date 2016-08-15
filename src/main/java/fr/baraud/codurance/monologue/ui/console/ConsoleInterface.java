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
import java.util.Properties;
import java.util.Scanner;

/**
 * Console interface is an implementation of a UserInterface that provides a command line
 * interface to interact with the application Monologue
 * @see UserInterface
 * @see fr.baraud.codurance.monologue.Monologue
 */
public class ConsoleInterface implements UserInterface{

    /**
     *
     */
    public final static String property_message_logo = "ui.console.message.logo";
    public final static String property_message_welcome = "ui.console.message.welcome";
    public final static String property_message_goodbye= "ui.console.message.goodbye";
    public final static String property_message_help= "ui.console.message.help";
    public final static String property_message_unknown_command= "ui.console.message.unknown.command";
    public final static String property_instruction_post= "ui.console.instructions.post";
    public final static String property_instruction_wall= "ui.console.instructions.wall";
    public final static String property_instruction_follow= "ui.console.instructions.follow";
    public final static String property_instruction_help="ui.console.instructions.help";
    public final static String property_instruction_quit= "ui.console.instructions.quit";
    public final static String property_instruction_split="ui.console.instructions.split";
    public final static String property_display_instruction = "ui.console.display.instruction";

    /**
     * The properties that contains messages and display custom patters
     */
    private final Properties properties;

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
    public ConsoleInterface(InputStream in, OutputStream out, Properties properties){
        this.userInputScanner = new Scanner(in);
        this.userDisplayStream = out;
        this.properties = properties;
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
            try {
                userDisplayStream.write(properties.getProperty(property_display_instruction).getBytes());
                userDisplayStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void writeHelp(){
        writeInformation(properties.getProperty(property_message_help));
    }

    public void writeTimeline(Timeline timeline, Date currentTime) {
        if (timeline != null){
            writeInformation(timeline.getMessage()+ " ("+printDelay(timeline.getMessageTimestamp(), currentTime)+")");
            writeTimeline(timeline.getNext(), currentTime);
        }
    }

    public void writeWall(Timeline wall, Date currentTime) {
        if (wall != null){
            writeInformation(wall.getUser()+" - "+wall.getMessage()+ " ("+printDelay(wall.getMessageTimestamp(), currentTime)+")");
            writeWall(wall.getNext(), currentTime);
        }

    }

    protected String printInUnit(long delay, long unit, String unitName){
        return delay/unit > 1 ? delay/unit +" "+unitName+"s ago" : "1 "+unitName+ " ago";
    }

    protected String printDelay(Date firstDate, Date now){

        final long ONE_SECOND_IN_MS = 1000l;
        final long ONE_MIN_IN_MS = 60000l;
        final long ONE_HOUR_IN_MS = 3600000l;
        final long ONE_DAY_IN_MS = 86400000l;
        final long ONE_MONTH_IN_MS = 2592000000l;
        final long ONE_YEAR_IN_MS = 31104000000l;

        if (firstDate.compareTo(now) > 0){
            throw new IllegalArgumentException("the date parameter should be previous to the second one");
        }
        long delay = now.getTime() - firstDate.getTime();
        if (delay < ONE_MIN_IN_MS){
            return printInUnit(delay, ONE_SECOND_IN_MS, "second");
        }
        if (delay < ONE_HOUR_IN_MS){
            return printInUnit(delay, ONE_MIN_IN_MS, "minute");
        }
        if (delay < ONE_DAY_IN_MS) {
            return printInUnit(delay, ONE_HOUR_IN_MS, "hour");
        }
        if (delay < ONE_MONTH_IN_MS) {
            return printInUnit(delay, ONE_DAY_IN_MS, "day");
        }
        if (delay < ONE_YEAR_IN_MS) {
            return printInUnit(delay, ONE_MONTH_IN_MS, "month");
        }
        return printInUnit(delay, ONE_YEAR_IN_MS, "year");
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
    protected Instruction parseInstruction(String userEntry){
        if (userEntry == null || userEntry.isEmpty()){
            writeInformation(properties.getProperty(property_message_unknown_command));
        }
        String[] instructionParts = userEntry.split(properties.getProperty(property_instruction_split));
        if (instructionParts.length == 1) {
            if (instructionParts[0].equals(properties.getProperty(property_instruction_quit))){
                return new Instruction(Action.EXIT, null, null);
            }
            if (instructionParts[0].equals(properties.getProperty(property_instruction_help))){
                return new Instruction(Action.HELP, null, null);
            }
            if (instructionParts[0].length() == 0){
                return null;
            }
            return new Instruction(Action.SHOW_TIMELINE, instructionParts[0], null);
        }
        if (instructionParts.length == 2 && instructionParts[1].equals(properties.getProperty(property_instruction_wall))) {
            return new Instruction(Action.SHOW_WALL, instructionParts[0], null);
        }
        if (instructionParts.length > 2 && instructionParts[1].equals(properties.getProperty(property_instruction_post))){
            return new Instruction(Action.POST, instructionParts[0], userEntry.replaceFirst(instructionParts[0]+" "+instructionParts[1]+" ",""));
        }
        if (instructionParts.length > 2 && instructionParts[1].equals(properties.getProperty(property_instruction_follow))){
            return new Instruction(Action.FOLLOW, instructionParts[0], instructionParts[2]);
        }
        writeInformation(properties.getProperty(property_message_unknown_command));
        return null;
    }

    /**
     * Display to the interface the welcome message
     */
    private void sayHello() {
        writeInformation(properties.getProperty(property_message_logo));
        writeInformation(properties.getProperty(property_message_welcome));
    }

    /**
     * Display to the interface the goodbye message
     */
    private void sayBye() {
        writeInformation(properties.getProperty(property_message_goodbye));
    }

    /**
     * In the case of the ConsoleInterface, there is nothing to close
     * @see UserInterface#close()
     */
    public void close() {
        sayBye();
    }
}
