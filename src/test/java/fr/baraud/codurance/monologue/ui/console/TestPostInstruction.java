package fr.baraud.codurance.monologue.ui.console;

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
 * Created by animus on 24/08/16.
 */
public class TestPostInstruction {

    @Test
    public void action_shouldBePost(){
        assertEquals(Action.POST, new PostInstruction(null, null).getAction());
    }

    @Test
    public void social_shouldCallPost(){
        //given
        PostInstruction instruction = new PostInstruction(null, null);
        MockSocial mockSocial = new MockSocial();
        //when
        instruction.apply(mockSocial, new MockUI(), new Date());
        //then
        assertTrue(mockSocial.posted);

    }

    public class MockSocial implements SocialStack{
        boolean posted = false;

        @Override
        public SocialStack post(String user, String message, Date messageTimestamp) {
            posted = true;
            return this;
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

    public class MockUI implements UserInterface{

        boolean timelineWrote = false;

        @Override
        public Instruction getNextInstruction() {
            return null;
        }

        @Override
        public void writeTimeline(Timeline timeline, Date currentTime) {
            timelineWrote = true;
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
