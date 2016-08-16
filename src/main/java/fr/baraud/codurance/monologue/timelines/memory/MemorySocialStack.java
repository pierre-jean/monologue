package fr.baraud.codurance.monologue.timelines.memory;

import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.User;

import java.util.*;

/**
 * MemorySocialStack is a representation of a state of the history of Monologue
 * App. It contains all the users and their timelines at this state.
 * It does not store this info in a database so once it's no longer referenced
 * it is removed from the memory and the state is lost.
 * It is immutable, so if the state change, a new instance should be created.
 */
public class MemorySocialStack implements SocialStack {

    /**
     * A list of users indexed by their name.
     * @see fr.baraud.codurance.monologue.timelines.User
     */
    private final Map<String, User> users;

    /**
     * An existing list of users, that can be the result of a previous state
     * @param users existing users, can be an empty list but should not be null
     */
    public MemorySocialStack(Map<String, User> users){
        this.users = users;
    }

    /**
     * @see fr.baraud.codurance.monologue.timelines.SocialStack#post(String, String, Date)
     */
    @Override
    public SocialStack post(String username, String message, Date messageTimestamp){
        if (message == null || message.isEmpty()){
            return this;
        }
        User user = users.get(username);
        List<User> following = user != null ? user.getFollowing() : new ArrayList<>();
        Timeline timeline = user != null ?
                new Timeline(message, username, messageTimestamp, user.getTimeline()) :
                new Timeline(message, username, messageTimestamp, null);
        Map<String, User> newUsers = new HashMap<>();
        newUsers.putAll(users);
        newUsers.put(username, new User(username, following, timeline));
        return new MemorySocialStack(newUsers);
    }

    /**
     * @see fr.baraud.codurance.monologue.timelines.SocialStack#getTimeline(String)
     */
    @Override
    public Timeline getTimeline(String username){
        User user = users.get(username);
        return user != null? user.getTimeline() : null;
    }

    /**
     * @see fr.baraud.codurance.monologue.timelines.SocialStack#getWall(String)
     */
    @Override
    public Timeline getWall(String username){
        User user = users.get(username);
        if (user == null){
            return null;
        }
        TreeSet<Timeline> allTimeline = new TreeSet<>();
        allTimeline.add(user.getTimeline());
        for (User following : user.getFollowing()){
            allTimeline.add(following.getTimeline());
        }
        return buildWall(allTimeline);
    }

    /**
     * @see fr.baraud.codurance.monologue.timelines.SocialStack#follow(String, String)
     */
    public SocialStack follow(String username, String following){
        User user = users.get(username);
        User toFollow = users.get(following);
        if (user != null && toFollow != null) {
            List<User> newFollowing = new ArrayList<>();
            newFollowing.addAll(user.getFollowing());
            newFollowing.add(toFollow);
            Map<String, User> newUsers = new HashMap<>();
            newUsers.putAll(users);
            newUsers.put(username, new User(username, newFollowing, user.getTimeline()));
            return new MemorySocialStack(newUsers);
        }
        return this;
    }

    /**
     * Create the aggregation of posts ordered by their timestamp into one timeline
     * @param followingAndPersonal the list of timeline to aggregate into one wall
     * @return a wall as a timeline of one or several users posts
     */
    private Timeline buildWall(TreeSet<Timeline> followingAndPersonal){
        if (followingAndPersonal.isEmpty()){
            return null;
        }
        Timeline last = followingAndPersonal.last();
        TreeSet<Timeline> remainingFollowingAndPersonal =  new TreeSet<>();
        for (Timeline timeline : followingAndPersonal) {
            if (timeline == last && timeline.getNext() != null ) {
                remainingFollowingAndPersonal.add(timeline.getNext());
            }
            if ( timeline!= last && timeline != null) {
                remainingFollowingAndPersonal.add(timeline);
            }
        }
        return new Timeline(last.getMessage(), last.getUser(), last.getMessageTimestamp(), buildWall(remainingFollowingAndPersonal));
    }
}
