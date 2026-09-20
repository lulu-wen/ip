package lumi;
import java.util.ArrayList;
import java.util.List;

import lumi.task.Task;

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

    /** Executes one user command. Returns true if the program should keep running. */
    private boolean executeCommand(String command, String arguments) throws LumiException {
        switch (command) {
        case COMMAND_BYE:
            return false;
        case COMMAND_LIST:
            listTasks();
            return true;
        case COMMAND_MARK:
            setTaskDone(arguments, true);
            return true;
        case COMMAND_UNMARK:
            setTaskDone(arguments, false);
            return true;
        case COMMAND_DELETE:
            deleteTask(arguments);
            return true;
        case COMMAND_TODO:
            addTask(Parser.parseTodo(arguments));
            return true;
        case COMMAND_DEADLINE:
            addTask(Parser.parseDeadline(arguments));
            return true;
        case COMMAND_EVENT:
            addTask(Parser.parseEvent(arguments));
            return true;
        default:
            throw new LumiException("I don't know that one. I understand: "
                    + "todo, deadline, event, list, mark, unmark, delete, bye.");
        }
    }

    private void addTask(Task task) throws LumiException {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.show("Got it. I've added this task:",
                Ui.TASK_INDENT + task,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    private void listTasks() {
        if (tasks.isEmpty()) {
            ui.show("Your list is empty. " + Parser.TODO_FORMAT);
            return;
        }
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Here are the tasks in your list:");
        int number = TaskList.FIRST_TASK_NUMBER;
        for (Task task : tasks.asList()) {
            lines.add(number + "." + task);
            number++;
        }
        ui.show(lines.toArray(new String[0]));
    }

    private void deleteTask(String arguments) throws LumiException {
        int taskIndex = Parser.parseTaskIndex(arguments);
        Task removed = tasks.remove(taskIndex);
        storage.save(tasks.asList());
        ui.show("Noted. I've removed this task:",
                Ui.TASK_INDENT + removed,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    private void setTaskDone(String arguments, boolean shouldBeDone) throws LumiException {
        int taskIndex = Parser.parseTaskIndex(arguments);
        Task task = tasks.get(taskIndex);
        if (shouldBeDone) {
            task.markAsDone();
            storage.save(tasks.asList());
            ui.show("Nice! I've marked this task as done:", Ui.TASK_INDENT + task);
        } else {
            task.markAsNotDone();
            storage.save(tasks.asList());
            ui.show("OK, I've marked this task as not done yet:", Ui.TASK_INDENT + task);
        }
    }

}
