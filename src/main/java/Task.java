/**
 * Represents a single item of work tracked by Lumi.
 * A task carries a description and a completion status; each concrete
 * subclass decides the icon that identifies its type in the task listing.
 */
public abstract class Task {
    private static final String ICON_DONE = "X";
    private static final String ICON_NOT_DONE = " ";

    protected final String description;
    protected boolean isDone;

    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns the single-character icon that identifies this task's type. */
    public abstract String getTypeIcon();

    /** Returns the icon shown in the status box: marked when done, blank otherwise. */
    public String getStatusIcon() {
        return isDone ? ICON_DONE : ICON_NOT_DONE;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
