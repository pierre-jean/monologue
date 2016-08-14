package fr.baraud.codurance.monologue.timelines;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by animus on 14/08/16.
 */
public class TestUser {

    @Test
    public void testEqualUser(){
        Date now = new Date();
        User albert = new User("Albert", new ArrayList<>(), new Timeline("First message", "Albert", now, null));
        User usurper = new User("Albert", new ArrayList<>(), new Timeline("First message", "Albert", now, null));
        assertEquals(albert, usurper);
    }

    @Test
    public void testEqualItself(){
        User albert = new User("Albert", new ArrayList<>(), new Timeline("First message", "Albert", new Date(), null));
        assertEquals(albert, albert);
    }

    @Test
    public void testNotEqualNullUser(){
        User albert = new User("Albert", new ArrayList<>(), new Timeline("First message", "Albert", new Date(), null));
        assertNotEquals(albert, null);
    }

    @Test
    public void testNotEqualOtherObject(){
        User albert = new User("Albert", new ArrayList<>(), new Timeline("First message", "Albert", new Date(), null));
        assertNotEquals(albert, "I'm just a sentence");
    }

}
