package cms.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

import cms.model.Model;
import cms.model.ModelManager;
import cms.model.ReadOnlyAddressBook;
import cms.storage.JsonAddressBookStorage;
import cms.storage.JsonUserPrefsStorage;
import cms.storage.StorageManager;

public class ExportCommandTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_validPath_success() throws Exception {
        Path path = temporaryFolder.resolve("data").resolve("export.json");
        ExportCommand exportCommand = new ExportCommand(path);

        Model model = new ModelManager();
        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json")),
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")));

        CommandResult result = exportCommand.execute(model, storage);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, path), result.getFeedbackToUser());
        assertTrue(Files.exists(path));
    }

    @Test
    public void execute_withoutStorageContext_throwsCommandException() {
        Path path = temporaryFolder.resolve("data").resolve("export.json");
        ExportCommand exportCommand = new ExportCommand(path);

        Model model = new ModelManager();
        assertThrows(cms.logic.commands.exceptions.CommandException.class, () -> exportCommand.execute(model));
    }

    @Test
    public void execute_storagePermissionDenied_throwsCommandException() {
        Path path = temporaryFolder.resolve("data").resolve("exportDenied.json");
        ExportCommand exportCommand = new ExportCommand(path);

        Model model = new ModelManager();
        Path addressBookPath = temporaryFolder.resolve("addressBook.json");
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(addressBookPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
                throw new AccessDeniedException(filePath.toString());
            }
        };
        StorageManager storage = new StorageManager(
                addressBookStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")));

        Executable exportExecution = () -> exportCommand.execute(model, storage);
        cms.logic.commands.exceptions.CommandException exception =
            assertThrows(cms.logic.commands.exceptions.CommandException.class, exportExecution);
        assertEquals(String.format(ExportCommand.MESSAGE_EXPORT_PERMISSION_ERROR_FORMAT, path),
                exception.getMessage());
    }
}
