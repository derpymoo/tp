package cms.logic.parser;

import static cms.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cms.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.GITHUBUSERNAME_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.GITHUBUSERNAME_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static cms.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static cms.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static cms.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static cms.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static cms.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.NUSID_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.NUSID_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static cms.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static cms.logic.commands.CommandTestUtil.ROLE_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.ROLE_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.SOCUSERNAME_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.SOCUSERNAME_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static cms.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static cms.logic.commands.CommandTestUtil.TUTORIALGROUP_DESC_AMY;
import static cms.logic.commands.CommandTestUtil.TUTORIALGROUP_DESC_BOB;
import static cms.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static cms.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static cms.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static cms.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static cms.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static cms.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static cms.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static cms.logic.parser.CliSyntax.PREFIX_EMAIL;
import static cms.logic.parser.CliSyntax.PREFIX_GITHUBUSERNAME;
import static cms.logic.parser.CliSyntax.PREFIX_NAME;
import static cms.logic.parser.CliSyntax.PREFIX_NUSID;
import static cms.logic.parser.CliSyntax.PREFIX_PHONE;
import static cms.logic.parser.CliSyntax.PREFIX_ROLE;
import static cms.logic.parser.CliSyntax.PREFIX_SOCUSERNAME;
import static cms.logic.parser.CliSyntax.PREFIX_TUTORIALGROUP;
import static cms.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cms.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static cms.testutil.TypicalPersons.AMY;
import static cms.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import cms.logic.Messages;
import cms.logic.commands.AddCommand;
import cms.model.person.Address;
import cms.model.person.Email;
import cms.model.person.Name;
import cms.model.person.Person;
import cms.model.person.Phone;
import cms.model.tag.Tag;
import cms.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB
                + SOCUSERNAME_DESC_BOB + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TUTORIALGROUP_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB + GITHUBUSERNAME_DESC_BOB
                        + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TUTORIALGROUP_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TUTORIALGROUP_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_NUSID, PREFIX_ROLE,
                        PREFIX_SOCUSERNAME, PREFIX_GITHUBUSERNAME, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_PHONE, PREFIX_TUTORIALGROUP));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + NUSID_DESC_AMY + ROLE_DESC_AMY + SOCUSERNAME_DESC_AMY
                + GITHUBUSERNAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + TUTORIALGROUP_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TUTORIALGROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TUTORIALGROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TUTORIALGROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TUTORIALGROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TUTORIALGROUP_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + NUSID_DESC_BOB + ROLE_DESC_BOB + SOCUSERNAME_DESC_BOB
                + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TUTORIALGROUP_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + NUSID_DESC_BOB + ROLE_DESC_BOB
                + SOCUSERNAME_DESC_BOB + GITHUBUSERNAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TUTORIALGROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
