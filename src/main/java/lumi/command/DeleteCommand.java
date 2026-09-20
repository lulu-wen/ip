package lumi.command;

import lumi.LumiException;
import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;
import lumi.task.Task;

/** Removes one task from the list and reports what is left. */
public class DeleteCommand extends Command {
    private final int taskIndex;

    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LumiException {
        Task removed = tasks.remove(taskIndex);
        storage.save(tasks.asList());
        ui.show("Noted. I've removed this task:",
                Ui.TASK_INDENT + removed,
                "Now you have " + tasks.size() + " tasks in the list.");
    }
}
