import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int nextId = 1;

    public void add(Task task) {
        task.id = nextId++;
        tasks.put(task.id, task);
    }

    public void add(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.id, epic);
        epic.status = Status.NEW;
    }

    public void add(Subtask subtask, int epicId) {
        subtask.id = nextId++;
        subtasks.put(subtask.id, subtask);
        subtask.epicId = epicId;
        Epic epic = epics.get(epicId);
        epic.addSubtaskIds(subtask.id);
        setEpicStatus(epic);
    }

    public void printTasks() {
        System.out.println(tasks);
    }

    public void printSubtasks() {
        System.out.println(subtasks);
    }

    public void printEpics() {
        System.out.println(epics);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.subtasksIds.clear();
            setEpicStatus(epic);
        }
    }

    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    public Task taskSearch(int id) {
        Task aTask = new Task("", "");
        if (tasks.containsKey(id)) {
            aTask = tasks.get(id);
        }
        return aTask;
    }

    public Subtask subtaskSearch(int id) {
        Subtask aSubtask = new Subtask("", "", Status.NEW);
        if (subtasks.containsKey(id)) {
            aSubtask = subtasks.get(id);
        }
        return aSubtask;
    }

    public Epic epicSearch(int id) {
        Epic anEpic = new Epic("", "");
        if (epics.containsKey(id)) {
            anEpic = epics.get(id);
        }
        return anEpic;
    }

    public void taskRemove(int id) {
        int idForRemove = 0;
        if (tasks.containsKey(id)) {
            idForRemove = id;
        }
        tasks.remove(idForRemove);
    }

    public void subtaskRemove(int id) {
        Subtask aSubtask = subtasks.get(id);
        Epic anEpic = epics.get(aSubtask.epicId);
        anEpic.revomeSubtask(id);
        subtasks.remove(id);
        setEpicStatus(anEpic);
    }

    public void epicSubtasks(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtasksIds = epic.subtasksIds;
        ArrayList<Subtask> epicSubtascs = new ArrayList<>();
        for (Integer subtaskId : subtasksIds) {
            epicSubtascs.add(subtasks.get(subtaskId));
        }
        System.out.println(epicSubtascs);
    }

    public void epicRemove(int id) {
        int idForRemove = 0;
        if (epics.containsKey(id)) {
            idForRemove = id;
            Epic anEpic = epics.get(id);
            ArrayList<Integer> subtasksForRemove = anEpic.subtasksIds;
            for (int subTaskId : subtasksForRemove) {
                subtasks.remove(subTaskId);
            }
            subtasksForRemove.clear();
            epics.remove(idForRemove);
        }
    }

    public void update(Task task) {
        for (Task aTask : tasks.values()) {
            if (task.name.equals(aTask.name)) {
                task.id = aTask.id;
            }
        }
        tasks.put(task.id, task);
    }

    public void update(Epic epic) {
        for (Epic anEpic : epics.values()) {
            if (epic.name.equals(anEpic.name)) {
                epic.id = anEpic.id;
                epic.status = anEpic.status;
                epic.subtasksIds = anEpic.subtasksIds;
            }
        }
        epics.put(epic.id, epic);
    }
    public void update(Subtask subtask) {
        for (Subtask aSubtask : subtasks.values()) {
            if (subtask.name.equals(aSubtask.name)) {
                subtask.id = aSubtask.id;
                subtask.epicId = aSubtask.epicId;
            }
        }
        subtasks.put(subtask.id, subtask);
        Epic epic = epics.get(subtask.epicId);
        setEpicStatus(epic);
    }

    public void setEpicStatus(Epic epic) {
        ArrayList<Integer> checkForProgress = epic.subtasksIds;
        if (checkForProgress.isEmpty()) {
            epic.status = Status.NEW;
        } else {
            ArrayList<Status> inProgress = new ArrayList<>();
            ArrayList<Status> done = new ArrayList<>();
            for (Integer subtaskId : checkForProgress) {
                Subtask aSubtask = subtasks.get(subtaskId);
                if (aSubtask.status.equals(Status.IN_PROGRESS)) {
                    inProgress.add(aSubtask.status);
                } else if (aSubtask.status.equals(Status.DONE)) {
                    done.add(Status.DONE);
                }
            }
            if (inProgress.isEmpty() && done.isEmpty()) {
                epic.status = Status.NEW;
            } else if (done.size() == checkForProgress.size()) {
                epic.status = Status.DONE;
            } else {
                epic.status = Status.IN_PROGRESS;
            }
        }
    }
}

