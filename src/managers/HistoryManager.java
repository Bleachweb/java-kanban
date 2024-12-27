package managers;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

public interface HistoryManager {

    void addTask(Task task);
    void remove(int id);

    List<Task> getHistory();

    HashMap<Integer, Node> getTaskMap();

    void updateTask (Task task);
}