package fr.baraud.codurance.monologue;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.timelines.memory.MemorySocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;
import fr.baraud.codurance.monologue.ui.console.ConsoleInterface;

/**
 * Created by animus on 13/08/16.
 */
public class TestMonologue {
    
    @Test
    public void TestPost(){
        Monologue monologue = new Monologue();
        Instruction post = new Instruction(Action.POST, "Elliot", "FU society is watching");
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{post}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(post.getUser(), socialStack.postUser);
        assertEquals(post.getContent(), socialStack.postMessage);
    }
    
    @Test
    public void TestTimeline(){
        Monologue monologue = new Monologue();
        Instruction showTimeline = new Instruction(Action.SHOW_TIMELINE, "Elliot", null);
        Timeline timeline = new Timeline("FU Society is watching", "Elliot", new Date(0), null);
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showTimeline}));
        MockSocialStack socialStack =  new MockSocialStack(timeline);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(timeline, ui.lastWrittenTimeline);
    }
    
    @Test
    public void TestWall(){
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.SHOW_WALL, "Elliot", null);
        Timeline timeline = new Timeline("FU Society is watching", "Elliot", new Date(0), null);
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showWall}));
        MockSocialStack socialStack =  new MockSocialStack(timeline);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(timeline, ui.lastWrittenWall);
    }
    
    @Test
    public void TestFollow(){
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.FOLLOW, "Elliot", "ECorp");
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showWall}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(showWall.getUser(), socialStack.follower);
        assertEquals(showWall.getContent(), socialStack.following);
    }
    
    @Test
    public void TestFollowUnkownUser(){
        Monologue monologue = new Monologue();
        Instruction showWall = new Instruction(Action.FOLLOW, "Santa", "ECorp");
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showWall}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(showWall.getUser(), ui.unknownUser);
    }
    
    @Test
    public void TestHelp(){
        Monologue monologue = new Monologue();
        Instruction showHelp = new Instruction(Action.HELP, null, null);
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showHelp}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        monologue.listenInstructions(ui, socialStack);
        assertEquals("Help", ui.lastWrittenInfo);
    }
    
    @Test
    public void TestUnkownUserTimeline(){
        Monologue monologue = new Monologue();
        Instruction showTimeline = new Instruction(Action.SHOW_TIMELINE, "Elliot", null);
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showTimeline}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(showTimeline.getUser(), ui.unknownUser);
    }
    
    @Test
    public void TestUnkownUserWall(){
        Monologue monologue = new Monologue();
        Instruction showTimeline = new Instruction(Action.SHOW_WALL, "Elliot", null);
        MockUI ui = new MockUI(Arrays.asList(new Instruction[]{showTimeline}));
        MockSocialStack socialStack =  new MockSocialStack(null);
        monologue.listenInstructions(ui, socialStack);
        assertEquals(showTimeline.getUser(), ui.unknownUser);
    }
    
    private class MockSocialStack implements SocialStack {
        
        public String postUser;
        public String postMessage;
        
        public String follower;
        public String following;
        
        Timeline timeline;

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
        
    }
    
    private class MockUI implements UserInterface {
        
        public Iterator<Instruction> instIterator;
        
        public String lastWrittenInfo;
        public Timeline lastWrittenTimeline;
        public Timeline lastWrittenWall;
        
        public String unknownUser;
        
        public MockUI(List<Instruction> instructions) {
            instIterator = instructions.iterator();
        }

        @Override
        public Instruction getNextInstruction() {
            while (instIterator.hasNext()){
                return instIterator.next();
            }
            return new Instruction(Action.EXIT, null, null);
        }

        @Override
        public void writeInformation(String information) {
            this.lastWrittenInfo = information;
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
            this.lastWrittenInfo = "Help";
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
