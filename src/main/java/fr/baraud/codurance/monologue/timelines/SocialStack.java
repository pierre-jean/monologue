package fr.baraud.codurance.monologue.timelines;

import java.util.Date;

/**
 * A SocialStack is a database of all the users and their timeline.
 * It is immutable so if a new post is created or a user gets a new follower,
 * a new instance is returned
 *  
 *@see Timeline
 *@see User
 */
public interface SocialStack {

    /**
     * Add a new post to the user's timeline (and create the user if it does not
     * exist). It does not affect the current instance of the SocialStack, only
     * the new instance returned.
     * 
     * @param user the author of the message. Should not contains space.
     * @param message the content of the message
     * @param messageTimestamp the date of the message creation
     * @return a new instance of the SocialStack representing the new state with
     * the added post
     */
    SocialStack post(String user, String message, Date messageTimestamp);

    /**
     * Add a new user to the following list of this user. It does not affect 
     * the current instance of the SocialStack, only the new instance returned.
     * @param user the user that wants to follow
     * @param following the user to follow
     * @return a new instance of the SocialStack with the updated following list
     * for this user. If the request user or the user to follow don't exist,
     * returns the same instance
     */
    SocialStack follow(String user, String following);

    /**
     * Get the personal timeline for the user
     * @param user owner of the personal timeline
     * @return the timeline if the user exist, null otherwise
     */
    Timeline getTimeline(String user);

    /**
     * Get the user's wall. A wall is an aggregation of the personal timeline
     * of the user and the personal timeline of the users he follows
     * @param user
     * @return the wall if the user exist, null otherwise
     */
    Timeline getWall(String user);

}
