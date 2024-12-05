package TaskManager;

import TaskManager.epic.Epic;
import TaskManager.subtask.Subtask;
import TaskManager.task.Task;

import java.util.List;

public interface TaskManager {
    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    void clearTasks();

    void clearSubtasks();

    void clearEpic();

    Task taskSearch(int id);

    Subtask subtaskSearch(int id);

    Epic epicSearch(int id);

    void taskRemove(int id);

    void subtaskRemove(int id);

    List<Subtask> epicSubtasks(int id);

    void epicRemove(int id);

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    List<Task> getHistory();
}
