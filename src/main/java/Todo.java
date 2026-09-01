/** Represents a task that has no date attached to it. */
public class Todo extends Task {
    private static final String TYPE_ICON = "T";

    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }
}
