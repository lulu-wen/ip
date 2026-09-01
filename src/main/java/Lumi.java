import java.util.ArrayList;
import java.util.Scanner;

/**
 * A command-line task tracker. Lumi reads commands from standard input,
 * records todos, deadlines and events, and reports the list back on request.
 */
public class Lumi {
    private static final String LOGO = " _    _   _ __  __ ___ \n"
            + "| |  | | | |  \\/  |_ _|\n"
            + "| |  | | | | |\\/| || | \n"
            + "| |__| |_| | |  | || | \n"
            + "|_____\\___/|_|  |_|___|";
    private static final String LINE = "    ____________________________________________________________";
    private static final String INDENT = "     ";
    private static final String TASK_INDENT = "  ";

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final String OPTION_BY = "/by";
    private static final String OPTION_FROM = "/from";
    private static final String OPTION_TO = "/to";

    /**
     * Split limit that keeps everything after the first separator in one piece,
     * so that a description may itself contain spaces or further separators.
     */
    private static final int KEYWORD_AND_REMAINDER = 2;

    /** Task numbers shown to the user start at 1, whereas list indexes start at 0. */
    private static final int FIRST_TASK_NUMBER = 1;

    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        readCommandsUntilExit();
        speak("Bye. Hope to see you again soon!");
    }

    private static void greet() {
        System.out.println("Hello from");
        System.out.println(LOGO);
        speak("Hello! I'm Lumi", "What can I do for you?");
    }

    /** Reads and runs commands until the user says bye or the input is exhausted. */
    private static void readCommandsUntilExit() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRunning = true;
            while (isRunning && scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    isRunning = executeInput(input);
                }
            }
        }
    }

    /** Separates one line of input into its command word and arguments, then runs it. */
    private static boolean executeInput(String input) {
        String[] inputParts = input.split(" ", KEYWORD_AND_REMAINDER);
        String command = inputParts[0].toLowerCase();
        return executeCommand(command, extractRemainder(inputParts));
    }

    /** Executes one user command. Returns true if the program should keep running. */
    private static boolean executeCommand(String command, String arguments) {
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
        case COMMAND_TODO:
            addTask(new Todo(arguments));
            return true;
        case COMMAND_DEADLINE:
            addTask(createDeadline(arguments));
            return true;
        case COMMAND_EVENT:
            addTask(createEvent(arguments));
            return true;
        default:
            speak("I'm sorry, but I don't know what that means :-(");
            return true;
        }
    }

    /** Builds a deadline from arguments shaped as {@code description /by when}. */
    private static Deadline createDeadline(String arguments) {
        String[] parts = arguments.split(OPTION_BY, KEYWORD_AND_REMAINDER);
        return new Deadline(parts[0].trim(), extractRemainder(parts));
    }

    /** Builds an event from arguments shaped as {@code description /from start /to end}. */
    private static Event createEvent(String arguments) {
        String[] fromParts = arguments.split(OPTION_FROM, KEYWORD_AND_REMAINDER);
        String description = fromParts[0].trim();
        if (fromParts.length == 1) {
            return new Event(description, "", "");
        }
        String[] toParts = fromParts[1].split(OPTION_TO, KEYWORD_AND_REMAINDER);
        return new Event(description, toParts[0].trim(), extractRemainder(toParts));
    }

    /**
     * Returns the trimmed text that followed the separator, or an empty string
     * when the user omitted that part of the command.
     */
    private static String extractRemainder(String[] parts) {
        return parts.length > 1 ? parts[1].trim() : "";
    }

    private static void addTask(Task task) {
        tasks.add(task);
        speak("Got it. I've added this task:",
                TASK_INDENT + task,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void listTasks() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            lines.add((i + FIRST_TASK_NUMBER) + "." + tasks.get(i));
        }
        speak(lines.toArray(new String[0]));
    }

    private static void setTaskDone(String arguments, boolean shouldBeDone) {
        Task task = tasks.get(parseTaskIndex(arguments));
        if (shouldBeDone) {
            task.markAsDone();
            speak("Nice! I've marked this task as done:", TASK_INDENT + task);
        } else {
            task.markAsNotDone();
            speak("OK, I've marked this task as not done yet:", TASK_INDENT + task);
        }
    }

    /** Converts the task number typed by the user into an index into {@code tasks}. */
    private static int parseTaskIndex(String arguments) {
        return Integer.parseInt(arguments.trim()) - FIRST_TASK_NUMBER;
    }

    /** Prints the given messages inside a pair of horizontal lines. */
    private static void speak(String... messages) {
        System.out.println(LINE);
        for (String message : messages) {
            System.out.println(INDENT + message);
        }
        System.out.println(LINE);
        System.out.println();
    }
}
