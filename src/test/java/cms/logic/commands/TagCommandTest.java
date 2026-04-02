package cms.logic.commands;

import static cms.logic.commands.CommandTestUtil.assertCommandFailure;
import static cms.logic.commands.CommandTestUtil.assertCommandSuccess;
import static cms.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static cms.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static cms.testutil.TypicalPersons.ALICE;
import static cms.testutil.TypicalPersons.BENSON;
import static cms.testutil.TypicalPersons.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import cms.commons.core.index.Index;
import cms.model.Model;
import cms.model.ModelManager;
import cms.model.UserPrefs;
import cms.model.person.NusId;
import cms.model.person.Person;
import cms.model.tag.Tag;
import cms.testutil.PersonBuilder;

public class TagCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addByIndexes_success() {
        TagCommand command = new TagCommand(TagCommand.Action.ADD,
                List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                List.of(new Tag("tag1"), new Tag("tag2")));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person updatedAlice = new PersonBuilder(ALICE).withTags("friends", "tag1", "tag2").build();
        Person updatedBenson = new PersonBuilder(BENSON).withTags("owesMoney", "friends", "tag1", "tag2").build();
        expectedModel.setPerson(ALICE, updatedAlice);
        expectedModel.setPerson(BENSON, updatedBenson);

        String expectedMessage = "tag1, tag2 has been added to "
                + "1, Alice Pauline, A0000001B; 2, Benson Meier, A0234501C";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteByNusIds_success() {
        TagCommand command = TagCommand.byNusIds(TagCommand.Action.DELETE,
                List.of(ALICE.getNusId(), BENSON.getNusId()),
                List.of(new Tag("friends"), new Tag("owesMoney")));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person updatedAlice = new PersonBuilder(ALICE).withTags().build();
        Person updatedBenson = new PersonBuilder(BENSON).withTags().build();
        expectedModel.setPerson(ALICE, updatedAlice);
        expectedModel.setPerson(BENSON, updatedBenson);

        String expectedMessage = "friends has been removed from "
                + "1, Alice Pauline, A0000001B; 2, Benson Meier, A0234501C\n"
                + "owesmoney has been removed from 2, Benson Meier, A0234501C";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteSkipsPersonsWithoutTag_success() {
        TagCommand command = new TagCommand(TagCommand.Action.DELETE,
                List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                List.of(new Tag("owesMoney")));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person updatedBenson = new PersonBuilder(BENSON).withTags("friends").build();
        expectedModel.setPerson(BENSON, updatedBenson);

        String expectedMessage = "owesmoney has been removed from 2, Benson Meier, A0234501C";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addNoChanges_returnsNoChangesMessage() {
        TagCommand command = new TagCommand(TagCommand.Action.ADD,
                List.of(INDEX_FIRST_PERSON),
                List.of(new Tag("friends")));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(command, model, TagCommand.MESSAGE_ADD_NO_CHANGES, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        TagCommand command = new TagCommand(TagCommand.Action.ADD,
                List.of(Index.fromOneBased(model.getFilteredPersonList().size() + 1)),
                List.of(new Tag("friend")));

        assertCommandFailure(command, model, cms.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidNusId_throwsCommandException() {
        TagCommand command = TagCommand.byNusIds(TagCommand.Action.DELETE,
                List.of(new NusId("A9999999Z")),
                List.of(new Tag("friend")));

        assertCommandFailure(command, model, TagCommand.MESSAGE_INVALID_NUS_ID);
    }

    @Test
    public void constructor_emptyTargets_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new TagCommand(TagCommand.Action.ADD, List.of(), List.of(new Tag("friend"))));
        assertThrows(IllegalArgumentException.class, () ->
                TagCommand.byNusIds(TagCommand.Action.ADD, List.of(), List.of(new Tag("friend"))));
    }

    @Test
    public void equals() {
        TagCommand addFirstCommand = new TagCommand(TagCommand.Action.ADD, List.of(INDEX_FIRST_PERSON),
                List.of(new Tag("friend")));
        TagCommand addFirstCommandCopy = new TagCommand(TagCommand.Action.ADD, List.of(INDEX_FIRST_PERSON),
                List.of(new Tag("friend")));
        TagCommand deleteSecondCommand = new TagCommand(TagCommand.Action.DELETE, List.of(INDEX_SECOND_PERSON),
                List.of(new Tag("friend")));

        assertTrue(addFirstCommand.equals(addFirstCommand));
        assertTrue(addFirstCommand.equals(addFirstCommandCopy));
        assertFalse(addFirstCommand.equals(deleteSecondCommand));
        assertFalse(addFirstCommand.equals(null));
        assertFalse(addFirstCommand.equals(1));
    }
}
