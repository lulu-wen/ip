package lumi;

import lumi.task.Deadline;
import lumi.task.Event;
import lumi.task.Task;
import lumi.task.Todo;

/**
 * Turns the raw text the user typed into something Lumi can act on: a command
 * word, its arguments, a task number, or a fully built task. Everything here
 * works on strings alone, so it never needs to know what the task list holds
 * or how a reply is printed.
 */
public class Parser {
    /** Worked examples appended to error messages so the user can see the expected shape. */
    public static final String TODO_FORMAT = "Try: todo read book";
    public static final String DEADLINE_FORMAT = "Try: deadline return book /by Sunday";
    public static final String EVENT_FORMAT = "Try: event project meeting /from Mon 2pm /to 4pm";

    private static final String OPTION_BY = "/by";
    private static final String OPTION_FROM = "/from";
    private static final String OPTION_TO = "/to";

    /**
     * Split limit that keeps everything after the first separator in one piece,
     * so that a description may itself contain spaces or further separators.
     */
    private static final int KEYWORD_AND_REMAINDER = 2;

    /** Returns the command word of an input line, in lower case. */
    public static String parseCommandWord(String input) {
        return input.split(" ", KEYWORD_AND_REMAINDER)[0].toLowerCase();
    }

    /** Returns the text following the command word, or an empty string if there is none. */
    public static String parseArguments(String input) {
        return extractRemainder(input.split(" ", KEYWORD_AND_REMAINDER));
    }

    /**
     * Builds a todo from its description.
     *
     * @throws LumiException If the description is missing or cannot be saved.
     */
    public static Todo parseTodo(String argument) throws LumiException {
        if (argument.isEmpty()) {
            throw new LumiException("A todo needs a description. " + TODO_FORMAT);
        }
        requireSavableText(argument);
        return new Todo(argument);
    }

    /**
     * Builds a deadline from arguments shaped as {@code description /by when}.
     *
     * @throws LumiException If either part is missing or cannot be saved.
     */
    public static Deadline parseDeadline(String arguments) throws LumiException {
        String[] parts = arguments.split(OPTION_BY, KEYWORD_AND_REMAINDER);
        String description = parts[0].trim();
        String by = extractRemainder(parts);

        if (description.isEmpty()) {
            throw new LumiException("A deadline needs something to do. " + DEADLINE_FORMAT);
        }
        if (by.isEmpty()) {
            throw new LumiException("A deadline needs a due date. " + DEADLINE_FORMAT);
        }
        requireSavableText(description);
        requireSavableText(by);

        return new Deadline(description, by);
    }

    /**
     * Builds an event from arguments shaped as {@code description /from start /to end}.
     *
     * @throws LumiException If any part is missing or cannot be saved.
     */
    public static Event parseEvent(String arguments) throws LumiException {
        String[] fromParts = arguments.split(OPTION_FROM, KEYWORD_AND_REMAINDER);
        String description = fromParts[0].trim();
        String from = "";
        String to = "";
        if (fromParts.length > 1) {
            String[] toParts = fromParts[1].split(OPTION_TO, KEYWORD_AND_REMAINDER);
            from = toParts[0].trim();
            to = extractRemainder(toParts);
        }

        if (description.isEmpty()) {
            throw new LumiException("An event needs a name. " + EVENT_FORMAT);
        }
        if (from.isEmpty()) {
            throw new LumiException("An event needs a start time. " + EVENT_FORMAT);
        }
        if (to.isEmpty()) {
            throw new LumiException("An event needs an end time. " + EVENT_FORMAT);
        }
        requireSavableText(description);
        requireSavableText(from);
        requireSavableText(to);

        return new Event(description, from, to);
    }

    /**
     * Converts the task number typed by the user into a list index. Whether that
     * index actually holds a task is for the task list to decide.
     *
     * @throws LumiException If the text is not a task number.
     */
    public static int parseTaskIndex(String arguments) throws LumiException {
        try {
            return Integer.parseInt(arguments.trim()) - TaskList.FIRST_TASK_NUMBER;
        } catch (NumberFormatException e) {
            if (isAllDigits(arguments.trim())) {
                throw new LumiException("That task number is far too large. Try: mark 1");
            }
            throw new LumiException("Task numbers are digits. Try: mark 1");
        }
    }

    /**
     * Returns the trimmed text that followed the separator, or an empty string
     * when the user omitted that part of the command.
     */
    private static String extractRemainder(String[] parts) {
        return parts.length > 1 ? parts[1].trim() : "";
    }

    /** Returns true only for a non-empty run of digits, with no sign or spaces. */
    private static boolean isAllDigits(String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rejects task text holding the character that separates fields in the save
     * file, because saving it would make the task unreadable when loaded back.
     */
    private static void requireSavableText(String text) throws LumiException {
        if (text.contains(Task.SEPARATOR_CHARACTER)) {
            throw new LumiException("Task text cannot contain '" + Task.SEPARATOR_CHARACTER
                    + "', because Lumi uses that to separate fields when it saves.");
        }
    }
}
