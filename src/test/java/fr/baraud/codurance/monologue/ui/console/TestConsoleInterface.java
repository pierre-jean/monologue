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

/**
 * Created by animus on 13/08/16.
 */
public class TestConsoleInterface {
    private final static String consolePropertyName = "console-interface.properties";

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
    public void testPostInstruction(){
        String alicePost = String.format("Alice -> The sky is blue%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(alicePost.getBytes()), new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.POST, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
        assertEquals("The sky is blue", instruction.getContent());
    }

    @Test
    public void testReadInstruction(){
        String aliceTimeline = String.format("Alice%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceTimeline.getBytes()), new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.SHOW_TIMELINE, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
    }

    @Test
    public void testWallInstruction(){
        String aliceWall = String.format("Alice wall%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceWall.getBytes()), new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.SHOW_WALL, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
    }

    @Test
    public void testFollowInstruction(){
        String aliceFollowsBob = String.format("Alice follows Bob%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceFollowsBob.getBytes()), new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.FOLLOW, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
        assertEquals("Bob", instruction.getContent());
    }

    @Test
    public void testExitInstruction(){
        String exit = String.format("quit%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(exit.getBytes()), new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.EXIT, instruction.getAction());
    }

    @Test
    public void testHelpInstruction(){
        String help = String.format("?%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(help.getBytes()), new ByteArrayOutputStream(), props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.HELP, instruction.getAction());
    }

    @Test
    public void testUnknownEscapeInstruction(){
        String wrongInstruction1 = String.format("%n");
        String wrongInstruction2 = String.format("wall Alice%n");
        String aliceRead = String.format("Alice%n");
        OutputStream out = new ByteArrayOutputStream();
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(String.format(wrongInstruction1+wrongInstruction2+aliceRead).getBytes()), out, props);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.SHOW_TIMELINE, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
        assertTrue(out.toString().contains(props.getProperty(ConsoleInterface.property_message_unknown_command)));
    }

    @Test
    public void testCloseUserInterface(){
        OutputStream out = new ByteArrayOutputStream();
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream("".getBytes()), out, props);
        userInterface.close();
        assertTrue(out.toString().contains(String.format(props.getProperty(ConsoleInterface.property_message_goodbye))));
    }

    @Test
    public void Test1secFormatting(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        assertEquals("1 second ago", consoleInterface.printInUnit(1, 1, "second"));
    }

    @Test
    public void Test2secFormatting(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        assertEquals("2 seconds ago", consoleInterface.printInUnit(275, 100, "second"));
    }

    @Test
    public void TestPrintDelay(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        assertEquals("25 seconds ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400000025000l)));
        assertEquals("2 minutes ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400000120000l)));
        assertEquals("1 hour ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400003700000l)));
        assertEquals("1 day ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400086500000l)));
        assertEquals("1 month ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1402764800000l)));
        assertEquals("1 year ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1433177600000l)));
    }

    @Test
    public void TestWrongDateThrowException(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        assertEquals("25 seconds ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400000025000l)));
        assertEquals("2 minutes ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400000120000l)));
        assertEquals("1 hour ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400003700000l)));
        assertEquals("1 day ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1400086500000l)));
        assertEquals("1 month ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1402764800000l)));
        assertEquals("1 year ago", consoleInterface.printDelay(new Date(1400000000000l), new Date(1433177600000l)));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void TestCompareNullPointerException() {
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        exception.expect(IllegalArgumentException.class);
        consoleInterface.printDelay(new Date(1550000000000l), new Date(1400000000000l));
    }

    @Test
    public void TestWriteTimeline(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        String aliceName = "Alice";
        Date date1 = new Date(1471100010000l);
        Date date4 = new Date(1471100015000l);
        Date now = new Date(1471100020000l);
        Timeline timeline1 = new Timeline("Hello", aliceName, date1, null);
        Timeline timeline4 = new Timeline("Indeed, there is", aliceName, date4, timeline1);
        out.reset();
        consoleInterface.writeTimeline(timeline4, now);
        String result = String.format("Indeed, there is (5 seconds ago)%nHello (10 seconds ago)%n");
        assertEquals(result, out.toString());
    }

    @Test
    public void TestWriteHelp(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        out.reset();
        consoleInterface.writeHelp();
        assertEquals(String.format(props.getProperty(ConsoleInterface.property_message_help)+"%n"),out.toString());
    }
    
    @Test
    public void TestUnkownUser(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        out.reset();
        consoleInterface.writeWarningUnknownUser("Elliot");
        assertEquals(String.format(props.getProperty(ConsoleInterface.property_message_unknown_user)+"Elliot%n"),out.toString());
    }

    @Test
    public void TestWriteWall(){
        ByteArrayInputStream in =  new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleInterface consoleInterface = new ConsoleInterface(in, out, props);
        String aliceName = "Alice";
        String johnName = "John";
        String bobName = "Bob";
        Date date1 = new Date(1471100010000l);
        Date date2 = new Date(1471100015000l);
        Date date3 = new Date(1471100020000l);
        Date date4 = new Date(1471100025000l);
        Date now = new Date(1471100030000l);
        Timeline timeline1 = new Timeline("Hello", aliceName, date1, null);
        Timeline timeline2 = new Timeline("Welcome", johnName, date2, null);
        Timeline timeline3 = new Timeline("There is a nice vibe here!", bobName, date3, null);
        Timeline timeline4 = new Timeline("Indeed, there is", aliceName, date4, timeline1);
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
        consoleInterface.writeWall(aliceWall, now);
        assertEquals(result, out.toString());
    }

}
