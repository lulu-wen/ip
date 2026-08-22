import java.util.Scanner;
import java.util.ArrayList;

public class Lumi {
    static final String LOGO = " _    _   _ __  __ ___ \n"
            + "| |  | | | |  \\/  |_ _|\n"
            + "| |  | | | | |\\/| || | \n"
            + "| |__| |_| | |  | || | \n"
            + "|_____\\___/|_|  |_|___|\n";
    static final String LINE = "____________________________________________________________\n";
    static final String INDENT = "  ";
    static ArrayList<Task> tasks = new ArrayList<>();

    public static void addTask(String description) {
        tasks.add(new Task(description));
        speak("added: " + description);
    }

    public static void listTasks() {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + ". " + tasks.get(i);
        }
        speak(lines);
    }

    public static void speak(String... messages) {
        System.out.println(LINE);
        for (String message : messages) {
            // one speak, one line
            System.out.println(INDENT + message);
        }
        System.out.println(LINE);
    }

    public static void greet() {
        System.out.println("Hello from");
        System.out.println(LOGO + LINE);
        System.out.println(INDENT + "Hello! I'm Lumi");
        System.out.println(INDENT + "What can I do for you?");
    }

    public static void main(String[] args) {
        greet();
        System.out.print(LINE);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            String[] inputParts = input.split(" ", 2);
            String cmd = inputParts[0].trim();
            if (input.toLowerCase().equals("bye")) {
                break;
            } else if (input.toLowerCase().equals("list")) {
                listTasks();
            } else if (cmd.toLowerCase().equals("mark")) {
                int taskIndex = Integer.parseInt(inputParts[1].trim()) - 1; // 0-based
                tasks.get(taskIndex).markAsDone();
                speak("Nice! I've marked this task as done:", "  " + tasks.get(taskIndex));
            } else if (cmd.toLowerCase().equals("unmark")) {
                int taskIndex = Integer.parseInt(inputParts[1].trim()) - 1; // 0-based
                tasks.get(taskIndex).markAsNotDone();
                speak("OK, I've marked this task as not done yet:", "  " + tasks.get(taskIndex));
            } else {
                addTask(input);
            }
        }
        speak("Bye. Hope to see you again soon!");
    }
}

