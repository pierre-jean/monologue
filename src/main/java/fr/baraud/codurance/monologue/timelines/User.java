package fr.baraud.codurance.monologue.timelines;

import java.util.List;
import java.util.Objects;

/**
 * A User is a representation of a Monologue user once he/she published. 
 * It's immutable so when the timeline changes a new instance should be created.
 *
 */
public class User {

    private final String name;
    private final List<User> following;
    private final Timeline timeline;

    /**
     * @param name the name of the user. Should not contains any space.
     * @param following the list of users he/she follows. Should not be null.
     * @param timeline the timeline of the user. Should not be null
     * @see Timeline
     */
    public User(String name, List<User> following, Timeline timeline){
        this.name = name;
        this.following =  following;
        this.timeline =  timeline;
    }

    public String getName() {
        return name;
    }

    public List<User> getFollowing() {
        return following;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * A user with the same name, following the same person, and having a
     * similar timeline will be considered equal with this one.
     */
    @Override
    public boolean equals(Object o) {
        if ( o == this){
            return true;
        }
        if (o == null){
            return false;
        }
        if (this.getClass() != o.getClass()){
            return false;
        }
        User user = (User)o;
        return name.equals(user.getName())
            && following.equals(user.getFollowing())
            && timeline.equals(user.getTimeline());
    }

    @Override
    public int hashCode() {
        return  Objects.hash(name, following);
    }
}
