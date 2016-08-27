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
 * Created by animus on 25/08/16.
 */
public class TestShowTimelineInstruction {

    @Test
    public void action_shouldBeShowTimeline(){
        //given
        Instruction instruction = new ShowTimelineInstruction(null);
        //then
        assertEquals(Action.SHOW_TIMELINE, instruction.getAction());
    }

    @Test
    public void ui_ShowTimelineShouldBeCalled(){
        //given
        Instruction instruction = new ShowTimelineInstruction("Santa");
        MockUI mockUI = new MockUI();
        MockSocial mockSocial = new MockSocial("Hello");
        //when
        instruction.apply(mockSocial, mockUI, new Date());
        //then
        assertTrue(mockUI.timelineWritten);
        assertEquals("Hello", mockUI.message);
        assertEquals("Santa", mockUI.user);
    }

    @Test
    public void ui_UnknownUserShouldBePrinted(){
        //given
        Instruction instruction = new ShowTimelineInstruction("NotMe");
        MockUI mockUI = new MockUI();
        MockSocial mockSocial = new MockSocial("Hello");
        //when
        instruction.apply(mockSocial, mockUI, new Date());
        //then
        assertEquals("NotMe", mockUI.unknownUser);
    }

    public class MockUI implements UserInterface {
        public boolean timelineWritten;
        public String message;
        public String user;
        public String unknownUser;

        @Override
        public Instruction getNextInstruction() {
            return null;
        }

        @Override
        public void writeTimeline(Timeline timeline, Date currentTime) {
            timelineWritten = true;
            message = timeline.getMessage();
            user = timeline.getUser();
        }

        @Override
        public void writeWall(Timeline wall, Date currentTime) {

        }

        @Override
        public void writeHelp() {

        }

        @Override
        public void writeWarningUnknownUser(String user) {
            this.unknownUser = user;
        }

        @Override
        public void close() {

        }
    }

    public class MockSocial implements SocialStack {

        private final String message;

        public MockSocial(String message){
            this.message = message;
        }

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
            return new Timeline(this.message,user, new Date(), null);
        }

        @Override
        public Timeline getWall(String user) {
            return null;
        }

        @Override
        public boolean userExist(String user) {
            return "Santa".equals(user);
        }
    }
}
