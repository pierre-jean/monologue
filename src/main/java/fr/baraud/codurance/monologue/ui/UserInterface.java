package fr.baraud.codurance.monologue.ui;

import fr.baraud.codurance.monologue.timelines.Timeline;

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
     * Send an information message to the user
     * @param information is a text message to inform the user
     */
    void writeInformation(String information);


    /**
     * Send and display the timeline of an user
     * @param timeline
     */
    void writeTimeline(Timeline timeline);

    /**
     * Send and display the wall of an user,
     * aggregation of multiple users' timelines
     * @param wall
     */
    void writeWall(Timeline wall);

    /**
     * Close the user interface
     * To call before leaving the app, to close any open connection
     * and to give a notice to the user of the closing of the app
     */
    void close();
}
