package fr.baraud.codurance.monologue.timelines;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class TestTimeline {

    @Test
    public void timelinesWithSameAttributes_areEqual(){
        //given
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            new Date(0), null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            new Date(0), null);
        //then
        assertEquals(timeline1, timeline2);
    }

    @Test
    public void timelinesWithFromDifferentUsers_areNotEqual(){
        //given
        Timeline timeline1 = new Timeline("this is the same as the other", "other",
            new Date(0), null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            new Date(0), null);
        //then
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void timelinesWithDiffentMessages_areNotEqual(){
        //given
        Timeline timeline1 = new Timeline("this is not the same as the other", "me",
            new Date(0), null);
        Timeline timeline2 = new Timeline("this is different", "me", new Date(0),
            null);
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void timelinesWithDifferentTimestamps_areNotEqual(){
        //given
        Date firstTime = new Date(0);
        Date aMinuteLater = new Date(60000L);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            firstTime, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            aMinuteLater, null);
        //then
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void timelinesWithDifferentHistory_areNotEqual(){
        //given
        Timeline next1 = new Timeline("hey", "friend", new Date(0), null);
        Timeline next2 = new Timeline("ho", "me", new Date(0), null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            new Date(0), next1);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            new Date(0), next2);
        //then
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void timelineEqualsItself(){
        //given
        Timeline timeline = new Timeline("this is the same as the other", "me",
            new Date(0), null);
        //then
        assertEquals(timeline, timeline);
    }


    @Test
    public void timelineDoesNotEqualsOtherThings(){
        //given
        Timeline timeline = new Timeline("this is the same as the other", "me",
            new Date(0), null);
        //then
        assertNotEquals(timeline, "this is the same as the other");
    }

    @Test
    public void timelineDoesNotEqualNullButIsNPESafe(){
        //given
        Timeline timeline = new Timeline("this is the same as the other", "me",
            new Date(0), null);
        //then
        assertNotEquals(timeline, null);
    }

    @Test
    public void timeline_similarTopMessage_haveSameHashCode(){
        //given
        Timeline next1 = new Timeline("hey", "friend", new Date(0), null);
        Timeline next2 = new Timeline("ho", "me", new Date(0), null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            new Date(0), next1);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            new Date(0), next2);
        //then
        assertEquals(timeline1.hashCode(), timeline2.hashCode());
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void compareToNullRaisesNPE() {
        //given
        Timeline timeline = new Timeline("this is the same as the other", "me", new Date(), null);
        //then
        exception.expect(NullPointerException.class);
        //when
        timeline.compareTo(null);
    }

    @Test
    public void timeline_WithOlderTopMessageTimestamp_isInferior() {
        //given
        Date before = new Date(0);
        Date fifteenSecLater = new Date(15000L);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            before, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            fifteenSecLater, null);
        //then
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void timeline_withSameTimestamp_areClassifiedByUserName() {
        //given
        Timeline timeline1 = new Timeline("this is the same as the other", "abc",
            new Date(0), null);
        Timeline timeline2 = new Timeline("this is the same as the other", "efg",
            new Date(0), null);
        //then
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void timeline_withSameTimestampAndUser_areClassifiedByMessageContent() {
        //given
        Date now = new Date(0);
        Timeline timeline1 = new Timeline("abc", "me", now, null);
        Timeline timeline2 = new Timeline("efg", "me", now, null);
        //then
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void timeline_withSameTopMessage_areClassifiedByHistory() {
        //given
        Date now = new Date(0);
        Timeline next1 = new Timeline("abc", "me", now, null);
        Timeline next2 = new Timeline("efg", "me", now, null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            now, next1);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            now, next2);
        //then
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void timelineWithSimilarHistoryUserDateMessage_areEqual() {
        //given
        Date now = new Date(0);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            now, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            now, null);
        //then
        assertTrue(timeline1.compareTo(timeline2) == 0);
    }

    @Test
    public void timelineWithSameMessageButShorterHistory_isInferior() {
        //given
        Date now = new Date(0);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            now, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            now, timeline1);
        //then
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void timelineWithSameMessageButLongerHistory_isBigger() {
        //given
        Date now = new Date();
        Timeline timeline2 = new Timeline("this is the same as the other", "me",
            now, null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me",
            now, timeline2);
        //then
        assertTrue(timeline1.compareTo(timeline2) > 0);
    }

}
