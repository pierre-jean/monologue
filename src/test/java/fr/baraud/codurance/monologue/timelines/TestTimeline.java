package fr.baraud.codurance.monologue.timelines;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by animus on 14/08/16.
 */
public class TestTimeline {

    @Test
    public void testSimpleTimelineEquality(){
        Date timestamp = new Date();
        Timeline timeline1 = new Timeline("this is the same as the other", "me", timestamp, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", timestamp, null);
        assertEquals(timeline1, timeline2);
    }

    @Test
    public void TestTimelineUserInequality(){
        Date timestamp = new Date();
        Timeline timeline1 = new Timeline("this is the same as the other", "other", timestamp, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", timestamp, null);
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void TestTimelineMessageInequality(){
        Date timestamp = new Date();
        Timeline timeline1 = new Timeline("this is not the same as the other", "me", timestamp, null);
        Timeline timeline2 = new Timeline("this is different", "me", timestamp, null);
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void TestTimelineTimestampInequality(){
        Date timestamp1 = new Date(1471170000);
        Date timestamp2 = new Date(1471179999);
        Timeline timeline1 = new Timeline("this is the same as the other", "me", timestamp1, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", timestamp2, null);
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void TestTimelineNextInequality(){
        Date timestamp = new Date();
        Timeline next1 = new Timeline("hey", "friend", timestamp, null);
        Timeline next2 = new Timeline("ho", "me", timestamp, null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me", timestamp, next1);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", timestamp, next2);
        assertNotEquals(timeline1, timeline2);
    }

    @Test
    public void TestTimelineItselfEquality(){
        Timeline timeline = new Timeline("this is the same as the other", "me", new Date(), null);
        assertEquals(timeline, timeline);
    }


    @Test
    public void TestTimelineOtherObjectInequality(){
        Timeline timeline = new Timeline("this is the same as the other", "me", new Date(), null);
        assertNotEquals(timeline, "this is the same as the other");
    }

    @Test
    public void TestTimelineNullInequality(){
        Timeline timeline = new Timeline("this is the same as the other", "me", new Date(), null);
        assertNotEquals(timeline, null);
    }

    @Test
    public void TestTimelineHashCode(){
        Date timestamp = new Date();
        Timeline next1 = new Timeline("hey", "friend", timestamp, null);
        Timeline next2 = new Timeline("ho", "me", timestamp, null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me", timestamp, next1);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", timestamp, next2);
        assertEquals(timeline1.hashCode(), timeline2.hashCode());
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void TestCompareNullPointerException() {
        Timeline timeline = new Timeline("this is the same as the other", "me", new Date(), null);
        exception.expect(NullPointerException.class);
        timeline.compareTo(null);
    }

    @Test
    public void TestCompareDifferentTimestamp() {
        Date now = new Date(1471176872);
        Date later = new Date(1471188888);
        Timeline timeline1 = new Timeline("this is the same as the other", "me", now, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", later, null);
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void TestCompareDifferentUser() {
        Date now = new Date();
        Timeline timeline1 = new Timeline("this is the same as the other", "abc", now, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "efg", now, null);
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void TestCompareDifferentMessage() {
        Date now = new Date();
        Timeline timeline1 = new Timeline("abc", "me", now, null);
        Timeline timeline2 = new Timeline("efg", "me", now, null);
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void TestCompareDifferentNextTimeline() {
        Date now = new Date();
        Timeline next1 = new Timeline("abc", "me", now, null);
        Timeline next2 = new Timeline("efg", "me", now, null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me", now, next1);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", now, next2);
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void TestCompareSameTimeline() {
        Date now = new Date();
        Timeline timeline1 = new Timeline("this is the same as the other", "me", now, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", now, null);
        assertTrue(timeline1.compareTo(timeline2) == 0);
    }

    @Test
    public void TestCompareShorterTimeline() {
        Date now = new Date();
        Timeline timeline1 = new Timeline("this is the same as the other", "me", now, null);
        Timeline timeline2 = new Timeline("this is the same as the other", "me", now, timeline1);
        assertTrue(timeline1.compareTo(timeline2) < 0);
    }

    @Test
    public void TestCompareLongerTimeline() {
        Date now = new Date();
        Timeline timeline2 = new Timeline("this is the same as the other", "me", now, null);
        Timeline timeline1 = new Timeline("this is the same as the other", "me", now, timeline2);
        assertTrue(timeline1.compareTo(timeline2) > 0);
    }

}
