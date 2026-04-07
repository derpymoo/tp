package cms.logic.commands;

import cms.logic.commands.exceptions.CommandException;
import cms.model.Model;
import cms.storage.Storage;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Executes the command using model and storage.
     *
     * <p>Commands that do not require direct storage access can rely on this default behavior,
     * which delegates to {@link #execute(Model)}.</p>
     */
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        return execute(model);
    }

}
