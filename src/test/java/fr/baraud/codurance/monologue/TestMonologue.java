package fr.baraud.codurance.monologue;

import fr.baraud.codurance.monologue.Monologue;
import fr.baraud.codurance.monologue.console.ConsoleInterface;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by animus on 13/08/16.
 */
public class TestMonologue {

    @Test
    public void TestNoException(){
        String alicePostInstruction = "Alice -> Are you joining me for breakfast?";
        String sendInstruction = "%n";
        String exitInstruction = "quit";
        ByteArrayInputStream simulatedAliceEntry = new ByteArrayInputStream(String.format((alicePostInstruction+sendInstruction+exitInstruction)).getBytes());
        ConsoleInterface consoleInterface = new ConsoleInterface(simulatedAliceEntry, new ByteArrayOutputStream());
        Monologue monologue = new Monologue(consoleInterface);
        monologue.listenInstructions();
    }
}
