package cms.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import cms.logic.commands.exceptions.CommandException;
import cms.model.Model;
import cms.storage.Storage;

/**
 * Exports the current in-memory address book data to a user-specified JSON file path.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports current data to the given JSON file path.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " \"C:\\Users\\Josh\\Documents\\backup.json\"";

    public static final String MESSAGE_SUCCESS = "Exported current data to: %1$s";
    public static final String MESSAGE_EXPORT_ERROR_FORMAT =
            "Could not export data to file %s due to the following error: %s";
    public static final String MESSAGE_EXPORT_PERMISSION_ERROR_FORMAT =
            "Could not export data to file %s due to insufficient permissions to write to the file or the folder.";
    public static final String MESSAGE_STORAGE_CONTEXT_REQUIRED =
            "Export command requires storage context.";

    private final Path exportFilePath;

    public ExportCommand(Path exportFilePath) {
        this.exportFilePath = requireNonNull(exportFilePath);
    }

    public Path getExportFilePath() {
        return exportFilePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_STORAGE_CONTEXT_REQUIRED);
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        requireNonNull(storage);

        try {
            storage.saveAddressBook(model.getAddressBook(), exportFilePath);
            return new CommandResult(String.format(MESSAGE_SUCCESS, exportFilePath));
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_PERMISSION_ERROR_FORMAT, exportFilePath), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(MESSAGE_EXPORT_ERROR_FORMAT,
                    exportFilePath, ioe.getMessage()), ioe);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return exportFilePath.equals(otherExportCommand.exportFilePath);
    }

    @Override
    public int hashCode() {
        return exportFilePath.hashCode();
    }
}
