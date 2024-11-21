package View;

import java.util.function.Supplier;

public class Command {

    // The actor this command belongs to. All commands also belong to manager.
    private Actors actor;

    // The text to display when this command displayed.
    private String command;

    // A functional interface for the execution logic.
    private Supplier<Boolean> executeLogic;

    public Command(Actors actor, String command, Supplier<Boolean> executeLogic) {
        this.actor = actor;
        this.command = command;
        this.executeLogic = executeLogic;
    }

    /**
     * The logic for when this command is executed.
     * @return Command performed
     */
    public boolean execute() {
        return executeLogic.get(); // Call the function provided
    }

    /**
     * The text to display when the command is called.
     * @return command
     */
    public String displayText() {
        return command;
    }

    public Actors getActor() {
        return actor;
    }

}
