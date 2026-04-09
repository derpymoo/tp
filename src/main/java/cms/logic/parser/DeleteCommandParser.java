package cms.logic.parser;

import static cms.logic.parser.CliSyntax.PREFIX_ID;
import static cms.logic.parser.CliSyntax.PREFIX_MATRIC;

import java.util.List;
import java.util.stream.Collectors;

import cms.commons.core.index.Index;
import cms.logic.Messages;
import cms.logic.commands.DeleteCommand;
import cms.logic.parser.exceptions.ParseException;
import cms.model.person.NusMatric;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            String trimmedArgs = args.trim();

            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs, PREFIX_ID, PREFIX_MATRIC);
            boolean hasIndexTargets = !argMultimap.getAllValues(PREFIX_ID).isEmpty();
            boolean hasNusMatricTargets = !argMultimap.getAllValues(PREFIX_MATRIC).isEmpty();

            if (trimmedArgs.isEmpty()
                    || !argMultimap.getPreamble().isEmpty()
                    || hasIndexTargets == hasNusMatricTargets) {
                throw new ParseException(DeleteCommand.MESSAGE_USAGE);
            }

            if (hasNusMatricTargets) {
                List<NusMatric> nusMatrics = ParserUtil.parseNusMatrics(parseNusMatricTokens(argMultimap));
                return DeleteCommand.byNusMatrics(nusMatrics);
            }

            List<Index> indexes = ParserUtil.parseIndexes(String.join(" ", argMultimap.getAllValues(PREFIX_ID)));
            return new DeleteCommand(indexes);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

    private List<String> parseNusMatricTokens(ArgumentMultimap argMultimap) throws ParseException {
        List<String> nusMatricValues = argMultimap.getAllValues(PREFIX_MATRIC);
        if (nusMatricValues.isEmpty()) {
            throw new ParseException(NusMatric.MESSAGE_FORMAT_CONSTRAINTS);
        }

        return nusMatricValues.stream()
                .flatMap(value -> List.of(value.trim().split("\\s+")).stream())
                .filter(token -> !token.isBlank())
                .collect(Collectors.toList());
    }

}
