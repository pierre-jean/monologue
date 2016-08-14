package fr.baraud.codurance.monologue.timelines;

import java.util.Date;
import java.util.Objects;

/**
 * Created by animus on 13/08/16.
 */
public class Timeline implements Comparable<Timeline>{
    private final String message;
    private final String user;
    private final Date messageTimestamp;
    private final Timeline next;

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
        return (Objects.equals(message, timeline.getMessage())
                && Objects.equals(user, timeline.getUser())
                && Objects.equals(messageTimestamp, timeline.getMessageTimestamp())
                && Objects.equals(next, timeline.getNext()));
    }

    @Override
    public int hashCode() {
        return  Objects.hash(message, user, messageTimestamp);
    }

    public int compareTo(Timeline timeline) {
        if (timeline == null ){
            throw new NullPointerException();
        }
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
