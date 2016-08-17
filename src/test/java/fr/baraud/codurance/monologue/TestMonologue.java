package fr.baraud.codurance.monologue;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import fr.baraud.codurance.monologue.ui.console.TextAndValues;
import org.junit.Test;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

public class TestMonologue {

    @Test
    public void listen_receivePostInstruction_callSocialStackPostMethod(){
        //given
        Monologue monologue = new Monologue();
        Instruction post = new Instruction(Action.POST, "Elliot",
            "FU society is watching");
        MockUI ui = new MockUI(Collections.singletonList(post));
        MockSocialStack socialStack =  new MockSocialStack(null);
        //when
        monologue.listenInstructions(ui, socialStack);
        //then
        assertEquals(post.getUser(), socialStack.postUser);
        assertEquals(post.getContent(), socialStack.postMessage);
    }

    @Test
    public void listen_receiveTimelineInstruction_callUIWriteTimeline(){
        //given
        Monologue monologue = new Monologue();
        Instruction showTimeline = new Instruction(Action.SHOW_TIMELINE,
            "Elliot", null);
        Timeline timeline = new Timeline("FU Society is watching",
            "Elliot", new Date(0), null);
        MockUI ui = new MockUI(Collections.singletonList(showTimeline));
        MockSocialStack socialStack =  new MockSocialStack(timeline);
        //when
        monologue.listenInstructions(ui, socialStack);
        //then
        assertEquals(timeline, ui.lastWrittenTimeline);
    }

    @Test
    public void listen_receiveWallInstruction_callUIWriteWall(){
        //given
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.SHOW_WALL, "Elliot", null);
        Timeline timeline = new Timeline("FU Society is watching",
            "Elliot", new Date(0), null);
        MockUI ui = new MockUI(Collections.singletonList(showWall));
        MockSocialStack socialStack =  new MockSocialStack(timeline);
        // when
        monologue.listenInstructions(ui, socialStack);
        // then
        assertEquals(timeline, ui.lastWrittenWall);
    }

    @Test
    public void listen_receiveFollowInstruction_callSocialStackFollow(){
        //given
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.FOLLOW, "Elliot", "ECorp");
        MockUI ui = new MockUI(Collections.singletonList(showWall));
        MockSocialStack socialStack =  new MockSocialStack(null);
        // when
        monologue.listenInstructions(ui, socialStack);
        // then
        assertEquals(showWall.getUser(), socialStack.follower);
        assertEquals(showWall.getContent(), socialStack.following);
    }

    @Test
    public void listen_receiveFollowUnknownUser_callUIWriteWarningUnknownUser(){
        //given
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.FOLLOW, "Santa", "ECorp");
        MockUI ui = new MockUI(Collections.singletonList(showWall));
        MockSocialStack socialStack =  new MockSocialStack(null);
        //when
        monologue.listenInstructions(ui, socialStack);
        //then
        assertEquals(showWall.getUser(), ui.unknownUser);
    }

    @Test
    public void listen_receiveHelpInstruction_callUIWriteHelp(){
        //given
        Monologue monologue = new Monologue();
        Instruction showHelp = new Instruction(Action.HELP, null, null);
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showHelp}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        //when
        monologue.listenInstructions(ui, socialStack);
        //then
        assertEquals("Help", ui.lastWrittenHelp);
    }

    @Test
    public void listen_timelineForUnknownUser_callUIWriteWarningUnknownUser(){
        //given
        Monologue monologue = new Monologue();
        Instruction showTimeline = new Instruction(Action.SHOW_TIMELINE,
            "Santa", null);
        MockUI ui = new MockUI(Collections.singletonList(showTimeline));
        MockSocialStack socialStack =  new MockSocialStack(null);
        // when
        monologue.listenInstructions(ui, socialStack);
        //then
        assertEquals(showTimeline.getUser(), ui.unknownUser);
    }

    @Test
    public void listen_wallForUnknownUser_callUIWriteWarningUnknownUser(){
        //given
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.SHOW_WALL, "Santa",
            null);
        MockUI ui = new MockUI(Collections.singletonList(showWall));
        MockSocialStack socialStack =  new MockSocialStack(null);
        //when
        monologue.listenInstructions(ui, socialStack);
        //then
        assertEquals(showWall.getUser(), ui.unknownUser);
    }

    @Test
    public void loadProperties_keyHello_equalsWorld(){
        Properties props = new Properties();
        try {
            String testProperty = "DummyProperties.properties";
            props = TextAndValues.loadProperties(testProperty);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("world", props.getProperty("hello"));
    }

    /**
     * A mock version of the stack that set its public members with the values
     * of method arguments when called
     */
    private class MockSocialStack implements SocialStack {

        public String postUser;
        public String postMessage;
        public String follower;
        public String following;

        final Timeline timeline;

        public MockSocialStack(Timeline timeline) {
            this.timeline = timeline;
        }

        @Override
        public SocialStack post(String user, String message,
                                Date messageTimestamp) {
            this.postUser = user;
            this.postMessage = message;
            return this;
        }

        @Override
        public SocialStack follow(String user, String following) {
            this.follower = user;
            this.following = following;
            if (follower.equals("Elliot")){
                return new MockSocialStack(timeline);
            }
            return this;
        }

        @Override
        public Timeline getTimeline(String user) {
            return timeline;
        }

        @Override
        public Timeline getWall(String user) {
            return timeline;
        }

        @Override
        public boolean userExist(String user) {
            return "Elliot".equals(user) || "ECorp".equals(user);
        }

    }

    /**
     * A mock version of the UI that set its public members with the values
     * of method arguments when called
     */
    private class MockUI implements UserInterface {

        public final Iterator<Instruction> instIterator;
        public Timeline lastWrittenTimeline;
        public Timeline lastWrittenWall;
        public String unknownUser;
        public String lastWrittenHelp;

        public MockUI(List<Instruction> instructions) {
            instIterator = instructions.iterator();
        }

        @Override
        public Instruction getNextInstruction() {
            if (instIterator.hasNext()){
                return instIterator.next();
            }
            return new Instruction(Action.EXIT, null, null);
        }

        @Override
        public void writeTimeline(Timeline timeline, Date currentTime) {
            this.lastWrittenTimeline = timeline;
        }

        @Override
        public void writeWall(Timeline wall, Date currentTime) {
            this.lastWrittenWall = wall;
        }

        @Override
        public void writeHelp() {
            this.lastWrittenHelp = "Help";
        }

        @Override
        public void writeWarningUnknownUser(String user) {
            unknownUser = user;
        }

        @Override
        public void close() {
        }


    }



}
