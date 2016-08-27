package fr.baraud.codurance.monologue.ui;

import fr.baraud.codurance.monologue.timelines.SocialStack;

import java.util.Date;

/**
 * Instruction is the instance of a user interaction, that once applied will return the new version of the SocialStock
 * @see Action
 */
public interface Instruction {

    /**
     * @return the type of Action for this instrument
     * @see Action
     */
    Action getAction();

    /**
     * Apply this instruction to the provided socialStack and UI.
     * Depending on the action, it will print some messages to the user and/or return a new version of the SocialStack
     * @param socialStack the version of the SocialStack before application of the instruction
     * @param ui the user interface to display optional information to the user
     * @param instructionDate
     * @return the version of the SocialStack (immutable object) after application of the instruction
     */
    SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate);



}
