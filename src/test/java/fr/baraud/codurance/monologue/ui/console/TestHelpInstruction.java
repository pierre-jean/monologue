package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.TestMonologue;
import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by animus on 25/08/16.
 */
public class TestHelpInstruction {

    @Test
    public void action_ShouldBeHelp(){
        //given
        Instruction instruction = new HelpInstruction();
        //then
        assertEquals(Action.HELP, instruction.getAction());
    }

    @Test
    public void ui_writeHelpShouldBeCalled(){
        //given
        Instruction instruction = new HelpInstruction();
        MockSocial mockSocial = new MockSocial();
        MockUI mockUI =  new MockUI();
        //when
        instruction.apply(mockSocial, mockUI, new Date());
        //then
        assertTrue(mockUI.helpWritten);
    }

    public class MockUI implements UserInterface {

        public boolean helpWritten = false;

        @Override
        public Instruction getNextInstruction() {
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
            helpWritten = true;
        }

        @Override
        public void writeWarningUnknownUser(String user) {

        }

        @Override
        public void close() {

        }
    }

    public class MockSocial implements SocialStack{

        @Override
        public SocialStack post(String user, String message, Date messageTimestamp) {
            return null;
        }

        @Override
        public SocialStack follow(String user, String following) {
            return null;
        }

        @Override
        public Timeline getTimeline(String user) {
            return null;
        }

        @Override
        public Timeline getWall(String user) {
            return null;
        }

        @Override
        public boolean userExist(String user) {
            return false;
        }
    }
}
