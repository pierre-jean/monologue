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
    final static String property_message_logo = "ui.console.message.logo";
    // property key to the welcome message when app is launched
    final static String property_message_welcome = "ui.console.message.welcome";
    // property key to the message displayed when app is closed
    final static String property_message_goodbye= "ui.console.message.goodbye";
    // property key to the help message providing guidance on command usages
    final static String property_message_help= "ui.console.message.help";
    // property key to the warning message when an instruction is not recognized
    final static String property_message_unknown_command= "ui.console.message.unknown.command";
    // property key to the warning message when a user is not found
    final static String property_message_unknown_user = "ui.console.message.unknown.user";
    // property key to format a message to the user (ex: adding a line return)
    final static String property_message_info = "ui.console.message.information.format";
    // property key to the unit base second in singular
    final static String property_message_second_ago = "ui.console.message.second.ago";
    // property key to the unit base second in plural
    final static String property_message_seconds_ago = "ui.console.message.seconds.ago";
    // property key to the unit base second in singular
    final static String property_message_minute_ago = "ui.console.message.minute.ago";
    // property key to the unit base second in plural
    final static String property_message_minutes_ago = "ui.console.message.minutes.ago";
    // property key to the unit base second in singular
    final static String property_message_hour_ago = "ui.console.message.hour.ago";
    // property key to the unit base second in plural
    final static String property_message_hours_ago = "ui.console.message.hours.ago";
    // property key to the unit base second in singular
    final static String property_message_day_ago = "ui.console.message.day.ago";
    // property key to the unit base second in plural
    final static String property_message_days_ago = "ui.console.message.days.ago";
    // property key to the unit base second in singular
    final static String property_message_month_ago = "ui.console.message.month.ago";
    // property key to the unit base second in plural
    final static String property_message_months_ago = "ui.console.message.months.ago";
    // property key to the unit base year in singular
    final static String property_message_year_ago = "ui.console.message.year.ago";
    // property key to the unit base year in plural
    final static String property_message_years_ago = "ui.console.message.years.ago";
    // property key to the pattern used to recognised the post instruction
    final static String property_instruction_post= "ui.console.instructions.post";
    // property key to the pattern used to recognised the wall instruction
    final static String property_instruction_wall= "ui.console.instructions.wall";
    // property key to the pattern used to recognised the follow instruction
    final static String property_instruction_follow= "ui.console.instructions.follow";
    // property key to the pattern used to recognised the help instruction
    final static String property_instruction_help="ui.console.instructions.help";
    // property key to the pattern used to recognised the quit instruction
    final static String property_instruction_quit= "ui.console.instructions.quit";
    // property key to the pattern used to split the instruction in sequences
    final static String property_instruction_split="ui.console.instructions.split";
    // property key to the pattern printed before an instruction invite
    final static String property_display_instruction = "ui.console.display.instruction";
    // property key to the space separator
    final static String property_display_space = "ui.console.display.space";

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
            write(getText(property_display_instruction));
            userInstruction = parseInstruction(userInputScanner.nextLine());
        } while (userInstruction == null);
        return userInstruction;
    }

    /**
     * display text to the user interface
     * @param information the text to display to the user
     */
    private void write(String text) {
        try {
            userDisplayStream.write(String.format(text).getBytes());
            userDisplayStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Write an information to the output, formated as an information
     * (for instance a line return at the end)
     * 
     * @param information the message to display
     * @see #property_message_info
     */
    private void writeInformation(String information){
        String messageFormat = properties.getProperty(property_message_info);
        write(String.format(messageFormat, information));
    }

    /**
     * Display the help to the user
     */
    @Override
    public void writeHelp(){
        writeInformation(getText(property_message_help));
    }
    
    /**
     * Display a warning message regarding a user not found
     * @param user the user that is missing
     */
    @Override
    public void writeWarningUnknownUser(String user) {
        writeInformation(getText(property_message_unknown_user)+user);
    }

    /**
     * Write the timeline to the console
     * @see fr.baraud.codurance.monologue.ui.UserInterface
     */
    @Override
    public void writeTimeline(Timeline timeline, Date currentTime) {
        if (timeline != null){
            writeInformation(timeline.getMessage()+ " ("+printDelay(timeline.getMessageTimestamp(), currentTime)+")");
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
            writeInformation(wall.getUser()+" - "+wall.getMessage()+ " ("+printDelay(wall.getMessageTimestamp(), currentTime)+")");
            writeWall(wall.getNext(), currentTime);
        }

    }

    /**
     * Return the expression of delay in a sentence.
     * Example: for 2500ms, if the base unit is 1000ms and the unit name is
     * second, it will return "2 seconds ago"
     * @param delay the time in ms to express as delay
     * @param unit in ms to divide the delay in the base we desire
     * @param delayExpressionSingular the sentence to express the delay for 0 or
     * 1 unit. Ex: %1d second ago 
     * @param delayExpressionPlural the sentence to express the delay for more
     * than 1 unit. Ex: %1d seconds ago 
     * @return the formatted delayExpression, the plural versio or the singular 
     * version depending if the division delay/unit is superior to one or not.
     */
    String printInUnit(long delay, long unit, String delayExpressionSingular, String delayExpressionPlural){
        long result = delay/unit;
        return result > 1 ? String.format(delayExpressionPlural, result) :
            String.format(delayExpressionSingular, result);
    }

    /**
     * Compute the time passed between the 2 dates and return a string in the
     * form "20 seconds ago" or "1 year ago"
     * @param firstDate, must be previous to the second parameter now
     * @param now the current date, must be superior to the first parameter
     * @return a sentence representing the time spent since the first date as 
     * if the second date is the current Date and time
     */
    String printDelay(Date firstDate, Date now){

        final long ONE_SECOND_IN_MS = 1000L;
        final long ONE_MIN_IN_MS = 60000L;
        final long ONE_HOUR_IN_MS = 3600000L;
        final long ONE_DAY_IN_MS = 86400000L;
        final long ONE_MONTH_IN_MS = 2592000000L;
        final long ONE_YEAR_IN_MS = 31104000000L;

        if (firstDate.compareTo(now) > 0){
            throw new IllegalArgumentException("the date parameter should be previous to the second one");
        }
        long delay = now.getTime() - firstDate.getTime();
        if (delay < ONE_MIN_IN_MS){
            return printInUnit(delay, ONE_SECOND_IN_MS, getText(property_message_second_ago), getText(property_message_seconds_ago));
        }
        if (delay < ONE_HOUR_IN_MS){
            return printInUnit(delay, ONE_MIN_IN_MS, getText(property_message_minute_ago), getText(property_message_minutes_ago));
        }
        if (delay < ONE_DAY_IN_MS) {
            return printInUnit(delay, ONE_HOUR_IN_MS, getText(property_message_hour_ago), getText(property_message_hours_ago));
        }
        if (delay < ONE_MONTH_IN_MS) {
            return printInUnit(delay, ONE_DAY_IN_MS, getText(property_message_day_ago), getText(property_message_days_ago));
        }
        if (delay < ONE_YEAR_IN_MS) {
            return printInUnit(delay, ONE_MONTH_IN_MS, getText(property_message_month_ago), getText(property_message_months_ago));
        }
        return printInUnit(delay, ONE_YEAR_IN_MS, getText(property_message_year_ago), getText(property_message_years_ago));
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
            writeInformation(getText(property_message_unknown_command));
        }
        String instructionSplitChar = getText(property_instruction_split);
        String[] instructionParts = userEntry.split(instructionSplitChar);
        if (instructionParts.length == 1) {
            if (instructionParts[0].equals(getText(property_instruction_quit))){
                return new Instruction(Action.EXIT, null, null);
            }
            if (instructionParts[0].equals(getText(property_instruction_help))){
                return new Instruction(Action.HELP, null, null);
            }
            if (instructionParts[0].length() == 0){
                return null;
            }
            return new Instruction(Action.SHOW_TIMELINE, instructionParts[0], null);
        }
        if (instructionParts.length == 2 && instructionParts[1].equals(getText(property_instruction_wall))) {
            return new Instruction(Action.SHOW_WALL, instructionParts[0], null);
        }
        if (instructionParts.length > 2 && instructionParts[1].equals(getText(property_instruction_post))){
            return new Instruction(Action.POST, instructionParts[0],
                userEntry.replaceFirst(instructionParts[0]+instructionSplitChar+instructionParts[1]+instructionSplitChar,
                    ""));
        }
        if (instructionParts.length > 2 && instructionParts[1].equals(getText(property_instruction_follow))){
            return new Instruction(Action.FOLLOW, instructionParts[0], instructionParts[2]);
        }
        writeInformation(getText(property_message_unknown_command));
        return null;
    }

    /**
     * Display to the interface the welcome message
     */
    private void sayHello() {
        writeInformation(getText(property_message_logo));
        writeInformation(getText(property_message_welcome));
    }

    /**
     * Display to the interface the goodbye message
     */
    private void sayBye() {
        writeInformation(getText(property_message_goodbye));
    }

    /**
     * In the case of the ConsoleInterface, there is nothing to close
     * @see UserInterface#close()
     */
    @Override
    public void close() {
        sayBye();
    }

    /**
     * Small helper to get a property from the property file.
     * It is slightly shorter to write and prevents null.
     * @param property the property key of the text to find in the properties provide in the constructor
     * @return the result of the property found with the key passed
     */
    private String getText(String property){
        String result = properties.getProperty(property);
        return result == null? "": result;
    }

}
