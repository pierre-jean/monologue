package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestConsoleInterface {

    private final static String consolePropertyName = "console-interface.properties";

    private final static String PROPERTY_MESSAGE_GOODBYE = "ui.console.message.goodbye";
    private static final String PROPERTY_MESSAGE_HELP = "ui.console.message.help";
    private static final String PROPERTY_MESSAGE_INFO = "ui.console.message.information.format";
    private static final String PROPERTY_MESSAGE_UNKNOWN_USER = "ui.console.message.unknown.user";
    private static final String PROPERTY_MESSAGE_UNKNOWN_COMMAND = "ui.console.message.unknown.command";
    private static final String PROPERTY_DISPLAY_INSTRUCTION = "ui.console.display.instruction";


    private Properties props;

    @Before
    public void loadProperties(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(consolePropertyName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void stringShouldGeneratePostInstruction(){
        //given
        String alicePost = String.format("Alice -> The sky is blue%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(alicePost.getBytes()),
            new ByteArrayOutputStream(), props);
        //when
        Instruction instruction = userInterface.getNextInstruction();
        //then
        assertEquals(Action.POST, instruction.getAction());
        assertEquals(PostInstruction.class, instruction.getClass());
    }

    @Test
    public void stringShouldGenerateTimelineInstruction(){
        //given
        String aliceTimeline = String.format("Alice%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceTimeline.getBytes()),
            new ByteArrayOutputStream(), props);
        //when
        Instruction instruction = userInterface.getNextInstruction();
        //then
        assertEquals(ShowTimelineInstruction.class, instruction.getClass());
    }

    @Test
    public void stringShouldGenerateWallInstruction(){
        //given
        String aliceWall = String.format("Alice wall%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceWall.getBytes()),
            new ByteArrayOutputStream(), props);
        //when
        Instruction instruction = userInterface.getNextInstruction();
        //then
        assertEquals(ShowWallInstruction.class, instruction.getClass());
    }

    @Test
    public void testFollowInstruction(){
        String aliceFollowsBob = String.format("Alice follows Bob%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceFollowsBob.getBytes()),
            new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(FollowInstruction.class, instruction.getClass());
    }

    @Test
    public void stringShouldGenerateExitInstruction(){
        //given
        String exit = String.format("quit%n");
        //when
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(exit.getBytes()),
            new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        //then
        assertEquals(ExitInstruction.class, instruction.getClass());
    }

    @Test
    public void stringShouldGenerateHelpInstruction(){
        //given
        String help = String.format("?%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(help.getBytes()),
            new ByteArrayOutputStream(), props);
        //when
        Instruction instruction = userInterface.getNextInstruction();
        //then
        assertEquals(HelpInstruction.class, instruction.getClass());
    }

    @Test
    public void getNextInstructionOnlyReturnsCorrectInstructionAndPrintsWarning(){
        //given
        String wrongInstruction1 = String.format("%n");
        String wrongInstruction2 = String.format("wall Alice%n");
        String rightInstruction = String.format("Alice%n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(
            String.format(wrongInstruction1+wrongInstruction2+rightInstruction).getBytes()), out, props);
        //when
        Instruction instruction = userInterface.getNextInstruction();
        //then
        assertEquals(ShowTimelineInstruction.class, instruction.getClass());
    }

    @Test
    public void closePrintGoodbyeMessage(){
        //given
        OutputStream out = new ByteArrayOutputStream();
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream("".getBytes()), out, props);
        //when
        userInterface.close();
        //then
        assertTrue(out.toString().contains(String.format(props.getProperty(PROPERTY_MESSAGE_GOODBYE))));
    }

    @Test
    public void printInUnit1dividedBy1InSeconds_shouldPrint1secondAgo(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        //then
        assertEquals("1 second ago", consoleInterface.printInUnit(1, 1, "%1d second ago", "%1d seconds ago"));
    }

    @Test
    public void printInUnit275dividedBy100InSeconds_shouldPrint2secondsAgo(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        //then
        assertEquals("2 seconds ago", consoleInterface.printInUnit(275, 100, "%1d second ago", "%1d seconds ago"));
    }

    @Test
    public void  printDelayShouldPrintSecondMinuteOrMonth_dependingOnTheDelay() {
        //given
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        final long begin = 0L;
        final long after25secInMs = 25000L;
        final long after2minInMs = 120000L;
        final long after1hourInMs = 3600000L;
        final long after1dayInMs = 86400000L;
        final long after1MonthInMs = 2678400000L;
        final long after1YearnInMs = 32140800000L;
        //then
        assertEquals("25 seconds ago", consoleInterface.printDelay(new Date(begin), new Date(after25secInMs)));
        assertEquals("2 minutes ago", consoleInterface.printDelay(new Date(begin), new Date(after2minInMs)));
        assertEquals("1 hour ago", consoleInterface.printDelay(new Date(begin), new Date(after1hourInMs)));
        assertEquals("1 day ago", consoleInterface.printDelay(new Date(begin), new Date(after1dayInMs)));
        assertEquals("1 month ago", consoleInterface.printDelay(new Date(begin), new Date(after1MonthInMs)));
        assertEquals("1 year ago", consoleInterface.printDelay(new Date(begin), new Date(after1YearnInMs)));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void secondParameterPreviousToTheFirstParameter_throwsException() {
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Date before = new Date(0L);
        Date after = new Date(5000L);
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        exception.expect(IllegalArgumentException.class);
        //then
        consoleInterface.printDelay(after, before);
    }

    @Test
    public void writeTimeline_printsOutputRightOrder(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        String aliceName = "Alice";
        Date date10secAgo = new Date(0L);
        Date date5secAgo = new Date(5000L);
        Date date = new Date(10000L);
        Timeline timeline1 = new Timeline("Hello", aliceName, date10secAgo, null);
        Timeline timeline4 = new Timeline("Indeed, there is", aliceName, date5secAgo, timeline1);
        out.reset();
        String result = String.format("Indeed, there is (5 seconds ago)%nHello (10 seconds ago)%n");
        //when
        consoleInterface.writeTimeline(timeline4, date);
        //then
        assertEquals(result, out.toString());
    }

    @Test
    public void writeHelp_shouldDisplayHelp(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        out.reset();
        //when
        consoleInterface.writeHelp();
        //then
        assertEquals(String.format(props.getProperty(PROPERTY_MESSAGE_HELP)+"%n"),out.toString());
    }
    
    @Test
    public void callWarningUnknownUser_shouldDisplayWarningMessageWithUserName(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        out.reset();
        String result = String.format(props.getProperty(PROPERTY_MESSAGE_INFO),
            String.format(props.getProperty(PROPERTY_MESSAGE_UNKNOWN_USER), "Elliot"));
        //when
        consoleInterface.writeWarningUnknownUser("Elliot");
        //then
        assertEquals(result, out.toString());
    }

    @Test
    public void callWriteWall_shouldDisplayTheWallCorrectly(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        String aliceName = "Alice";
        String johnName = "John";
        String bobName = "Bob";
        Date date20secAgo = new Date(10000L);
        Date date15secAgo = new Date(15000L);
        Date date10secAgo = new Date(20000L);
        Date date5secAgo = new Date(25000L);
        Date now = new Date(30000L);
        Timeline timeline1 = new Timeline("Hello", aliceName, date20secAgo, null);
        Timeline timeline2 = new Timeline("Welcome", johnName, date15secAgo, null);
        Timeline timeline3 = new Timeline("There is a nice vibe here!", bobName, date10secAgo, null);
        Timeline timeline4 = new Timeline("Indeed, there is", aliceName, date5secAgo, timeline1);
        Timeline aliceWall = new Timeline(timeline4.getMessage(), timeline4.getUser(), timeline4.getMessageTimestamp(),
                new Timeline(timeline3.getMessage(), timeline3.getUser(), timeline3.getMessageTimestamp(),
                        new Timeline(timeline2.getMessage(), timeline2.getUser(), timeline2.getMessageTimestamp(),
                                new Timeline(timeline1.getMessage(), timeline1.getUser(), timeline1.getMessageTimestamp(),
                                        null))));
        out.reset();
        String result = String.format("Alice - Indeed, there is (5 seconds ago)%n" +
                "Bob - There is a nice vibe here! (10 seconds ago)%n" +
                "John - Welcome (15 seconds ago)%n" +
                "Alice - Hello (20 seconds ago)%n");
        //when
        consoleInterface.writeWall(aliceWall, now);
        //then
        assertEquals(result, out.toString());
    }

    @Test
    public void emptyInstruction_ShouldPrintNewInstructionInvitation(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream("%nquit%n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        out.reset();
        String result = String.format(props.getProperty(PROPERTY_DISPLAY_INSTRUCTION), "");
        //when
        consoleInterface.getNextInstruction();
        //then
        assertEquals(result, out.toString());
    }

    @Test
    public void instruction_UnknownInstructionShouldPrintWarningMessage(){
        //given
        ByteArrayInputStream in =  new ByteArrayInputStream(String.format("Unknown instructions composition%nquit%n").getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        out.reset();
        //when
        consoleInterface.getNextInstruction();
        //then
        assertTrue(out.toString().contains(props.getProperty(PROPERTY_MESSAGE_UNKNOWN_COMMAND)));
    }

}
