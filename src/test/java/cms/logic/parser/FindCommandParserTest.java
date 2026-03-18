package cms.logic.parser;

import static cms.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cms.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cms.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import cms.logic.commands.FindCommand;
import cms.model.person.NameContainsKeywordsPredicate;
import cms.model.person.NusIdContainsKeywordsPredicate;
import cms.model.person.NameOrNusIdContainsKeywordsPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameOrNusIdContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"), java.util.Collections.emptyList()));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_namePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);
    }

    @Test
    public void parse_idPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NusIdContainsKeywordsPredicate(Collections.singletonList("A0234504F")));
        assertParseSuccess(parser, " id/A0234504F", expectedFindCommand);

        // lowercase id should also be accepted (case-insensitive)
        FindCommand expectedFindCommandLower =
                new FindCommand(new NusIdContainsKeywordsPredicate(Collections.singletonList("A0234504F")));
        assertParseSuccess(parser, " id/a0234504f", expectedFindCommandLower);
    }

    @Test
    public void parse_id_multipleIds() {
        FindCommand expectedFindCommand =
                new FindCommand(new NusIdContainsKeywordsPredicate(Arrays.asList("A0234502D", "A0234505G")));
        assertParseSuccess(parser, " id/A0234502D A0234505G", expectedFindCommand);
    }

    @Test
    public void parse_bothPrefixes_throwsParseException() {
        assertParseFailure(parser, " n/Alice id/A0234504F",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyNamePrefix_throwsParseException() {
        assertParseFailure(parser, " n/   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyIdPrefix_throwsParseException() {
        assertParseFailure(parser, " id/  ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unprefixedMixedTokens_classifiesCorrectly() {
        // unprefixed tokens: one name token and one NUS ID token
        FindCommand expectedFindCommand = new FindCommand(
                new NameOrNusIdContainsKeywordsPredicate(Arrays.asList("john"), Arrays.asList("A0234504F")));
        assertParseSuccess(parser, "john A0234504F", expectedFindCommand);
    }

}
