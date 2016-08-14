package fr.baraud.codurance.monologue.timelines.memory;

import fr.baraud.codurance.monologue.timelines.Timeline;
import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.timelines.User;

import java.util.*;

/**
 * Created by animus on 13/08/16.
 */
public class MemorySocialStack implements SocialStack {

    private final Map<String, User> users;

    public MemorySocialStack(Map<String, User> users){
        this.users = users;
    }

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

    public Timeline getTimeline(String username){
        User user = users.get(username);
        return user != null? user.getTimeline() : null;
    }

    public Timeline getWall(String username){
        User user = users.get(username);
        if (user == null){
            return null;
        }
        TreeSet<Timeline> allTimeline = new TreeSet<Timeline>();
        allTimeline.add(user.getTimeline());
        for (User following : user.getFollowing()){
            allTimeline.add(following.getTimeline());
        }
        return buildWall(allTimeline);
    }

    public SocialStack follow(String username, String following){
        User user = users.get(username);
        User toFollow = users.get(following);
        if (user != null && toFollow != null) {
            List<User> newFollowing = new ArrayList<User>();
            newFollowing.addAll(user.getFollowing());
            newFollowing.add(toFollow);
            Map<String, User> newUsers = new HashMap<>();
            newUsers.putAll(users);
            newUsers.put(username, new User(username, newFollowing, user.getTimeline()));
            return new MemorySocialStack(newUsers);
        }
        return this;
    }

    private Timeline buildWall(TreeSet<Timeline> followingAndPersonal){
        if (followingAndPersonal.isEmpty()){
            return null;
        }
        Timeline last = followingAndPersonal.last();
        TreeSet<Timeline> remainingFollowingAndPersonal =  new TreeSet<Timeline>();
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
