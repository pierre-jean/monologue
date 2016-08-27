package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * ShowTimelineInstruction triggers the display of the timeline to the user interface
 */
public class ShowTimelineInstruction implements Instruction {

    private final String user;

    public ShowTimelineInstruction(String user){
        this.user = user;
    }


    @Override
    public Action getAction() {
        return Action.SHOW_TIMELINE;
    }

    @Override
    public SocialStack apply(SocialStack socialStack, UserInterface ui, Date date) {
        if (!socialStack.userExist(user)){
            ui.writeWarningUnknownUser(user);
        } else {
            ui.writeTimeline(socialStack.getTimeline(user), date);
        }
        return socialStack;
    }
}
