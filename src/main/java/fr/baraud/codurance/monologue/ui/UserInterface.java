package fr.baraud.codurance.monologue.ui;

import fr.baraud.codurance.monologue.timelines.Timeline;

import java.util.Date;

/**
 * An interface to read input from users and send output to them.
 */
public interface UserInterface {

    /**
     * Returns the next valid instruction sent by an user
     * The method will block until an instruction is received.
     * Once called, the method iterate to (or wait for) the next instruction.
     *
     * @return an user Instruction
     * @see Instruction
     */
    Instruction getNextInstruction();

    /**
     * Send and display the timeline of an user
     * @param timeline to display
     * @param currentTime the current time, used to print the delay since the 
     * post was created
     */
    void writeTimeline(Timeline timeline, Date currentTime);

    /**
     * Send and display the wall of an user,
     * aggregation of multiple users' timelines
     * @param wall
     * @param currentTime the current time, used to print the delay since the 
     * post was created
     */
    void writeWall(Timeline wall, Date currentTime);

    /**
     * Print help for user
     */
    void writeHelp();
    
    /**
     * Print warning message when user does not exist
     */
    void writeWarningUnknownUser(String user);

    /**
     * Close the user interface
     * To call before leaving the app, to close any open connection
     * and to give a notice to the user of the closing of the app
     */
    void close();
}
