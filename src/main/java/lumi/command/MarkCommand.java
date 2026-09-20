package lumi.command;

import lumi.LumiException;
import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;
import lumi.task.Task;

/**
 * Marks one task done or not done. Both directions share the same steps and
 * differ only in the wording, so one command covers mark and unmark.
 */
public class MarkCommand extends Command {
    private final int taskIndex;
    private final boolean shouldBeDone;

    public MarkCommand(int taskIndex, boolean shouldBeDone) {
        this.taskIndex = taskIndex;
        this.shouldBeDone = shouldBeDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LumiException {
        Task task = tasks.get(taskIndex);
        if (shouldBeDone) {
            task.markAsDone();
            storage.save(tasks.asList());
            ui.show("Nice! I've marked this task as done:", Ui.TASK_INDENT + task);
        } else {
            task.markAsNotDone();
            storage.save(tasks.asList());
            ui.show("OK, I've marked this task as not done yet:", Ui.TASK_INDENT + task);
        }
    }
}
