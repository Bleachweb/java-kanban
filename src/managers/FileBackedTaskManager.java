package managers;

import statuses.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER_CSV_FILE = "id,type,name,status,description,start time, duration, epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {

        List<String> lines = new ArrayList<>();
        lines.add(HEADER_CSV_FILE); // Заголовок CSV
        for (Task task : getTasks()) {
            lines.add(task.toString());
        }
        for (Epic epic : getEpics()) {
            lines.add(epic.toString());
        }
        for (Subtask subtask : getSubtasks()) {
            lines.add(subtask.toString());
        }
        try {
            Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла", e);
        }
    }

    private Task fromString(String value) {
        final String EMPTY_TIME = "Не указано";
        String[] parts = value.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Некорректный формат строки: " + value);
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            TaskType type = TaskType.valueOf(parts[1].trim());
            String name = parts[2].trim();
            Status status = Status.valueOf(parts[3].trim());
            String description = parts[4].trim();


            switch (type) {
                case TASK:
                    if ((parts.length == 7)) {
                        if (!parts[6].equals(EMPTY_TIME)) {
                            Integer duration = Integer.valueOf(parts[6]);
                            LocalDateTime startTime = LocalDateTime.parse(parts[5],
                                    DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));
                            return new Task(name, description, status, id, duration, startTime);
                        }
                    }
                    return new Task(name, description, status, id);
                case EPIC:
                    if ((parts.length == 7)) {
                        if (!parts[6].equals(EMPTY_TIME)) {
                            Integer duration = Integer.valueOf(parts[6]);
                            LocalDateTime startTime = LocalDateTime.parse(parts[5],
                                    DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));
                            return new Epic(name, description, id, duration, startTime);
                        }
                    }
                    return new Epic(name, description, id);
                case SUBTASK:
                    if (parts.length < 8) {
                        throw new IllegalArgumentException("Для подзадачи отсутствует epicId: " + value);
                    }
                    if ((parts.length == 8)) {
                        if (!parts[6].equals(EMPTY_TIME)) {
                            Integer duration = Integer.valueOf(parts[6]);
                            LocalDateTime startTime = LocalDateTime.parse(parts[5],
                                    DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));
                            int epicId = Integer.parseInt(parts[7].trim());
                            return new Subtask(name, description, status, epicId, id, duration, startTime);
                        } else {
                            int epicId = Integer.parseInt(parts[7].trim());
                            return new Subtask(name, description, status, epicId, id);
                        }
                    }
                    int epicId = Integer.parseInt(parts[7].trim());
                    return new Subtask(name, description, status, epicId, id);
                default:
                    throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный формат числа в строке: " + value, e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректные данные в строке: " + value, e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task task = manager.fromString(line);
                int oldId = task.getId();

                if (task instanceof Epic) {
                    manager.add((Epic) task);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    manager.add(subtask);
                    Epic epic = manager.epicSearch(subtask.getEpicId());
                    if (epic != null) {
                        epic.addSubtaskId(subtask.getId());
                    }
                } else {
                    manager.add(task);
                }
                task.setId(oldId);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла", e);
        }
        return manager;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }


    @Override
    public void taskRemove(int id) {
        super.taskRemove(id);
        save();
    }

    @Override
    public void subtaskRemove(int id) {
        super.subtaskRemove(id);
        save();
    }

    @Override
    public void epicRemove(int id) {
        super.epicRemove(id);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }


    public static void main(String[] args) {

        try {
            // Создаем файл CSV и сохраняем в него задачи
            File tempFile = File.createTempFile("tasks", ".csv");
            FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);
            System.out.println("Временный файл создан: " + tempFile.getAbsolutePath());

            //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
            Task task1 = new Task("посуда", "помыть тарелки", Status.NEW);
            taskManager.add(task1);
            Task task2 = new Task("уборка", "помыть полы", Status.NEW, 30,
                    LocalDateTime.now().plusHours(1));
            taskManager.add(task2);

            Epic epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег");
            taskManager.add(epic1);

            Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.IN_PROGRESS, 3,
                    30, LocalDateTime.now().plusHours(2));
            taskManager.add(subtask1);
            Subtask subtask2 = new Subtask("собеседование", "пройти и его", Status.NEW, 3,
                    30, LocalDateTime.now().plusHours(3));
            taskManager.add(subtask2);
            Subtask subtask3 = new Subtask("7 спринт", "успешно сдать", Status.IN_PROGRESS, 3,
                    20, LocalDateTime.now().plusHours(4));
            taskManager.add(subtask3);

            Epic epic2 = new Epic("купить квартиру", "когда заработаешь много денег");
            taskManager.add(epic2);

            // Загружаем задачи из файла
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

            // Проверяем, что задачи загрузились корректно
            System.out.println("Задачи после загрузки:");
            System.out.println(HEADER_CSV_FILE);
            for (Task task : loadedManager.getTasks()) {
                System.out.println(task);
            }
            for (Epic epic : loadedManager.getEpics()) {
                System.out.println(epic);
            }
            for (Subtask subtask : loadedManager.getSubtasks()) {
                System.out.println(subtask);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при работе с временным файлом: " + e.getMessage());
        }
    }
}

