package fr.baraud.codurance.monologue.console;

import fr.baraud.codurance.monologue.Action;
import fr.baraud.codurance.monologue.Instruction;
import fr.baraud.codurance.monologue.UserInterface;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by animus on 13/08/16.
 */
public class TestConsoleInterface {

    @Test
    public void TestPostInstruction(){
        String alicePost = String.format("Alice -> The sky is blue%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(alicePost.getBytes()), new ByteArrayOutputStream());
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.POST, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
        assertEquals("The sky is blue", instruction.getContent());
    }

    @Test
    public void TestReadInstruction(){
        String aliceTimeline = String.format("Alice%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceTimeline.getBytes()), new ByteArrayOutputStream());
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.SHOW_TIMELINE, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
    }

    @Test
    public void TestWallInstruction(){
        String aliceWall = String.format("Alice wall%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceWall.getBytes()), new ByteArrayOutputStream());
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.SHOW_WALL, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
    }

    @Test
    public void TestFollowInstruction(){
        String aliceFollowsBob = String.format("Alice follows Bob%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(aliceFollowsBob.getBytes()), new ByteArrayOutputStream());
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.FOLLOW, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
        assertEquals("Bob", instruction.getContent());
    }

    @Test
    public void TestExitInstruction(){
        String exit = String.format("quit%n");
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(exit.getBytes()), new ByteArrayOutputStream());
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.EXIT, instruction.getAction());
    }

    @Test
    public void TestUnknownEscapeInstruction(){
        String wrongInstruction1 = String.format("%n");
        String wrongInstruction2 = String.format("wall Alice%n");
        String aliceRead = String.format("Alice%n");
        OutputStream out = new ByteArrayOutputStream();
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream(String.format(wrongInstruction1+wrongInstruction2+aliceRead).getBytes()), out);
        Instruction instruction = userInterface.getNextInstruction();
        assertEquals(Action.SHOW_TIMELINE, instruction.getAction());
        assertEquals("Alice", instruction.getUser());
        assertTrue(out.toString().contains(ConsoleInterface.UNKNOWN_COMMAND_WARNING));
    }

    @Test
    public void TestCloseUserInterface(){
        OutputStream out = new ByteArrayOutputStream();
        UserInterface userInterface = new ConsoleInterface(new ByteArrayInputStream("".getBytes()), out);
        userInterface.close();
        assertTrue(out.toString().contains(String.format(ConsoleInterface.GOODBYE_MESSAGE)));
    }
}