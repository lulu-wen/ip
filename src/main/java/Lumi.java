import java.util.Scanner;
import java.util.ArrayList;

public class Lumi {
    static String logo = " _    _   _ __  __ ___ \n"
            + "| |  | | | |  \\/  |_ _|\n"
            + "| |  | | | | |\\/| || | \n"
            + "| |__| |_| | |  | || | \n"
            + "|_____\\___/|_|  |_|___|\n";
    static String line = "____________________________________________________________\n";
    static String indent = "  ";
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
        System.out.println(line);
        for (String message : messages) {
            // one speak, one line
            System.out.println(indent + message);
        }
        System.out.println(line);
    }

    public static void greet() {
        System.out.println("Hello from");
        System.out.println(logo + line);
        System.out.println(indent + "Hello! I'm Lumi");
        System.out.println(indent + "What can I do for you?");
    }

    public static void main(String[] args) {
        greet();
        System.out.print(line);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            String[] input_part = input.split(" ",2);
            String cmd = input_part[0].trim();
            if(input.toLowerCase().equals("bye")) {
                break;
            } else if (input.toLowerCase().equals("list")) {
                listTasks();
            } else if (cmd.toLowerCase().equals("mark")) {
                int cmd_id = Integer.parseInt(input_part[1].trim()) - 1; // 0-based
                tasks.get(cmd_id).markAsDone();
                speak("Nice! I've marked this task as done:",  "  " + tasks.get((cmd_id)));
            } else if (cmd.toLowerCase().equals("unmark")) {
                int cmd_id = Integer.parseInt(input_part[1].trim()) - 1; // 0-based
                tasks.get(cmd_id).markAsNotDone();
                speak("OK, I've marked this task as not done yet:", "  " + tasks.get(cmd_id));
            } else {
                addTask(input);
            }
        }
        speak("Bye. Hope to see you again soon!");
    }
}

