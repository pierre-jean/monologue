package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * Created by animus on 19/08/16.
 */
public class HelpInstruction implements Instruction {
    @Override
    public Action getAction() {
        return Action.HELP;
    }

    @Override
    public SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate) {
        ui.writeHelp();
        return socialStack;
    }
}
