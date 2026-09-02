package lumi.task;

/** Represents a task that has to be finished before a stated point in time. */
public class Deadline extends Task {
    private static final String TYPE_ICON = "D";

    protected final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
