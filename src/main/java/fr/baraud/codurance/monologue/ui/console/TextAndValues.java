package fr.baraud.codurance.monologue.ui.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper class to gather in one place all property keys and property values access
 * in order not to pollute ConsoleInterface logic with too many property jey
 */
public class TextAndValues {

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
    // property key to format a message to the user (ex: adding a line return)
   public final static String property_message_info = "ui.console.message.information.format";
    // property key to format a timeline (should take 2 arguments, the message and the delay)
    public final static String property_message_timeline = "ui.console.message.timeline.format";
    // property key to format a wall (should take 3 arguments, the user, the message and the delay)
    public final static String property_message_wall = "ui.console.message.wall.format";
    // property key to the unit base second in singular
    public final static String property_message_second_ago = "ui.console.message.second.ago";
    // property key to the unit base second in plural
    public final static String property_message_seconds_ago = "ui.console.message.seconds.ago";
    // property key to the unit base second in singular
    public final static String property_message_minute_ago = "ui.console.message.minute.ago";
    // property key to the unit base second in plural
    public final static String property_message_minutes_ago = "ui.console.message.minutes.ago";
    // property key to the unit base second in singular
    public final static String property_message_hour_ago = "ui.console.message.hour.ago";
    // property key to the unit base second in plural
    public final static String property_message_hours_ago = "ui.console.message.hours.ago";
    // property key to the unit base second in singular
    public final static String property_message_day_ago = "ui.console.message.day.ago";
    // property key to the unit base second in plural
    public final static String property_message_days_ago = "ui.console.message.days.ago";
    // property key to the unit base second in singular
    public final static String property_message_month_ago = "ui.console.message.month.ago";
    // property key to the unit base second in plural
    public final static String property_message_months_ago = "ui.console.message.months.ago";
    // property key to the unit base year in singular
    public final static String property_message_year_ago = "ui.console.message.year.ago";
    // property key to the unit base year in plural
    public final static String property_message_years_ago = "ui.console.message.years.ago";
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
     * @param properties The properties should contain all property key from this class (see public members)
     */
    TextAndValues(Properties properties){
        this.properties = properties;
    }

    /**
     * Small helper to get a property from the properties of this file.
     * It is slightly shorter to write and prevents null.
     * @param property the property key of the text to find in the properties
     * provide in the constructor
     * @return the result of the TextAndValues.property found with the key passed
     */
    String getText(String property){
        String result = properties.getProperty(property);
        return result == null? "": result;
    }

    /**
     * Helper to load properties file contained in the classpath
     * @param filename the file name if it is in the resource folder, or the file path
     * @return a property file
     * @throws IOException
     */
    public static Properties loadProperties(String filename) throws IOException{
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(filename)) {
            props.load(resourceStream);
        }
        return props;
    }
}
