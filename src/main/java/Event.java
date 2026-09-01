/** Represents a task that spans a period between two stated points in time. */
public class Event extends Task {
    private static final String TYPE_ICON = "E";

    protected final String from;
    protected final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
