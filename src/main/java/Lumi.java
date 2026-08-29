import java.util.ArrayList;
import java.util.Scanner;

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

    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRunning = true;
            while (isRunning && scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }
                String[] inputParts = input.split(" ", 2);
                String command = inputParts[0].toLowerCase();
                String arguments = (inputParts.length > 1) ? inputParts[1].trim() : "";
                isRunning = executeCommand(command, arguments);
            }
        }
        speak("Bye. Hope to see you again soon!");
    }

    private static void greet() {
        System.out.println("Hello from");
        System.out.println(LOGO);
        speak("Hello! I'm Lumi", "What can I do for you?");
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

    /** Creates a deadline from arguments */
    private static Deadline createDeadline(String arguments) {
        String[] parts = arguments.split(OPTION_BY, 2);
        String description = parts[0].trim();
        String by = (parts.length > 1) ? parts[1].trim() : "";
        return new Deadline(description, by);
    }

    /** Creates an event from arguments */
    private static Event createEvent(String arguments) {
        String[] fromParts = arguments.split(OPTION_FROM, 2);
        String description = fromParts[0].trim();
        String from = "";
        String to = "";
        if (fromParts.length > 1) {
            String[] toParts = fromParts[1].split(OPTION_TO, 2);
            from = toParts[0].trim();
            to = (toParts.length > 1) ? toParts[1].trim() : "";
        }
        return new Event(description, from, to);
    }

    private static void addTask(Task task) {
        tasks.add(task);
        speak("Got it. I've added this task:",
                TASK_INDENT + task,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void listTasks() {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        speak(lines);
    }

    private static void setTaskDone(String arguments, boolean isDone) {
        int taskIndex = Integer.parseInt(arguments.trim()) - 1;
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
            speak("Nice! I've marked this task as done:", TASK_INDENT + task);
        } else {
            task.markAsNotDone();
            speak("OK, I've marked this task as not done yet:", TASK_INDENT + task);
        }
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
