package taskManager.managers;
import taskManager.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LENGTH = 10;
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (historyList.size() == HISTORY_LENGTH) {
            historyList.removeFirst();
        }
        historyList.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}