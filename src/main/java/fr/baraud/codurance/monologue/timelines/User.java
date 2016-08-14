package fr.baraud.codurance.monologue.timelines;

import java.util.List;

/**
 * Created by animus on 13/08/16.
 */
public class User {

    private final String name;
    private final List<User> following;
    private final Timeline timeline;

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
}
