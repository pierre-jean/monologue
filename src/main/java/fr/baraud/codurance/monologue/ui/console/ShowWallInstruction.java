package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * Created by animus on 19/08/16.
 */
public class ShowWallInstruction  implements Instruction{

    private final String user;

    public ShowWallInstruction(String user){
        this.user = user;

    }

    @Override
    public Action getAction() {
        return Action.SHOW_WALL;
    }

    @Override
    public SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate) {
        if (!socialStack.userExist(user)){
            ui.writeWarningUnknownUser(user);
            return socialStack;
        }
            ui.writeWall(socialStack.getWall(user), instructionDate);
            return socialStack;
    }
}
