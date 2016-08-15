package fr.baraud.codurance.monologue.ui;

/**
 * An instruction is an user action and its associated parameters
 * It encapsulate all information sent by the user to interact with the 
 * application
 */
public class Instruction {

    private final Action action;
    private final String user;
    private final String content;

    /**
     *
     * @param action the type of action of this instruction. Mandatory.
     * @param user the user is a mandatory parameter for actions 
     * SHOW_TIMELINE, SHOW_WALL, POST, AND FOLLOWS. 
     * For FOLLOW, the user represents the subject that wants to follow another 
     * user. 
     * User can be null in other cases.
     * @param content the content of the instruction. For action POST, it 
     * represents the text to post. For action FOLLOW, it represents the user 
     * that is followed. Null otherwise.
     */
    public Instruction(Action action, String user, String content){
        this.action = action;
        this.user = user;
        this.content = content;
    }

    /**
     * Returns the type of the instruction
     * @return the action of the instruction
     * @see Action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Returns the user for this action
     * For Action SHOW_TIMELINE, SHOW_WALL, represents the user targeted by 
     * the action.
     * For Action POST represents the author of the post.
     * For Action FOLLOW represents the author that wants to follow another user.
     * @return the User for this action
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns an optional content, or payload, associated with the action.
     * For Action POST, represents the text to be posted to the timeline.
     * For Action FOLLOW, represents the name of the user to follow.
     * Is null otherwise
     * @return the content of the instruction
     */
    public String getContent() {
        return content;
    }
}
