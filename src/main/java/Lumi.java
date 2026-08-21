import java.util.Scanner;

public class Lumi {
    static String logo = " _    _   _ __  __ ___ \n"
            + "| |  | | | |  \\/  |_ _|\n"
            + "| |  | | | | |\\/| || | \n"
            + "| |__| |_| | |  | || | \n"
            + "|_____\\___/|_|  |_|___|\n";
    static String line = "____________________________________________________________\n";
    static String indent = "  ";
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
            }
            if (input.isEmpty()) {
                continue;
            }
            speak(input);
        }
        speak("Bye. Hope to see you again soon!");
    }
}
