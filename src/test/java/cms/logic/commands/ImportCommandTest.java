package cms.logic.commands;

import static cms.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import cms.logic.commands.ImportCommand.KeepPolicy;
import cms.model.Model;
import cms.model.ModelManager;
import cms.model.UserPrefs;

public class ImportCommandTest {

    @Test
    public void execute_validPath_success() {
        Path path = Path.of("data/import.json");
        ImportCommand importCommand = new ImportCommand(path);

        Model model = new ModelManager();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(importCommand, model,
                String.format(ImportCommand.MESSAGE_SUCCESS, path), expectedModel);
    }

    @Test
    public void equals() {
        ImportCommand importFirstCommand = new ImportCommand(Path.of("data/first.json"),
                KeepPolicy.CURRENT);
        ImportCommand importFirstCommandCopy = new ImportCommand(Path.of("data/first.json"),
                KeepPolicy.CURRENT);
        ImportCommand importSecondCommand = new ImportCommand(Path.of("data/second.json"),
                KeepPolicy.CURRENT);
        ImportCommand importDifferentMode = new ImportCommand(Path.of("data/first.json"),
                KeepPolicy.INCOMING);

        assertTrue(importFirstCommand.equals(importFirstCommand));
        assertTrue(importFirstCommand.equals(importFirstCommandCopy));
        assertFalse(importFirstCommand.equals(importSecondCommand));
        assertFalse(importFirstCommand.equals(importDifferentMode));
        assertFalse(importFirstCommand.equals(1));
        assertFalse(importFirstCommand.equals(null));
    }
}
