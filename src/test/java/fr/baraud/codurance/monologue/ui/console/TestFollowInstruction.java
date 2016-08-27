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
public class TestFollowInstruction {

    @Test
    public void action_shouldBeFollow(){
        assertEquals(Action.FOLLOW, new FollowInstruction(null, null).getAction());
    }

    @Test
    public void social_shouldCallFollow(){
        //given
        FollowInstruction instruction = new FollowInstruction("me", "you");
        MockSocial mockSocial = new MockSocial();
        //when
        instruction.apply(mockSocial, new MockUI(), new Date());
        //then
        assertTrue(mockSocial.followed);
        assertEquals("me", mockSocial.follower);
        assertEquals("you", mockSocial.following);
    }

    @Test
    public void ui_printUnknownFollower(){
        //given
        FollowInstruction instruction = new FollowInstruction("notMe", "you");
        MockUI mockUI = new MockUI();
        MockSocial mockSocial = new MockSocial();
        //when
        instruction.apply(mockSocial, mockUI, new Date());
        //then
        assertEquals("notMe", mockUI.unknownUser);

    }

    @Test
    public void ui_printUnknownFollowing(){
        //given
        FollowInstruction instruction = new FollowInstruction("me", "notYou");
        MockUI mockUI = new MockUI();
        MockSocial mockSocial = new MockSocial();
        //when
        instruction.apply(mockSocial, mockUI, new Date());
        //then
        assertEquals("notYou", mockUI.unknownUser);

    }

    public class MockUI implements UserInterface {
        public String unknownUser;

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

        public boolean followed = false;
        public String follower;
        public String following;

        @Override
        public SocialStack post(String user, String message, Date messageTimestamp) {
            return null;
        }

        @Override
        public SocialStack follow(String user, String following) {
            followed = true;
            follower = user;
            this.following = following;
            return this;
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
            return "you".equals(user) || "me".equals(user);
        }
    }

}
