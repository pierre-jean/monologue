package fr.baraud.codurance.monologue.timelines;

import java.util.Date;
import java.util.Objects;

/**
 * A Timeline is an immutable representation of the post of the users.
 * A timeline is linked to the previous post via the getNext() method.
 * If a new post is created, a new timeline should be instanciated a this one
 * should be set as the next of the new instance.
 */
public class Timeline implements Comparable<Timeline>{
    private final String message;
    private final String user;
    private final Date messageTimestamp;
    private final Timeline next;

    /**
     * @param message the message of the post
     * @param user the author of the post
     * @param messageTimestamp the time the post was sent
     * @param next the previous message in the personal timeline of this user
     */
    public Timeline(String message, String user, Date messageTimestamp, Timeline next){
        this.message = message;
        this.user = user;
        this.messageTimestamp = messageTimestamp;
        this.next = next;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    public Timeline getNext() {
        return next;
    }

    /**
     * A timeline will be considered equal with another one only
     * if all posted messages, authors, and timestamps are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ( o == null){
            return false;
        }
        if (this.getClass() != o.getClass()){
            return false;
        }
        Timeline timeline = (Timeline) o;
        return Objects.equals(message, timeline.getMessage())
            && Objects.equals(user, timeline.getUser())
            && Objects.equals(messageTimestamp, timeline.getMessageTimestamp())
            && Objects.equals(next, timeline.getNext());
    }

    @Override
    public int hashCode() {
        return  Objects.hash(message, user, messageTimestamp);
    }

    /**
     * A timeline is ordered by its timestamp first (newer is bigger)
     * then the alphabetical order of the author, then the alphabetical order
     * of the message
     */
    @Override
    public int compareTo(Timeline timeline) {
        if (!messageTimestamp.equals(timeline.getMessageTimestamp())){
            return messageTimestamp.compareTo(timeline.getMessageTimestamp());
        }
        if (!user.equals(timeline.getUser())){
            return user.compareTo(timeline.getUser());
        }
        if (!message.equals(timeline.getMessage())){
            return message.compareTo(timeline.getMessage());
        }
        if (next != null && timeline.getNext() == null){
            return 1;
        }
        if (next == null && timeline.getNext() != null){
            return -1;
        }
        if (next == null && timeline.getNext() == null){
            return 0;
        }
        return next.compareTo(timeline.getNext());
    }
}
