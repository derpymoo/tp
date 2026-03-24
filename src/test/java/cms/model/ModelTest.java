package cms.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import cms.commons.core.GuiSettings;
import cms.model.person.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModelTest {

    @Test
    public void sortPersonsByTutorialGroup_defaultImplementation_noOp() {
        Model model = new ModelStub();

        assertDoesNotThrow(model::sortPersonsByTutorialGroup);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    private static class ModelStub implements Model {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {}

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return new UserPrefs();
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {}

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("addressbook.json");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {}

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {}

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public boolean hasPersonWithConflictingField(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {}

        @Override
        public void addPerson(Person person) {}

        @Override
        public void setPerson(Person target, Person editedPerson) {}

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {}
    }
}
