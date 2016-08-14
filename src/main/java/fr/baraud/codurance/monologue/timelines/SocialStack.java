package fr.baraud.codurance.monologue.timelines;

import java.util.Date;

/**
 * Created by animus on 14/08/16.
 */
public interface SocialStack {

    SocialStack post(String user, String message, Date messageTimestamp);

    SocialStack follow(String user, String following);

    Timeline getTimeline(String user);

    Timeline getWall(String user);

}
