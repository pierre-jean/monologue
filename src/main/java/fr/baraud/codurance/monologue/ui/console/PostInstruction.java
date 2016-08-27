package fr.baraud.codurance.monologue.ui.console;

import fr.baraud.codurance.monologue.timelines.SocialStack;
import fr.baraud.codurance.monologue.ui.Action;
import fr.baraud.codurance.monologue.ui.Instruction;
import fr.baraud.codurance.monologue.ui.UserInterface;

import java.util.Date;

/**
 * PostInstruction build a new SocialStack with an updated timeline for the user that posted the message
 */
public class PostInstruction implements Instruction {

    private final String user;
    private final String content;


    public PostInstruction(String user, String content){
        this.user = user;
        this.content = content;
    }

    @Override
    public SocialStack apply(SocialStack socialStack, UserInterface ui, Date date){
        return socialStack.post(user, content, date);
    }

    @Override
    public Action getAction(){
        return Action.POST;
    }



}
