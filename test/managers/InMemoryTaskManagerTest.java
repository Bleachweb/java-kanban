package managers;

import org.junit.jupiter.api.Test;
import statuses.Status;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void testClearTasks() {
        Task task = new Task("Task 1", "Description", Status.NEW, 30, LocalDateTime.now());
        taskManager.add(task);

        taskManager.clearTasks();

        assertEquals(0, taskManager.getTasks().size());
    }
}