package fr.baraud.codurance.monologue;

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
     * @see fr.baraud.codurance.monologue.Instruction
     */
    Instruction getNextInstruction();

    /**
     * Send and display to the user output interface
     * @param answer the text to display to the user
     */
    void writeAnswer(String answer);

    /**
     * Close the user interface
     * To call before leaving the app, to close any open connection
     * and to give a notice to the user of the closing of the app
     */
    void close();
}
