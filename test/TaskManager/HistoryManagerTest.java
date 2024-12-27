package TaskManager;

import TaskManager.managers.HistoryManager;
import TaskManager.managers.Managers;
import TaskManager.managers.Node;
import TaskManager.tasks.Epic;
import TaskManager.tasks.Subtask;
import TaskManager.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {
    private HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("1", "Описание", 1);
        task2 = new Task("1","Обновление", 1);
        task3 = new Task("2","Проверка", 2);
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task = new Task("посуда", "помыть тарелки", Status.NEW,1);
        Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег", 3);
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,3,4);

        historyManager.addTask(task);
        historyManager.addTask(epic1);
        historyManager.addTask(subtask1);

        assertEquals(3, historyManager.getHistory().size());
        assertTrue(historyManager.getHistory().contains(task));
        assertTrue(historyManager.getHistory().contains(epic1));
        assertTrue(historyManager.getHistory().contains(subtask1));
    }

//    @Test
//    void shouldRemoveFirstTaskWhenAddNewTaskAndListAreFull() {
//        Task task11 = new Task("Задача 11", "проверка", Status.NEW);
//        for (int i = 0; i < 10; i++) {
//            historyManager.addTask(new Task("Задача " + i, "проверка", Status.NEW));
//        }
//
//        historyManager.addTask(task11);
//
//        assertEquals(10, historyManager.getHistory().size());
//        assertEquals(task11, historyManager.getHistory().get(9));
//
//    } этот тест больше не нужен, тк у нас не ограничена история просмотров

    @Test
    public void shouldCheckTaskUpdateById() {
        // Создаем и добавляем новую задачу

        historyManager.addTask(task1);

        assertEquals("Описание", historyManager.getTaskMap().get(1).task.getDescription());

        historyManager.updateTask(task2);

        assertEquals("Обновление", historyManager.getTaskMap().get(1).task.getDescription());

        historyManager.updateTask(task3);

        assertEquals("Обновление", historyManager.getTaskMap().get(1).task.getDescription());
    }

    @Test
    void shouldCheckTaskAddAndDoubles() {
        Task task1 = new Task("1", "Описание1", 1);
        Task task2 = new Task("2", "Описание2", 2);
        Task task3 = new Task("1", "Описание", 1);

        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);

        Node node = historyManager.getTaskMap().get(task1.getId());
        assertNotNull(node);
        assertEquals(2,historyManager.getTaskMap().size());
        assertEquals("1", node.task.getName());
    }

    @Test
    public void testRemoveTaskFromEmptyList() {
        historyManager.remove(1);
    }

    @Test
    public void testRemoveExistingTask() {
        historyManager.addTask(task1);
        historyManager.addTask(task3);

        historyManager.remove(1);

        assertNull(historyManager.getTaskMap().get(1), "Задача с id 1 должна быть удалена");
        assertNotNull(historyManager.getTaskMap().get(2), "Задача с id 2 должна остаться в taskMap");
    }

    @Test
    public void testRemoveTaskThatDoesNotExist() {
        historyManager.addTask(task1);

        historyManager.remove(2); // Задача с id = 2 не существует

        assertNotNull(historyManager.getTaskMap().get(1), "Задача с id 1 должна остаться в taskMap");
    }

    @Test
    public void testRemoveHeadTask() {
        historyManager.addTask(task1);
        historyManager.addTask(new Task("3","Описание", 3));
        historyManager.addTask(task3);

        historyManager.remove(1);

        assertNull(historyManager.getTaskMap().get(1), "Задача с id 1 должна быть удалена");
        assertNotNull(historyManager.getTaskMap().get(2), "Задача с id 2 должна остаться в taskMap");
        assertEquals(2,historyManager.getTaskMap().size(), "Задачи id 2,3 должны остаться в taskMap");
    }

}