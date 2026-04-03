package cms.logic.commands;

import static cms.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cms.model.Model;
import cms.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_helpOverview_success() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.SHOWING_HELP_MESSAGE,
                true,
                false,
                HelpCommand.getAllHelpMessages());
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpForCommand_success() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.SHOWING_HELP_MESSAGE,
                true,
                false,
                AddCommand.MESSAGE_USAGE);
        assertCommandSuccess(new HelpCommand(AddCommand.COMMAND_WORD), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        HelpCommand helpCommand = new HelpCommand();

        assertTrue(helpCommand.equals(helpCommand));
        assertFalse(helpCommand.equals(null));
        assertFalse(helpCommand.equals(new Object()));
        assertEquals(new HelpCommand(), new HelpCommand());
        assertEquals(new HelpCommand(AddCommand.COMMAND_WORD), new HelpCommand(AddCommand.COMMAND_WORD));
    }

    @Test
    public void toOneLineHelp_noNewline_returnsSameLine() {
        String result = HelpCommand.toOneLineHelp("list: Lists all persons.");
        assertEquals("- list: Lists all persons.", result);
    }
}
