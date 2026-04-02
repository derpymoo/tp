package cms.logic.parser;

import static cms.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cms.logic.commands.CommandTestUtil.VALID_NUSID_AMY;
import static cms.logic.commands.CommandTestUtil.VALID_NUSID_BOB;
import static cms.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cms.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static cms.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static cms.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import cms.logic.commands.TagCommand;
import cms.logic.commands.TagCommand.Action;
import cms.model.person.NusId;
import cms.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validAddArgs_returnsTagCommand() {
        assertParseSuccess(parser, "add n/1 2 tag/friend tutor",
                new TagCommand(Action.ADD, List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                        List.of(new Tag("friend"), new Tag("tutor"))));

        assertParseSuccess(parser, "add id/" + VALID_NUSID_AMY + " " + VALID_NUSID_BOB + " tag/friend",
                TagCommand.byNusIds(Action.ADD, List.of(new NusId(VALID_NUSID_AMY), new NusId(VALID_NUSID_BOB)),
                        List.of(new Tag("friend"))));
    }

    @Test
    public void parse_validDeleteArgs_returnsTagCommand() {
        assertParseSuccess(parser, "delete n/1 2 tag/friend tutor",
                new TagCommand(Action.DELETE, List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                        List.of(new Tag("friend"), new Tag("tutor"))));

        assertParseSuccess(parser, "delete id/" + VALID_NUSID_AMY + " id/" + VALID_NUSID_BOB + " tag/friend",
                TagCommand.byNusIds(Action.DELETE, List.of(new NusId(VALID_NUSID_AMY), new NusId(VALID_NUSID_BOB)),
                        List.of(new Tag("friend"))));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "add", expectedMessage);
        assertParseFailure(parser, "rename n/1 tag/friend", expectedMessage);
        assertParseFailure(parser, "add 1 tag/friend", expectedMessage);
        assertParseFailure(parser, "add n/1 id/" + VALID_NUSID_AMY + " tag/friend", expectedMessage);
        assertParseFailure(parser, "delete n/1", expectedMessage);
    }
}
