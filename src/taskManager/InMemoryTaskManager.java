package taskManager;
import taskManager.tasks.Epic;
import taskManager.tasks.Subtask;
import taskManager.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void add(Task task) {
        task.setId(nextId++);
        int id = task.getId();
        tasks.put(id, task);
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId++);
        int id = epic.getId();
        epics.put(id, epic);
        epic.setStatus(Status.NEW);
    }

    @Override
    public void add(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return;
        }
        subtask.setId(nextId++);
        int id = subtask.getId();
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(id);
        setEpicStatus(epic);
    }

    @Override
    public List <Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List <Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List <Epic> getEpics() {
       return new ArrayList<>(epics.values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            setEpicStatus(epic);
        }
    }

    @Override
    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task taskSearch(int id) {
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Subtask subtaskSearch(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }

    @Override
    public Epic epicSearch(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public void taskRemove(int id) {
        tasks.remove(id);
    }

    @Override
    public void subtaskRemove(int id) {
        Subtask aSubtask = subtasks.get(id);
        int epicId = aSubtask.getEpicId();
        Epic anEpic = epics.get(epicId);
        anEpic.removeSubtask(id);
        subtasks.remove(id);
        setEpicStatus(anEpic);
    }

    @Override
    public List<Subtask> epicSubtasks(int id) {
        Epic epic = epics.get(id);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        List<Subtask> epicSubtascs = new ArrayList<>();
        for (Integer subtaskId : subtasksIds) {
            epicSubtascs.add(subtasks.get(subtaskId));
        }
        return epicSubtascs;
    }

    @Override
    public void epicRemove(int id) {
        Epic anEpic = epics.get(id);
        List<Integer> subtasksForRemove = anEpic.getSubtasksIds();
        for (int subTaskId : subtasksForRemove) {
            subtasks.remove(subTaskId);
        }
        anEpic.removeSubtasks();
        epics.remove(id);
    }

    @Override
    public void update(Task task) {
        int id = task.getId();
        Task updatedTask = tasks.get(id);
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
        tasks.put(id, updatedTask);
    }

    @Override
    public void update(Epic epic) {
        int id = epic.getId();
        Epic updatedEpic = epics.get(id);
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
        epics.put(id, updatedEpic);
    }

    @Override
    public void update(Subtask subtask) {
        int id = subtask.getId();
        Subtask updatedSubtask = subtasks.get(id);
        updatedSubtask.setName(subtask.getName());
        updatedSubtask.setDescription(subtask.getDescription());
        updatedSubtask.setStatus(subtask.getStatus());
        subtasks.put(id, updatedSubtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        setEpicStatus(epic);
    }

    private void setEpicStatus(Epic epic) {
        List<Integer> checkForProgress = epic.getSubtasksIds();
        if (checkForProgress.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            List<Status> inProgress = new ArrayList<>();
            List<Status> done = new ArrayList<>();
            for (Integer subtaskId : checkForProgress) {
                Subtask aSubtask = subtasks.get(subtaskId);
                Status status = aSubtask.getStatus();
                if (status.equals(Status.IN_PROGRESS)) {
                    inProgress.add(status);
                } else if (status.equals(Status.DONE)) {
                    done.add(Status.DONE);
                }
            }
            if (inProgress.isEmpty() && done.isEmpty()) {
                epic.setStatus(Status.NEW);
            } else if (done.size() == checkForProgress.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

