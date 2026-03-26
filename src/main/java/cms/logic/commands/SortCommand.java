package cms.logic.commands;

import static java.util.Objects.requireNonNull;

import cms.model.Model;

/**
 * Sorts all persons in the address book by the requested field.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String SORT_BY_TUTORIAL_GROUP = "tg";
    public static final String SORT_BY_NAME = "name";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all persons by the specified field.\n"
            + "Parameters: " + SORT_BY_TUTORIAL_GROUP + " | " + SORT_BY_NAME + "\n"
            + "Example: " + COMMAND_WORD + " " + SORT_BY_TUTORIAL_GROUP;
    public static final String MESSAGE_SUCCESS_TUTORIAL_GROUP = "Sorted all persons by tutorial group";
    public static final String MESSAGE_SUCCESS_NAME = "Sorted all persons by name";

    private final String sortBy;

    public SortCommand(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (SORT_BY_TUTORIAL_GROUP.equals(sortBy)) {
            model.sortPersonsByTutorialGroup();
            return new CommandResult(MESSAGE_SUCCESS_TUTORIAL_GROUP);
        }

        model.sortPersonsByName();
        return new CommandResult(MESSAGE_SUCCESS_NAME);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return sortBy.equals(otherSortCommand.sortBy);
    }
}
