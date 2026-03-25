package cms.logic.parser;

import static cms.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import cms.logic.commands.ImportCommand;
import cms.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    public static final String MESSAGE_INVALID_FILE_PATH = "File path is invalid: %1$s\n"
        + "Format: " + ImportCommand.MESSAGE_USAGE;
    public static final String MESSAGE_FILE_EXTENSION_REQUIRED = "File path must end with .json\n"
        + "Format: " + ImportCommand.MESSAGE_USAGE;

    @Override
    public ImportCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        String pathString = removeMatchingSurroundingQuotes(trimmedArgs);
        if (pathString == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        final Path importFilePath;
        try {
            importFilePath = Path.of(pathString);
        } catch (InvalidPathException ipe) {
            throw new ParseException(String.format(MESSAGE_INVALID_FILE_PATH, ipe.getReason()));
        }

        if (!importFilePath.toString().toLowerCase().endsWith(".json")) {
            throw new ParseException(MESSAGE_FILE_EXTENSION_REQUIRED);
        }

        return new ImportCommand(importFilePath);
    }

    private String removeMatchingSurroundingQuotes(String input) {
        boolean startsWithQuote = input.startsWith("\"");
        boolean endsWithQuote = input.endsWith("\"");

        if (startsWithQuote && endsWithQuote && input.length() > 1) {
            return input.substring(1, input.length() - 1);
        }

        if (startsWithQuote != endsWithQuote) {
            return null;
        }

        return input;
    }
}

