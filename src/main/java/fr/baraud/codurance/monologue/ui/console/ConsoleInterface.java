package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * Console interface is an implementation of a UserInterface that provides 
 * a command line interface to interact with the application Monologue
 * @see UserInterface
 * @see fr.baraud.codurance.monologue.Monologue
 */
public class ConsoleInterface implements UserInterface{

    // property key to the logo (in ASCII) to display when app is launched
    public final static String property_message_logo = "ui.console.message.logo";
    // property key to the welcome message when app is launched
    public final static String property_message_welcome = "ui.console.message.welcome";
    // property key to the message displayed when app is closed
    public final static String property_message_goodbye= "ui.console.message.goodbye";
    // property key to the help message providing guidance on command usages
    public final static String property_message_help= "ui.console.message.help";
    // property key to the warning message when an instruction is not recognized
    public final static String property_message_unknown_command= "ui.console.message.unknown.command";
    // property key to the warning message when a user is not found
    public final static String property_message_unknown_user = "ui.console.message.unknown.user";
    // property key to the pattern used to recognised the post instruction
    public final static String property_instruction_post= "ui.console.instructions.post";
    // property key to the pattern used to recognised the wall instruction
    public final static String property_instruction_wall= "ui.console.instructions.wall";
    // property key to the pattern used to recognised the follow instruction
    public final static String property_instruction_follow= "ui.console.instructions.follow";
    // property key to the pattern used to recognised the help instruction
    public final static String property_instruction_help="ui.console.instructions.help";
    // property key to the pattern used to recognised the quit instruction
    public final static String property_instruction_quit= "ui.console.instructions.quit";
    // property key to the pattern used to split the instruction in sequences
    public final static String property_instruction_split="ui.console.instructions.split";
    // property key to the pattern printed before an instruction invite
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
     * @param properties the property file with the messages and pattern to
     * display
     */
    public ConsoleInterface(InputStream in, OutputStream out, Properties properties){
        this.userInputScanner = new Scanner(in);
        this.userDisplayStream = out;
        this.properties = properties;
        sayHello();
    }

    /**
     * Returns the next instruction entered for the input stream
     * If the instruction is not recognised, wait for a next one.
     * @see UserInterface#getNextInstruction()
     */
    @Override
    public Instruction getNextInstruction() {
        Instruction userInstruction;
        do {
            write(properties.getProperty(property_display_instruction), false);
            userInstruction = parseInstruction(userInputScanner.nextLine());
        } while (userInstruction == null);
        return userInstruction;
    }

    /**
     * display the text to the user interface
     * @param information the text to display to the user
     */
    private void write(String information, boolean includeLineReturn) {
        try {
            if (includeLineReturn){
                information = information + "%n";
            }
            userDisplayStream.write(String.format(information).getBytes());
            userDisplayStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the help to the user
     */
    @Override
    public void writeHelp(){
        write(properties.getProperty(property_message_help), true);
    }
    
    /**
     * Display a warning message regarding a user not found
     * @param user the user that is missing
     */
    @Override
    public void writeWarningUnknownUser(String user) {
        write(properties.getProperty(property_message_unknown_user)+user, true);
    }

    /**
     * Write the timeline to the console
     * @see fr.baraud.codurance.monologue.ui.UserInterface
     */
    @Override
    public void writeTimeline(Timeline timeline, Date currentTime) {
        if (timeline != null){
            write(timeline.getMessage()+ " ("+printDelay(timeline.getMessageTimestamp(), currentTime)+")", true);
            writeTimeline(timeline.getNext(), currentTime);
        }
    }

    /**
     * Write the wall to the console
     * @see fr.baraud.codurance.monologue.ui.UserInterface
     */
    @Override
    public void writeWall(Timeline wall, Date currentTime) {
        if (wall != null){
            write(wall.getUser()+" - "+wall.getMessage()+ " ("+printDelay(wall.getMessageTimestamp(), currentTime)+")", true);
            writeWall(wall.getNext(), currentTime);
        }

    }

    /**
     * Return the expression of delay in a sentence.
     * Example: for 2500ms, if the base unit is 1000ms and the unit name is
     * second, it will return "2 seconds ago"
     * @param delay the time in ms to express as delay
     * @param unit in ms to divide the delay in the base we desire
     * @param unitName the name of the unit (singular) we want to use
     * @return a sentence in the form "X 'unitname' ago" if X is equal or less 
     * than 1, or "X 'unitnames' ago' if X is superior to 1.
     */
    protected String printInUnit(long delay, long unit, String unitName){
        return delay/unit > 1 ? delay/unit +" "+unitName+"s ago" : "1 "+unitName+ " ago";
    }

    /**
     * Compute the time passed between the 2 dates and return a string in the
     * form "20 seconds ago" or "1 year ago"
     * @param firstDate, must be previous to the second parameter now
     * @param now the current date, must be superior to the first parameter
     * @return a sentence representing the time spent since the first date as 
     * if the second date is the current Date and time
     */
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
            write(properties.getProperty(property_message_unknown_command), true);
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
        write(properties.getProperty(property_message_unknown_command), true);
        return null;
    }

    /**
     * Display to the interface the welcome message
     */
    private void sayHello() {
        write(properties.getProperty(property_message_logo), true);
        write(properties.getProperty(property_message_welcome), true);
    }

    /**
     * Display to the interface the goodbye message
     */
    private void sayBye() {
        write(properties.getProperty(property_message_goodbye), true);
    }

    /**
     * In the case of the ConsoleInterface, there is nothing to close
     * @see UserInterface#close()
     */
    @Override
    public void close() {
        sayBye();
    }

}
