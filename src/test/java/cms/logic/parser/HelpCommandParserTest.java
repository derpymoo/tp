package cms.logic.parser;

import static cms.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cms.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cms.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import cms.logic.commands.AddCommand;
import cms.logic.commands.HelpCommand;

public class HelpCommandParserTest {

    private final HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_emptyArgs_returnsOverviewHelpCommand() {
        assertParseSuccess(parser, "   ", new HelpCommand());
    }

    @Test
    public void parse_validSingleCommand_returnsSpecificHelpCommand() {
        assertParseSuccess(parser, "  " + AddCommand.COMMAND_WORD + "  ", new HelpCommand(AddCommand.COMMAND_WORD));
    }

    @Test
    public void parse_multipleCommands_throwsParseException() {
        assertParseFailure(parser,
                "add delete",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownCommand_throwsParseException() {
        assertParseFailure(parser,
                "unknown",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }
}
