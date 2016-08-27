package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * Created by animus on 19/08/16.
 */
public class FollowInstruction implements Instruction {

    private final String follower;
    private final String following;

    public FollowInstruction(String follower, String following){
        this.follower = follower;
        this.following = following;
    }

    @Override
    public Action getAction() {
        return Action.FOLLOW;
    }

    @Override
    public SocialStack apply(SocialStack socialStack, UserInterface ui, Date instructionDate) {
        if (!socialStack.userExist(follower)){
            ui.writeWarningUnknownUser(follower);
            return socialStack;
        } else if (!socialStack.userExist(following)){
            ui.writeWarningUnknownUser(following);
            return socialStack;
        } else{
            return socialStack.follow(follower, following);
        }
    }

}
