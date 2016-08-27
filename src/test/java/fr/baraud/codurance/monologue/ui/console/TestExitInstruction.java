package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by animus on 24/08/16.
 */
public class TestExitInstruction {

    @Test
    public void action_shouldBeExit(){
        assertEquals(Action.EXIT, new ExitInstruction().getAction());
    }

    @Test
    public void socialStack_shouldBeNull(){
        ExitInstruction exitInstruction = new ExitInstruction();
        assertNull(exitInstruction.apply(new MockSocialStack(), null, new Date()));
    }

    public class MockSocialStack implements SocialStack{

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
