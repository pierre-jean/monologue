package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Console interface is an implementation of a UserInterface that provides 
 * a command line interface to interact with the application Monologue
 * @see UserInterface
 * @see fr.baraud.codurance.monologue.Monologue
 */
public class ConsoleInterface implements UserInterface{

    // property key to the logo (in ASCII) to display when app is launched
    private static final String PROPERTY_MESSAGE_LOGO = "ui.console.message.logo";
    // property key to the welcome message when app is launched
    private static final String PROPERTY_MESSAGE_WELCOME = "ui.console.message.welcome";
    // property key to the message displayed when app is closed
    private static final  String PROPERTY_MESSAGE_GOODBYE = "ui.console.message.goodbye";
    // property key to the help message providing guidance on command usages
    private static final String PROPERTY_MESSAGE_HELP = "ui.console.message.help";
    // property key to the warning message when an instruction is not recognized
    private static final String PROPERTY_MESSAGE_UNKNOWN_COMMAND = "ui.console.message.unknown.command";
    // property key to the warning message when a user is not found
    private static final String PROPERTY_MESSAGE_UNKNOWN_USER = "ui.console.message.unknown.user";
    // property key to format a message to the user (ex: adding a line return)
    private static final String PROPERTY_MESSAGE_INFO = "ui.console.message.information.format";
    // property key to format a timeline (should take 2 arguments, the message and the delay)
    private static final String PROPERTY_MESSAGE_TIMELINE = "ui.console.message.timeline.format";
    // property key to format a wall (should take 3 arguments, the user, the message and the delay)
    private static final String PROPERTY_MESSAGE_WALL = "ui.console.message.wall.format";
    // property key to the unit base second in singular
    private static final String PROPERTY_MESSAGE_SECOND_AGO = "ui.console.message.second.ago";
    // property key to the unit base second in plural
    private static final String PROPERTY_MESSAGE_SECONDS_AGO = "ui.console.message.seconds.ago";
    // property key to the unit base second in singular
    private static final String PROPERTY_MESSAGE_MINUTE_AGO = "ui.console.message.minute.ago";
    // property key to the unit base second in plural
    private static final String PROPERTY_MESSAGE_MINUTES_AGO = "ui.console.message.minutes.ago";
    // property key to the unit base second in singular
    private static final String PROPERTY_MESSAGE_HOUR_AGO = "ui.console.message.hour.ago";
    // property key to the unit base second in plural
    private static final String PROPERTY_MESSAGE_HOURS_AGO = "ui.console.message.hours.ago";
    // property key to the unit base second in singular
    private static final String PROPERTY_MESSAGE_DAY_AGO = "ui.console.message.day.ago";
    // property key to the unit base second in plural
    private static final String PROPERTY_MESSAGE_DAYS_AGO = "ui.console.message.days.ago";
    // property key to the unit base second in singular
    private static final String PROPERTY_MESSAGE_MONTH_AGO = "ui.console.message.month.ago";
    // property key to the unit base second in plural
    private static final String PROPERTY_MESSAGE_MONTHS_AGO = "ui.console.message.months.ago";
    // property key to the unit base year in singular
    private static final String PROPERTY_MESSAGE_YEAR_AGO = "ui.console.message.year.ago";
    // property key to the unit base year in plural
    private static final String PROPERTY_MESSAGE_YEARS_AGO = "ui.console.message.years.ago";
    // property key to the pattern used to recognised the post instruction
    private static final String PROPERTY_INSTRUCTION_POST = "ui.console.instructions.post";
    // property key to the pattern used to recognised the wall instruction
    private static final String PROPERTY_INSTRUCTION_WALL = "ui.console.instructions.wall";
    // property key to the pattern used to recognised the follow instruction
    private static final String PROPERTY_INSTRUCTION_FOLLOW = "ui.console.instructions.follow";
    // property key to the pattern used to recognised the help instruction
    private static final String PROPERTY_INSTRUCTION_HELP ="ui.console.instructions.help";
    // property key to the pattern used to recognised the quit instruction
    private static final String PROPERTY_INSTRUCTION_QUIT = "ui.console.instructions.quit";
    // property key to the pattern used to split the instruction in sequences
    private static final String PROPERTY_INSTRUCTION_SPLIT ="ui.console.instructions.split";
    // property key to the pattern printed before an instruction invite
    private static final String PROPERTY_DISPLAY_INSTRUCTION = "ui.console.display.instruction";

    private static final long ONE_SECOND_IN_MS = 1000L;
    private static final long ONE_MIN_IN_MS = 60000L;
    private static final long ONE_HOUR_IN_MS = 3600000L;
    private static final long ONE_DAY_IN_MS = 86400000L;
    private static final long ONE_MONTH_IN_MS = 2592000000L;
    private static final long ONE_YEAR_IN_MS = 31104000000L;

    private static final int ONE_WORD_INSTRUCTION = 1;
    private static final int TWO_WORD_INSTRUCTION = 2;

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

