package TaskManager;
import TaskManager.epic.Epic;
import TaskManager.subtask.Subtask;
import TaskManager.task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    int nextId = 1;

    public void add(Task task) {
        task.setId(nextId++);
        int id = task.getId();
        tasks.put(id, task);
    }

    public void add(Epic epic) {
        epic.setId(nextId++);
        int id = epic.getId();
        epics.put(id, epic);
        epic.setStatus(Status.NEW);
    }

    public void add(Subtask subtask) {
        subtask.setId(nextId++);
        int id = subtask.getId();
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskIds(id);
        setEpicStatus(epic);
    }

    public List <Task> printTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List <Subtask> printSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List <Epic> printEpics() {
       return new ArrayList<>(epics.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            setEpicStatus(epic);
        }
    }

    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    public Task taskSearch(int id) {
        return tasks.get(id);
    }

    public Subtask subtaskSearch(int id) {
        return subtasks.get(id);
    }

    public Epic epicSearch(int id) {
        return epics.get(id);
    }

    public void taskRemove(int id) {
        tasks.remove(id);
    }

    public void subtaskRemove(int id) {
        Subtask aSubtask = subtasks.get(id);
        int epicId = aSubtask.getEpicId();
        Epic anEpic = epics.get(epicId);
        anEpic.removeSubtask(id);
        subtasks.remove(id);
        setEpicStatus(anEpic);
    }

    public List<Subtask> epicSubtasks(int id) {
        Epic epic = epics.get(id);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        List<Subtask> epicSubtascs = new ArrayList<>();
        for (Integer subtaskId : subtasksIds) {
            epicSubtascs.add(subtasks.get(subtaskId));
        }
        return epicSubtascs;
    }

    public void epicRemove(int id) {
        Epic anEpic = epics.get(id);
        List<Integer> subtasksForRemove = anEpic.getSubtasksIds();
        for (int subTaskId : subtasksForRemove) {
            subtasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    public void update(Task task) {
        int id = task.getId();
        Task updatedTask = tasks.get(id);
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
        tasks.put(id, updatedTask);
    }

    public void update(Epic epic) {
        int id = epic.getId();
        Epic updatedEpic = epics.get(id);
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
        epics.put(id, updatedEpic);
    }

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
}

