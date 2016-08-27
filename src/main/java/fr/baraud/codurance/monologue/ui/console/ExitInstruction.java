package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * Created by animus on 22/08/16.
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
