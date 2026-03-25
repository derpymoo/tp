package cms.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;

import cms.model.Model;

/**
 * Imports address book data from a user-specified JSON file path into the application.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports address book data from a JSON file.\n"
            + "Parameters: FILE_PATH (must be a .json file)\n"
            + "Example: " + COMMAND_WORD + " data/addressbook.json";

    public static final String MESSAGE_SUCCESS = "Address book imported from: %s";

    private final Path importFilePath;

    /**
     * Creates an ImportCommand to import address book data from the specified file path.
     *
     * @param importFilePath the path to the JSON file to import from
     */
    public ImportCommand(Path importFilePath) {
        requireNonNull(importFilePath);
        this.importFilePath = importFilePath;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(String.format(MESSAGE_SUCCESS, importFilePath));
    }

    public Path getImportFilePath() {
        return importFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherImportCommand = (ImportCommand) other;
        return importFilePath.equals(otherImportCommand.importFilePath);
    }

    @Override
    public int hashCode() {
        return importFilePath.hashCode();
    }
}
