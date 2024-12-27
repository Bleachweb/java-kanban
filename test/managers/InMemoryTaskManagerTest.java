package managers;

import statuses.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private Task task1;
    private Epic epic1;


    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("посуда", "помыть тарелки", Status.NEW);
        epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег");
    }

    @Test
    void shouldAddAndGetNewTasks() {
        taskManager.add(task1);

        Task task = taskManager.taskSearch(task1.getId());

        assertNotNull(task, "Задача не найдена.");
        assertEquals(task1, task, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldAddAndGetNewEpics() {
        taskManager.add(epic1);

        Epic epic = taskManager.epicSearch(epic1.getId());

        assertNotNull(epic, "Задача не найдена.");
        assertEquals(epic1, epic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldAddAndGetNewSubtasks() {
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,1);
        taskManager.add(subtask1);

        Subtask subtask = taskManager.subtaskSearch(subtask1.getId());

        assertNotNull(subtask1, "Задача не найдена.");
        assertEquals(subtask1, subtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldNotHaveConflicts () {
        taskManager.add(task1);
        Task task2 =  new Task("посуда", "помыть тарелки", Status.NEW,1);

        taskManager.update(task2);

        assertEquals(task1,taskManager.taskSearch(task2.getId()));
    }

    @Test
    void shouldCheckTasksStability () {
        taskManager.add(task1);

        Task task2 = taskManager.taskSearch(task1.getId());

        assertEquals(task1.getName(),task2.getName());
        assertEquals(task1.getDescription(),task2.getDescription());
        assertEquals(task1.getStatus(),task2.getStatus());
    }

    @Test
    void shouldCheckTasksStabilityWhileAddingToHistoryManager () {
        taskManager.add(task1);
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.addTask(task1);

        Task task = taskManager.taskSearch(task1.getId());

        assertEquals(task1,task);
    }

}