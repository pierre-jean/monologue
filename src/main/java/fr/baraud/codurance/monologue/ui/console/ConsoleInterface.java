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

    /**
     * Class with all public 
     */
    private final TextAndValues textAndValues;

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
     * @param properties the TextAndValues.properties file with the messages and pattern to
     * display
     */
    public ConsoleInterface(InputStream in, OutputStream out, Properties properties){
        this.userInputScanner = new Scanner(in);
        this.userDisplayStream = out;
        this.textAndValues = new TextAndValues(properties);
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
            write(textAndValues.getText(TextAndValues.property_display_instruction));
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
            e.printStackTrace();
        }
    }

    /**
     * Write an information to the output, formatted as an information
     * (for instance a line return at the end)
     *
     * @param information the message to display
     * @see TextAndValues#property_message_info
     */
    private void writeInformation(String information){
        String messageFormat = textAndValues.getText(TextAndValues.property_message_info);
        write(String.format(messageFormat, information));
    }

    /**
     * Display the help to the user
     */
    @Override
    public void writeHelp(){
        writeInformation(textAndValues.getText(TextAndValues.property_message_help));
    }

    /**
     * Display a warning message regarding a user not found
     * @param user the user that is missing
     */
    @Override
    public void writeWarningUnknownUser(String user) {
        writeInformation(String.format(textAndValues.getText(TextAndValues.property_message_unknown_user),
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
                textAndValues.getText(TextAndValues.property_message_timeline),
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
                textAndValues.getText(TextAndValues.property_message_wall),
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

        final long ONE_SECOND_IN_MS = 1000L;
        final long ONE_MIN_IN_MS = 60000L;
        final long ONE_HOUR_IN_MS = 3600000L;
        final long ONE_DAY_IN_MS = 86400000L;
        final long ONE_MONTH_IN_MS = 2592000000L;
        final long ONE_YEAR_IN_MS = 31104000000L;

        if (firstDate.compareTo(now) > 0){
            throw new IllegalArgumentException("the date parameter should be "
                + "previous to the second one");
        }
        long delay = now.getTime() - firstDate.getTime();
        if (delay < ONE_MIN_IN_MS){
            return printInUnit(delay, ONE_SECOND_IN_MS,
                textAndValues.getText(TextAndValues.property_message_second_ago),
                textAndValues.getText(TextAndValues.property_message_seconds_ago));
        }
        if (delay < ONE_HOUR_IN_MS){
            return printInUnit(delay, ONE_MIN_IN_MS,
                textAndValues.getText(TextAndValues.property_message_minute_ago),
                textAndValues.getText(TextAndValues.property_message_minutes_ago));
        }
        if (delay < ONE_DAY_IN_MS) {
            return printInUnit(delay, ONE_HOUR_IN_MS,
                textAndValues.getText(TextAndValues.property_message_hour_ago),
                textAndValues.getText(TextAndValues.property_message_hours_ago));
        }
        if (delay < ONE_MONTH_IN_MS) {
            return printInUnit(delay, ONE_DAY_IN_MS,
                textAndValues.getText(TextAndValues.property_message_day_ago),
                textAndValues.getText(TextAndValues.property_message_days_ago));
        }
        if (delay < ONE_YEAR_IN_MS) {
            return printInUnit(delay, ONE_MONTH_IN_MS,
                textAndValues.getText(TextAndValues.property_message_month_ago),
                textAndValues.getText(TextAndValues.property_message_months_ago));
        }
        return printInUnit(delay, ONE_YEAR_IN_MS,
            textAndValues.getText(TextAndValues.property_message_year_ago),
            textAndValues.getText(TextAndValues.property_message_years_ago));
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
        final int EMPTY_INSTRUCTION = 0;
        final int ONE_WORD_INSTRUTION = 1;
        final int TWO_WORD_INSTRUCTION = 2;
        final String quitInstruction = textAndValues.getText(TextAndValues.property_instruction_quit);
        final String followInstruction = textAndValues.getText(TextAndValues.property_instruction_follow);
        final String postInstruction = textAndValues.getText(TextAndValues.property_instruction_post);
        final String helpInstruction = textAndValues.getText(TextAndValues.property_instruction_help);
        final String wallInstruction = textAndValues.getText(TextAndValues.property_instruction_wall);

        String[] instructionParts = userEntry.split(
            textAndValues.getText(TextAndValues.property_instruction_split));
        switch (instructionParts.length){

            case EMPTY_INSTRUCTION:
                return null;

            case ONE_WORD_INSTRUTION:
                String firstElement = instructionParts[0];
                if (firstElement.isEmpty()){
                    writeInformation(textAndValues.getText(TextAndValues.property_message_unknown_command));
                    return null;
                }
                if (firstElement.equals(quitInstruction)){
                    return new Instruction(Action.EXIT, null, null);
                }
                if (firstElement.equals(helpInstruction)){
                    return new Instruction(Action.HELP, null, null);
                }
                return new Instruction(Action.SHOW_TIMELINE, instructionParts[0], null);

            case TWO_WORD_INSTRUCTION:
                if (instructionParts[1].equals(wallInstruction)){
                    return new Instruction(Action.SHOW_WALL, instructionParts[0], null);
                }
                writeInformation(textAndValues.getText(TextAndValues.property_message_unknown_command));
                return null;

            //3 or more words for instruction
            default:
                String secondElement = instructionParts[1];
                if (secondElement.equals(postInstruction)){
                    String postMessage = userEntry.replaceFirst(
                        instructionParts[0]+textAndValues.getText(TextAndValues.property_instruction_split)
                            +instructionParts[1]+textAndValues.getText(TextAndValues.property_instruction_split),
                        "");
                    return new Instruction(Action.POST,
                        instructionParts[0],
                        postMessage);
                }
                if (secondElement.equals(followInstruction)){
                    return new Instruction(Action.FOLLOW,
                        instructionParts[0],
                        instructionParts[2]);
                }
                writeInformation(textAndValues.getText(TextAndValues.property_message_unknown_command));
                return null;

        }
    }

    /**
     * Display to the interface the welcome message
     */
    private void sayHello() {
        writeInformation(textAndValues.getText(TextAndValues.property_message_logo));
        writeInformation(textAndValues.getText(TextAndValues.property_message_welcome));
    }

    /**
     * Display to the interface the goodbye message
     */
    private void sayBye() {
        writeInformation(textAndValues.getText(TextAndValues.property_message_goodbye));
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
