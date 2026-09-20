package lumi;
import java.util.ArrayList;
import java.util.List;

import lumi.command.AddCommand;
import lumi.command.Command;
import lumi.command.DeleteCommand;
import lumi.command.ExitCommand;
import lumi.command.ListCommand;
import lumi.command.MarkCommand;

/**
 * A command-line task tracker. Lumi reads commands from standard input,
 * records todos, deadlines and events, and reports the list back on request.
 */
public class Lumi {

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    /** Where the task list lives, relative to the folder Lumi is run from. */
    private static final String SAVE_FILE_PATH = "data/lumi.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Wires up the parts Lumi needs. Nothing is read or printed here, so the
     * order in which the user sees things stays decided by {@link #run()}.
     *
     * @param filePath Where the task list is kept between runs.
     */
    public Lumi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList();
    }

    /** Greets the user, restores the saved tasks, then serves commands until bye. */
    public void run() {
        ui.showWelcome();
        loadTasks();
        readCommandsUntilExit();
        ui.show("Bye. Hope to see you again soon!");
    }

    public static void main(String[] args) {
        new Lumi(SAVE_FILE_PATH).run();
    }

    /**
     * Fills the task list with whatever a previous run saved. A save file that
     * cannot be read is reported and the session starts with an empty list,
     * so a damaged file never stops Lumi from running.
     */
    private void loadTasks() {
        List<String> skipped = new ArrayList<>();
        try {
            tasks.addAll(storage.load(skipped));
        } catch (LumiException e) {
            ui.show(e.getMessage(), "Starting with an empty list.");
            return;
        }
        if (!skipped.isEmpty()) {
            ArrayList<String> report = new ArrayList<>(skipped);
            report.add("Everything else in the file was loaded.");
            ui.show(report.toArray(new String[0]));
        }
    }

    /** Reads and runs commands until the user says bye or the input is exhausted. */
    private void readCommandsUntilExit() {
        boolean isRunning = true;
        while (isRunning && ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (!input.isEmpty()) {
                try {
                    isRunning = executeInput(input);
                } catch (LumiException e) {
                    ui.show(e.getMessage());
                }
            }
        }
        ui.close();
    }

    /** Separates one line of input into its command word and arguments, then runs it. */
    private boolean executeInput(String input) throws LumiException {
        return executeCommand(Parser.parseCommandWord(input), Parser.parseArguments(input));
    }

    /** Runs one command and reports whether the session should carry on. */
    private boolean executeCommand(String command, String arguments) throws LumiException {
        Command toRun = createCommand(command, arguments);
        toRun.execute(tasks, ui, storage);
        return !toRun.isExit();
    }

    /** Chooses the command that matches the keyword, ready to be run. */
    private Command createCommand(String command, String arguments) throws LumiException {
        switch (command) {
        case COMMAND_BYE:
            return new ExitCommand();
        case COMMAND_LIST:
            return new ListCommand();
        case COMMAND_MARK:
            return new MarkCommand(Parser.parseTaskIndex(arguments), true);
        case COMMAND_UNMARK:
            return new MarkCommand(Parser.parseTaskIndex(arguments), false);
        case COMMAND_DELETE:
            return new DeleteCommand(Parser.parseTaskIndex(arguments));
        case COMMAND_TODO:
            return new AddCommand(Parser.parseTodo(arguments));
        case COMMAND_DEADLINE:
            return new AddCommand(Parser.parseDeadline(arguments));
        case COMMAND_EVENT:
            return new AddCommand(Parser.parseEvent(arguments));
        default:
            throw new LumiException("I don't know that one. I understand: "
                    + "todo, deadline, event, list, mark, unmark, delete, bye.");
        }
    }

}
