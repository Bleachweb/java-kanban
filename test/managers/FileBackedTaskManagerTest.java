package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import statuses.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager manager;

    private Task task1;
    private Task task2;
    private Epic epic1;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        manager = new FileBackedTaskManager(tempFile);

        task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        manager.add(task1);
        task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS);
        manager.add(task2);

        epic1 = new Epic("Эпик", "Описание эпика");
        manager.add(epic1);

        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        manager.add(subtask1);
        subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.DONE, epic1.getId());
        manager.add(subtask2);
    }

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpic();

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.getEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.getSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void testSaveMultipleTasks() throws IOException {
        manager.save();

        String content = Files.readString(tempFile.toPath(), StandardCharsets.UTF_8);
        assertFalse(content.isEmpty(), "Файл не должен быть пустым");

        String[] lines = content.split("\n");
        assertEquals(6, lines.length, "Файл должен содержать 6 строк (заголовок + 5 задач)");
    }

    @Test
    void testLoadMultipleTasks() throws IOException {
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getTasks().size(), "Должно быть 2 задачи");
        assertEquals(1, loadedManager.getEpics().size(), "Должен быть 1 эпик");
        assertEquals(2, loadedManager.getSubtasks().size(), "Должно быть 2 подзадачи");

        Task loadedTask1 = loadedManager.taskSearch(task1.getId());
        assertNotNull(loadedTask1, "Задача 1 должна быть загружена");
        assertEquals(task1.getName(), loadedTask1.getName(), "Имя задачи 1 должно совпадать");
        assertEquals(task1.getDescription(), loadedTask1.getDescription(), "Описание задачи 1 должно совпадать");
        assertEquals(task1.getStatus(), loadedTask1.getStatus(), "Статус задачи 1 должен совпадать");

        Epic loadedEpic1 = loadedManager.epicSearch(epic1.getId());
        assertNotNull(loadedEpic1, "Эпик 1 должен быть загружен");
        assertEquals(epic1.getName(), loadedEpic1.getName(), "Имя эпика 1 должно совпадать");
        assertEquals(epic1.getDescription(), loadedEpic1.getDescription(), "Описание эпика 1 должно совпадать");

        Subtask loadedSubtask1 = loadedManager.subtaskSearch(subtask1.getId());
        assertNotNull(loadedSubtask1, "Подзадача 1 должна быть загружена");
        assertEquals(subtask1.getName(), loadedSubtask1.getName(), "Имя подзадачи 1 должно совпадать");
        assertEquals(subtask1.getDescription(), loadedSubtask1.getDescription(), "Описание подзадачи 1 должно совпадать");
        assertEquals(subtask1.getStatus(), loadedSubtask1.getStatus(), "Статус подзадачи 1 должен совпадать");
        assertEquals(subtask1.getEpicId(), loadedSubtask1.getEpicId(), "ID эпика подзадачи 1 должен совпадать");
    }
}