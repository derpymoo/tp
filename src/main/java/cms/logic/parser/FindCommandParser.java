package cms.logic.parser;

import static cms.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cms.logic.parser.CliSyntax.PREFIX_NAME;
import static cms.logic.parser.CliSyntax.PREFIX_NUSID;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cms.logic.commands.FindCommand;
import cms.logic.parser.exceptions.ParseException;
import cms.model.person.NameContainsKeywordsPredicate;
import cms.model.person.NusIdContainsKeywordsPredicate;
import cms.model.person.NameOrNusIdContainsKeywordsPredicate;
import cms.model.person.NusId;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Tokenize for optional prefixes
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NUSID);

        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasNusId = argMultimap.getValue(PREFIX_NUSID).isPresent();

        if (hasName && hasNusId) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (hasName) {
            String nameArg = argMultimap.getValue(PREFIX_NAME).get().trim();
            if (nameArg.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            List<String> nameKeywords = Arrays.stream(nameArg.split("\\s+"))
                    .collect(Collectors.toList());
            return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
        }

        if (hasNusId) {
            String idArg = argMultimap.getValue(PREFIX_NUSID).get().trim();
            if (idArg.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            List<String> idKeywords = Arrays.stream(idArg.split("\\s+"))
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            return new FindCommand(new NusIdContainsKeywordsPredicate(idKeywords));
        }

        // New behavior: no prefixes -> split tokens and classify each as name or NUS ID
        List<String> tokens = Arrays.stream(trimmedArgs.split("\\s+"))
                .collect(Collectors.toList());

        List<String> nameKeywords = tokens.stream()
                .filter(token -> !NusId.isValidNusId(token))
                .collect(Collectors.toList());

        List<String> idKeywords = tokens.stream()
                .filter(token -> NusId.isValidNusId(token))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        return new FindCommand(new NameOrNusIdContainsKeywordsPredicate(nameKeywords, idKeywords));
    }

}
