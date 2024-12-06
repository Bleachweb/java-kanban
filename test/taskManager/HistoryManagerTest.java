package taskManager;

import taskManager.tasks.Epic;
import taskManager.tasks.Subtask;
import taskManager.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private TaskManager taskManager;
    private HistoryManager HistoryManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        HistoryManager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task1 = new Task("посуда", "помыть тарелки", Status.NEW,1);
        Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег", 3);
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,3,4);

        HistoryManager.addTask(task1);
        HistoryManager.addTask(epic1);
        HistoryManager.addTask(subtask1);

        assertEquals(3, HistoryManager.getHistory().size());
        assertTrue(HistoryManager.getHistory().contains(task1));
        assertTrue(HistoryManager.getHistory().contains(epic1));
        assertTrue(HistoryManager.getHistory().contains(subtask1));
    }

    @Test
    void shouldRemoveFirstTaskWhenAddNewTaskAndListAreFull() {
        Task task11 = new Task("Задача 11", "проверка", Status.NEW);
        for (int i = 0; i < 10; i++) {
            HistoryManager.addTask(new Task("Задача " + i, "проверка", Status.NEW));
        }

        HistoryManager.addTask(task11);

        assertEquals(10, HistoryManager.getHistory().size());
        assertEquals(task11, HistoryManager.getHistory().get(9));

    }
}