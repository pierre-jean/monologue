package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * Exit Instruction breaks the listen loop of the program and allows it to quit
 */
public class ExitInstruction implements Instruction{

    @Override
    public Action getAction() {
        return Action.EXIT;
    }

    @Override
    public SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate) {
        return null;
    }
}
