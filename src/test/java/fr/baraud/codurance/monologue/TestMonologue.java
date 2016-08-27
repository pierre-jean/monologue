package fr.baraud.codurance.monologue;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

public class TestMonologue {

    @Test
    public void loadProperties_keyHello_equalsWorld(){
        Properties props = new Properties();
        try {
            String testProperty = "DummyProperties.properties";
            props = Monologue.loadProperties(testProperty);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("world", props.getProperty("hello"));
    }

    @Test
    public void ExitInstruction_shouldBreakTheLoop(){
        UserInterface mockUI = new MockUI();
        Monologue monologue = new Monologue();
        monologue.listenInstructions(mockUI, null);
    }

    public class FakeTimelineInstruction implements Instruction{

        @Override
        public Action getAction() {
            return Action.SHOW_TIMELINE;
        }

        @Override
        public SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate) {
            return socialStack;
        }
    }

    public class FakeExitInstruction implements Instruction{

        @Override
        public Action getAction() {
            return Action.EXIT;
        }

        @Override
        public SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate) {
            return null;
        }
    }

    public class MockUI implements UserInterface {

        Iterator<Instruction> instructionIterator;

        public MockUI(){
            List<Instruction> instructions = Arrays.asList(new FakeTimelineInstruction(), new FakeExitInstruction());
            instructionIterator = instructions.iterator();
        }

        @Override
        public Instruction getNextInstruction() {
            if (instructionIterator.hasNext()){
                return instructionIterator.next();
            }
            return null;
        }

        @Override
        public void writeTimeline(Timeline timeline, Date currentTime) {

        }

        @Override
        public void writeWall(Timeline wall, Date currentTime) {

        }

        @Override
        public void writeHelp() {

        }

        @Override
        public void writeWarningUnknownUser(String user) {

        }

        @Override
        public void close() {

        }
    }

}
