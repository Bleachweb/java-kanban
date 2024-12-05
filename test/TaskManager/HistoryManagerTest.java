package TaskManager;

import TaskManager.epic.Epic;
import TaskManager.subtask.Subtask;
import TaskManager.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private TaskManager taskManager;
    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task1 = new Task("посуда", "помыть тарелки", Status.NEW,1);
        Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег", 3);
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,3,4);
        inMemoryHistoryManager.addTask(task1);
        inMemoryHistoryManager.addTask(epic1);
        inMemoryHistoryManager.addTask(subtask1);
        assertEquals(3, inMemoryHistoryManager.getHistory().size());
        assertTrue(inMemoryHistoryManager.getHistory().contains(task1));
        assertTrue(inMemoryHistoryManager.getHistory().contains(epic1));
        assertTrue(inMemoryHistoryManager.getHistory().contains(subtask1));
    }

    @Test
    void shouldRemoveFirstTaskWhenAddNewTaskAndListAreFull() {
        Task task11 = new Task("Задача 11", "проверка", Status.NEW);
        for (int i = 0; i < 10; i++) {
            inMemoryHistoryManager.addTask(new Task("Задача " + i, "проверка", Status.NEW));
        }
        inMemoryHistoryManager.addTask(task11);

        assertEquals(10, inMemoryHistoryManager.getHistory().size());
        assertEquals(task11, inMemoryHistoryManager.getHistory().get(9));

    }
}