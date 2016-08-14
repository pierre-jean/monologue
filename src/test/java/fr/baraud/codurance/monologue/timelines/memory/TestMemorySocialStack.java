package fr.baraud.codurance.monologue.timelines.memory;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.timelines.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by animus on 14/08/16.
 */
public class TestMemorySocialStack {

    @Test
    public void testTimeline(){
        Date timestamp = new Date();
        Timeline aliceTimeline = new Timeline("I want this test to pass", "Alice", timestamp, null);
        User alice = new User("Alice", new ArrayList<>(), aliceTimeline);
        Map<String, User> users = new HashMap<>();
        users.put(alice.getName(), alice);
        SocialStack socialStack = new MemorySocialStack(users);
        assertEquals(aliceTimeline, socialStack.getTimeline(alice.getName()));
    }

    @Test
    public void testPost() {
        SocialStack timelineStack = new MemorySocialStack(new HashMap<>());
        Date timestamp = new Date();
        Timeline aliceTimeline = new Timeline("I want this test to pass", "Alice", timestamp, null);
        timelineStack = timelineStack.post("Alice", "I want this test to pass", timestamp);
        assertEquals(aliceTimeline, timelineStack.getTimeline("Alice"));
    }

    @Test
    public void testEmptyPost(){
        SocialStack firstVersion = new MemorySocialStack(new HashMap<>());
        SocialStack newVersion = firstVersion.post("Alice", "", new Date());
        assertEquals(firstVersion, newVersion);

    }

    @Test
    public void testSeveralPost(){
        SocialStack timelineStack = new MemorySocialStack(new HashMap<>());
        Date now = new Date(1471176872);
        Date later = new Date(1471188888);
        Timeline aliceTimeline1 = new Timeline("I want this test to pass", "Alice", now, null);
        Timeline aliceTimeline2 = new Timeline("Does it?", "Alice", later, aliceTimeline1);
        timelineStack = timelineStack.post(aliceTimeline1.getUser(), aliceTimeline1.getMessage(), now);
        timelineStack = timelineStack.post(aliceTimeline2.getUser(), aliceTimeline2.getMessage(), later);
        assertEquals(aliceTimeline2, timelineStack.getTimeline("Alice"));
    }

    @Test
    public void testFollow(){
        String aliceName = "Alice";
        String johnName = "John";
        Date now = new Date(1471176872);
        Date later = new Date(1471188888);
        Timeline aliceTimeline = new Timeline("I want to be friends with you John", aliceName,  now, null);
        Timeline johnTimeline = new Timeline("I want to be friends with you too, Alice", johnName, later, null);
        User alice = new User(aliceName, new ArrayList<>(), aliceTimeline);
        User john = new User(johnName, new ArrayList<>(), johnTimeline);
        Map<String, User> users = new HashMap<>();
        users.put(aliceName, alice);
        users.put(johnName, john);
        SocialStack socialStack = new MemorySocialStack(users);
        socialStack = socialStack.follow(aliceName, johnName);
        assertEquals(socialStack.getWall(aliceName).getMessage(), johnTimeline.getMessage());
    }

    @Test
    public void testFollowUnknownUser() {
        SocialStack beforeFollow = new MemorySocialStack(new HashMap<>());
        beforeFollow = beforeFollow.post("Alice", "I think I am alone here", new Date());
        SocialStack afterFollow = beforeFollow.follow("Alice", "Santa");
        assertEquals(beforeFollow, afterFollow);
    }

    @Test
    public void testWall(){
        String aliceName = "Alice";
        String johnName = "John";
        String bobName = "Bob";
        Date date1 = new Date(1471144444);
        Date date2 = new Date(1471155555);
        Date date3 = new Date(1471166666);
        Date date4 = new Date(1471177777);
        Timeline timeline1 = new Timeline("Hello", aliceName, date1, null);
        Timeline timeline2 = new Timeline("Welcome", johnName, date2, null);
        Timeline timeline3 = new Timeline("There is a nice vibe here!", bobName, date3, null);
        Timeline timeline4 = new Timeline("Indeed, there is", aliceName, date4, timeline1);
        User john = new User(johnName, new ArrayList<>(), timeline2);
        User bob = new User(bobName, new ArrayList<>(), timeline3);
        List<User> following = new ArrayList<>();
        following.add(john);
        following.add(bob);
        User alice = new User(aliceName, following, timeline4);
        Map<String, User> users = new HashMap<>();
        users.put(aliceName, alice);
        users.put(johnName, john);
        users.put(bobName, bob);
        SocialStack socialStack = new MemorySocialStack(users);
        Timeline aliceWall = new Timeline(timeline4.getMessage(), timeline4.getUser(), timeline4.getMessageTimestamp(),
                new Timeline(timeline3.getMessage(), timeline3.getUser(), timeline3.getMessageTimestamp(),
                        new Timeline(timeline2.getMessage(), timeline2.getUser(), timeline2.getMessageTimestamp(),
                                new Timeline(timeline1.getMessage(), timeline1.getUser(), timeline1.getMessageTimestamp(),
                                        null))));
        assertEquals(aliceWall, socialStack.getWall(aliceName));
    }

    @Test
    public void testWallUnknownUser(){
        SocialStack socialStack = new MemorySocialStack(new HashMap<>());
        assertNull(socialStack.getWall("Santa"));
    }
}
