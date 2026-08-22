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
    static ArrayList<String> tasks = new ArrayList<>();

    public static void addTask(String description) {
        tasks.add(description);
        speak("added: " + description);
    }

    public static void listTasks() {
        String[] lines = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            lines[i] = (i + 1) + ". " + tasks.get(i);
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
            if(input.toLowerCase().equals("bye")) {
                break;
            } else if (input.toLowerCase().equals("list")) {
                listTasks();
            } else {
                addTask(input);
            }
        }
        speak("Bye. Hope to see you again soon!");
    }
}
