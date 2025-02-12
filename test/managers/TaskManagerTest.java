package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, 30, LocalDateTime.now());

        taskManager.add(task);

        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Epic 1", "Description");

        taskManager.add(epic);

        assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(), 30, LocalDateTime.now());

        taskManager.add(subtask);

        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test
    public void testRemoveTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, 30, LocalDateTime.now());
        taskManager.add(task);

        taskManager.taskRemove(task.getId());

        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void testRemoveEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);

        taskManager.epicRemove(epic.getId());

        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    public void testRemoveSubtask() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(), 30,
                LocalDateTime.now());
        taskManager.add(subtask);

        taskManager.subtaskRemove(subtask.getId());

        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, 30, LocalDateTime.now());
        taskManager.add(task);
        task.setStatus(Status.IN_PROGRESS);

        taskManager.update(task);

        assertEquals(Status.IN_PROGRESS, taskManager.taskSearch(task.getId()).getStatus());
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        epic.setDescription("Updated Description");

        taskManager.update(epic);

        assertEquals("Updated Description", taskManager.epicSearch(epic.getId()).getDescription());
    }

    @Test
    public void testUpdateSubtask() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(), 30,
                LocalDateTime.now());
        taskManager.add(subtask);
        subtask.setStatus(Status.IN_PROGRESS);

        taskManager.update(subtask);

        assertEquals(Status.IN_PROGRESS, taskManager.subtaskSearch(subtask.getId()).getStatus());
    }

    @Test
    public void testGetHistory() {
        Task task = new Task("Task 1", "Description", Status.NEW, 30, LocalDateTime.now());
        taskManager.add(task);

        taskManager.taskSearch(task.getId());

        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void testTaskOverlapping() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 30, LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", Status.NEW, 30,
                LocalDateTime.now().plusMinutes(15));

        taskManager.add(task1);
        taskManager.add(task2);

        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    public void testEpicStatusAllNew() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(), 30,
                LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.NEW, epic.getId(), 30,
                LocalDateTime.now().plusHours(1));
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void testEpicStatusAllDone() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.DONE, epic.getId(),
                30, LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId(),
                30, LocalDateTime.now().plusHours(1));
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void testEpicStatusNewAndDone() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(),
                30, LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId(),
                30, LocalDateTime.now().plusHours(1));
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicStatusInProgress() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.add(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.IN_PROGRESS, epic.getId(),
                30, LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.IN_PROGRESS, epic.getId(),
                30, LocalDateTime.now().plusHours(1));
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}