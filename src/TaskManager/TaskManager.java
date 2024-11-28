package TaskManager;
import TaskManager.epic.Epic;
import TaskManager.subtask.Subtask;
import TaskManager.task.Task;
import java.util.ArrayList;
import java.util.HashMap;

 public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
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

    public Object printTasks() {
        return tasks;
    }

    public Object printSubtasks() {
        return subtasks;
    }

    public Object printEpics() {
       return epics;
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
            subtasksIds.clear();
            setEpicStatus(epic);
        }
    }

    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    public Task taskSearch(int id) {
        Task aTask = null;
        if (tasks.containsKey(id)) {
            aTask = tasks.get(id);
        }
        return aTask;
    }

    public Subtask subtaskSearch(int id) {
        Subtask aSubtask = null;
        if (subtasks.containsKey(id)) {
            aSubtask = subtasks.get(id);
        }
        return aSubtask;
    }

    public Epic epicSearch(int id) {
        Epic anEpic = null;
        if (epics.containsKey(id)) {
            anEpic = epics.get(id);
        }
        return anEpic;
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

    public Object epicSubtasks(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
        ArrayList<Subtask> epicSubtascs = new ArrayList<>();
        for (Integer subtaskId : subtasksIds) {
            epicSubtascs.add(subtasks.get(subtaskId));
        }
        return epicSubtascs;
    }

    public void epicRemove(int id) {
        Epic anEpic = epics.get(id);
        ArrayList<Integer> subtasksForRemove = anEpic.getSubtasksIds();
        for (int subTaskId : subtasksForRemove) {
            subtasks.remove(subTaskId);
        }
        subtasksForRemove.clear();
        epics.remove(id);
    }


    public void update(Task task) {
        int id = 0;
        for (Task aTask : tasks.values()) {
            String name = task.getName();
            String aName = aTask.getName();
            if (name.equals(aName)) {
                id = aTask.getId();
                task.setId(id);
            }
        }
        tasks.put(id, task);
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
        Subtask updatesSubtask = subtasks.get(id);
        updatesSubtask.setName(subtask.getName());
        updatesSubtask.setDescription(subtask.getDescription());
        updatesSubtask.setStatus(subtask.getStatus());
        subtasks.put(id, updatesSubtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        setEpicStatus(epic);
    }

    public void setEpicStatus(Epic epic) {
        ArrayList<Integer> checkForProgress = epic.getSubtasksIds();
        if (checkForProgress.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Status> inProgress = new ArrayList<>();
            ArrayList<Status> done = new ArrayList<>();
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

