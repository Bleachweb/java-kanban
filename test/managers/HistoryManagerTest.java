package managers;

import statuses.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
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
        task2 = new Task("1", "Обновление", 1);
        task3 = new Task("2", "Проверка", 2);
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task = new Task("посуда", "помыть тарелки", Status.NEW, 1);
        Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег", 3);
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW, 3, 4);

        historyManager.add(task);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        assertEquals(3, historyManager.getHistory().size());
        assertTrue(historyManager.getHistory().contains(task));
        assertTrue(historyManager.getHistory().contains(epic1));
        assertTrue(historyManager.getHistory().contains(subtask1));
    }

//    @Test
//    public void shouldCheckTaskUpdateById() {
//        // Создаем и добавляем новую задачу
//
//        historyManager.add(task1);
//
//        assertEquals("Описание", historyManager.getTask().get(1).task.getDescription());
//
//        historyManager.updateTask(task2);
//
//        assertEquals("Обновление", historyManager.getTaskMap().get(1).task.getDescription());
//
//        historyManager.updateTask(task3);
//
//        assertEquals("Обновление", historyManager.getTaskMap().get(1).task.getDescription());
//    }

    @Test
    void shouldCheckTaskAddAndDoubles() {
        Task task1 = new Task("1", "Описание1", 1);
        Task task2 = new Task("2", "Описание2", 2);
        Task task3 = new Task("1", "Описание", 1);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        Task check1 = historyManager.getHistory().getFirst();
        Task check2 = historyManager.getHistory().getLast();

        assertNotNull(check1);
        assertNotNull(check2);
        assertEquals(2, historyManager.getHistory().size());
        assertEquals("1", check2.getName());
    }

    @Test
    public void testRemoveTaskFromEmptyList() {
        historyManager.remove(1);
    }

    @Test
    public void testRemoveExistingTask() {
        historyManager.add(task1);
        historyManager.add(task3);

        historyManager.remove(1);

        assertFalse(historyManager.getHistory().contains(task1), "Задача с id 1 должна быть удалена");
        assertTrue(historyManager.getHistory().contains(task3), "Задача с id 2 должна остаться в taskMap");
    }

    @Test
    public void testRemoveTaskThatDoesNotExist() {
        historyManager.add(task1);

        historyManager.remove(2);

        assertTrue(historyManager.getHistory().contains(task1), "Задача с id 1 должна остаться в taskMap");
    }

    @Test
    public void testRemoveHeadTask() {
        historyManager.add(task1);
        historyManager.add(new Task("3", "Описание", 3));
        historyManager.add(task3);

        historyManager.remove(1);

        assertFalse(historyManager.getHistory().contains(task1), "Задача с id 1 должна быть удалена");
        assertTrue(historyManager.getHistory().contains(task3), "Задача с id 2 должна остаться в taskMap");
        assertEquals(2, historyManager.getHistory().size(), "Задачи id 2,3 должны остаться в taskMap");
    }

}