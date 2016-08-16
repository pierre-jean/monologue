package fr.baraud.codurance.monologue.timelines.memory;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.timelines.User;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class TestMemorySocialStack {

    @Test
    public void timeline_getByName_returnTimeline(){
        //given
        Timeline aliceTimeline = new Timeline("I want this test to pass",
            "Alice", new Date(0), null);
        User alice = new User("Alice", Collections.emptyList(), aliceTimeline);
        SocialStack socialStack = new MemorySocialStack(
            Collections.singletonMap(alice.getName(), alice));
        //when
        Timeline foundTimeline = socialStack.getTimeline(alice.getName());
        //then
        assertEquals(aliceTimeline, foundTimeline);
    }

    @Test
    public void timeline_post_createTimeline() {
        //given
        SocialStack timelineStack = new MemorySocialStack();
        Timeline aliceTimeline = new Timeline("I want this test to pass", "Alice",
            new Date(0), null);
        //when
        timelineStack = timelineStack.post("Alice", "I want this test to pass",
            new Date(0));
        //then
        assertEquals(aliceTimeline, timelineStack.getTimeline("Alice"));
    }

    @Test
    public void timeline_emptypost_areDiscarded(){
        //given
        SocialStack firstVersion = new MemorySocialStack();
        //when
        SocialStack newVersion = firstVersion.post("Alice", "", new Date());
        //then
        assertEquals(firstVersion, newVersion);

    }

    @Test
    public void timeline_severalSuccessivePost_createCorrectTimeline(){
        //given
        SocialStack timelineStack = new MemorySocialStack();
        Date startTime = new Date(0);
        Date aMinuteLater = new Date(60000L);
        Timeline aliceTimeline1 = new Timeline("I want this test to pass", "Alice",
            startTime, null);
        Timeline aliceTimeline2 = new Timeline("Does it?", "Alice", aMinuteLater,
            aliceTimeline1);
        //when
        timelineStack = timelineStack.post(aliceTimeline1.getUser(),
            aliceTimeline1.getMessage(), startTime);
        timelineStack = timelineStack.post(aliceTimeline2.getUser(),
            aliceTimeline2.getMessage(), aMinuteLater);
        //then
        assertEquals(aliceTimeline2, timelineStack.getTimeline("Alice"));
    }

    @Test
    public void wall_followJohn_addHimToWall(){
        //given
        String aliceName = "Alice";
        String johnName = "John";
        Date startTime = new Date(0);
        Date aMinuteLater = new Date(60000L);
        Timeline aliceTimeline = new Timeline("I want to be friends with you John",
            aliceName,  startTime, null);
        Timeline johnTimeline = new Timeline("I want to be friends with you too, Alice",
            johnName, aMinuteLater, null);
        User alice = new User(aliceName, new ArrayList<>(), aliceTimeline);
        User john = new User(johnName, new ArrayList<>(), johnTimeline);
        Map<String, User> users = new HashMap<>();
        users.put(aliceName, alice);
        users.put(johnName, john);
        SocialStack socialStack = new MemorySocialStack(users);
        //when
        socialStack = socialStack.follow(aliceName, johnName);
        //then
        assertEquals(socialStack.getWall(aliceName).getMessage(),
            johnTimeline.getMessage());
    }

    @Test
    public void socialStack_followUnknownUser_keepSameSocialStackInstance() {
        //given
        SocialStack beforeFollow = new MemorySocialStack();
        beforeFollow = beforeFollow.post("Alice", "I think I am alone here",
            new Date(0));
        //when
        SocialStack afterFollow = beforeFollow.follow("Alice", "Santa");
        //then
        assertEquals(beforeFollow, afterFollow);
    }

    @Test
    public void stack_buildWall_EqualsToSelfBuiltTimeline(){
        //given
        String aliceName = "Alice";
        String johnName = "John";
        String bobName = "Bob";
        Date date1 = new Date(0);
        Date date2 = new Date(60000L);
        Date date3 = new Date(120000L);
        Date date4 = new Date(180000L);
        Timeline timeline1 = new Timeline("Hello", aliceName, date1, null);
        Timeline timeline2 = new Timeline("Welcome", johnName, date2, null);
        Timeline timeline3 = new Timeline("There is a nice vibe here!", bobName,
            date3, null);
        Timeline timeline4 = new Timeline("Indeed, there is", aliceName, date4,
            timeline1);
        User john = new User(johnName, Collections.emptyList(), timeline2);
        User bob = new User(bobName, Collections.emptyList(), timeline3);
        List<User> following = new ArrayList<>();
        following.add(john);
        following.add(bob);
        User alice = new User(aliceName, following, timeline4);
        Map<String, User> users = new HashMap<>();
        users.put(aliceName, alice);
        users.put(johnName, john);
        users.put(bobName, bob);
        SocialStack socialStack = new MemorySocialStack(users);
        Timeline aliceWall = new Timeline(timeline4.getMessage(),
            timeline4.getUser(), timeline4.getMessageTimestamp(),
            new Timeline(timeline3.getMessage(), timeline3.getUser(),
                timeline3.getMessageTimestamp(),
                new Timeline(timeline2.getMessage(), timeline2.getUser(),
                    timeline2.getMessageTimestamp(),
                    new Timeline(timeline1.getMessage(),
                        timeline1.getUser(),
                        timeline1.getMessageTimestamp(),
                        null))));
        //when
        Timeline receivedWall = socialStack.getWall(aliceName);
        //then
        assertEquals(aliceWall, receivedWall);
    }

    @Test
    public void wall_forUnknownUser_returnNull(){
        //given
        SocialStack socialStack = new MemorySocialStack();
        //when
        Timeline wall = socialStack.getWall("Santa");
        //then
        assertNull(wall);
    }

    @Test
    public void stack_userExist_returnTrue(){
        //given
        Timeline aliceTimeline = new Timeline("I want this test to pass", "Alice",
            new Date(0), null);
        User alice = new User("Alice", new ArrayList<>(), aliceTimeline);
        SocialStack socialStack = new MemorySocialStack(
            Collections.singletonMap(alice.getName(), alice));
        //when
        boolean doesAliceExist = socialStack.userExist(alice.getName());
        //then
        assertTrue(doesAliceExist);
    }

    @Test
    public void testUserNotExist(){
        //given
        SocialStack socialStack = new MemorySocialStack();
        assertFalse(socialStack.userExist("Alice"));
    }
}