    private final Logger logger = Logger.getLogger(ConsoleInterface.class.getCanonicalName());

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
            write(getText(PROPERTY_DISPLAY_INSTRUCTION));
            userInstruction = parseInstruction(userInputScanner.nextLine());
        } while (userInstruction == null);
        return userInstruction;
    }

    /**
     * display text to the user interface
     * @param text the text to display to the user
     */
    private void write(String text) {
        try {
            userDisplayStream.write(String.format(text).getBytes());
            userDisplayStream.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Write an information to the output, formatted as an information
     * (for instance a line return at the end)
     *
     * @param information the message to display
     * @see #PROPERTY_MESSAGE_INFO
     */
    private void writeInformation(String information){
        String messageFormat = properties.getProperty(PROPERTY_MESSAGE_INFO);
        write(String.format(messageFormat, information));
    }

    /**
     * Display the help to the user
     */
    @Override
    public void writeHelp(){
        writeInformation(getText(PROPERTY_MESSAGE_HELP));
    }

    /**
     * Display a warning message regarding a user not found
     * @param user the user that is missing
     */
    @Override
    public void writeWarningUnknownUser(String user) {
        writeInformation(String.format(getText(PROPERTY_MESSAGE_UNKNOWN_USER),
            user));
    }

    /**
     * Write the timeline to the console
     * @see fr.baraud.codurance.monologue.ui.UserInterface
     */
    @Override
    public void writeTimeline(Timeline timeline, Date currentTime) {
        if (timeline != null){
            writeInformation(String.format(
                properties.getProperty(PROPERTY_MESSAGE_TIMELINE),
                timeline.getMessage(),
                printDelay(timeline.getMessageTimestamp(), currentTime)));
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
            writeInformation(String.format(
                properties.getProperty(PROPERTY_MESSAGE_WALL),
                wall.getUser(),
                wall.getMessage(),
                printDelay(wall.getMessageTimestamp(), currentTime)));
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
     * @return the formatted delayExpression, the plural version or the singular
     * version depending if the division delay/unit is superior to one or not.
     */
    String printInUnit(long delay, long unit, String delayExpressionSingular,
                       String delayExpressionPlural){
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

        if (firstDate.compareTo(now) > 0){
            throw new IllegalArgumentException("the date parameter should be "
                + "previous to the second one");
        }
        long delay = now.getTime() - firstDate.getTime();
        if (delay < ONE_MIN_IN_MS){
            return printInUnit(delay, ONE_SECOND_IN_MS,
                getText(PROPERTY_MESSAGE_SECOND_AGO),
                getText(PROPERTY_MESSAGE_SECONDS_AGO));
        }
        if (delay < ONE_HOUR_IN_MS){
            return printInUnit(delay, ONE_MIN_IN_MS,
                getText(PROPERTY_MESSAGE_MINUTE_AGO),
                getText(PROPERTY_MESSAGE_MINUTES_AGO));
        }
        if (delay < ONE_DAY_IN_MS) {
            return printInUnit(delay, ONE_HOUR_IN_MS,
                getText(PROPERTY_MESSAGE_HOUR_AGO),
                getText(PROPERTY_MESSAGE_HOURS_AGO));
        }
        if (delay < ONE_MONTH_IN_MS) {
            return printInUnit(delay, ONE_DAY_IN_MS,
                getText(PROPERTY_MESSAGE_DAY_AGO),
                getText(PROPERTY_MESSAGE_DAYS_AGO));
        }
        if (delay < ONE_YEAR_IN_MS) {
            return printInUnit(delay, ONE_MONTH_IN_MS,
                getText(PROPERTY_MESSAGE_MONTH_AGO),
                getText(PROPERTY_MESSAGE_MONTHS_AGO));
        }
        return printInUnit(delay, ONE_YEAR_IN_MS,
            getText(PROPERTY_MESSAGE_YEAR_AGO),
            getText(PROPERTY_MESSAGE_YEARS_AGO));
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
        final String quitInstruction = getText(PROPERTY_INSTRUCTION_QUIT);
        final String followInstruction = getText(PROPERTY_INSTRUCTION_FOLLOW);
        final String postInstruction = getText(PROPERTY_INSTRUCTION_POST);
        final String helpInstruction = getText(PROPERTY_INSTRUCTION_HELP);
        final String wallInstruction = getText(PROPERTY_INSTRUCTION_WALL);

        String[] instructionParts = userEntry.split(
            getText(PROPERTY_INSTRUCTION_SPLIT));
        switch (instructionParts.length){

            case ONE_WORD_INSTRUCTION:
                String firstElement = instructionParts[0];
                if (firstElement.isEmpty()){
                    write("");
                    return null;
                }
                if (firstElement.equals(quitInstruction)){
                    return new ExitInstruction();
                }
                if (firstElement.equals(helpInstruction)){
                    return new HelpInstruction();
                }
                return new ShowTimelineInstruction(instructionParts[0]);

            case TWO_WORD_INSTRUCTION:
                if (instructionParts[1].equals(wallInstruction)){
                    return new ShowWallInstruction(instructionParts[0]);
                }
                writeInformation(getText(PROPERTY_MESSAGE_UNKNOWN_COMMAND));
                return null;

            //3 or more words for instruction
            default:
                String secondElement = instructionParts[1];
                if (secondElement.equals(postInstruction)){
                    String postMessage = userEntry.replaceFirst(
                        instructionParts[0]+getText(PROPERTY_INSTRUCTION_SPLIT)
                            +instructionParts[1]+getText(PROPERTY_INSTRUCTION_SPLIT),
                        "");
                    return new PostInstruction(instructionParts[0], postMessage);
                }
                if (secondElement.equals(followInstruction)){
                    return new FollowInstruction(instructionParts[0], instructionParts[2]);
                }
                writeInformation(getText(PROPERTY_MESSAGE_UNKNOWN_COMMAND));
                return null;

        }
    }

    /**
     * Display to the interface the welcome message
     */
    private void sayHello() {
        writeInformation(getText(PROPERTY_MESSAGE_LOGO));
        writeInformation(getText(PROPERTY_MESSAGE_WELCOME));
    }

    /**
     * Display to the interface the goodbye message
     */
    private void sayBye() {
        writeInformation(getText(PROPERTY_MESSAGE_GOODBYE));
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
     * @param property the property key of the text to find in the properties 
     * provide in the constructor
     * @return the result of the property found with the key passed
     */
    private String getText(String property){
        String result = properties.getProperty(property);
        return result == null? "": result;
    }

}
