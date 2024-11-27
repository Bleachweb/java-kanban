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

    public void add(Subtask subtask, int epicId) {
        subtask.setId(nextId++);
        int id = subtask.getId();
        subtasks.put(id, subtask);
        subtask.setEpicId(epicId);
        Epic epic = epics.get(epicId);
        epic.addSubtaskIds(id);
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
        int epicId = aSubtask.getEpicId();
        Epic anEpic = epics.get(epicId);
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
        int id = 0;
        for (Epic anEpic : epics.values()) {
            String name = epic.getName();
            String aName = anEpic.getName();
            if (name.equals(aName)) {
                id = anEpic.getId();
                epic.setId(id);
                epic.setStatus(anEpic.getStatus());
                epic.subtasksIds = anEpic.subtasksIds;
            }
        }
        epics.put(id, epic);
    }
    public void update(Subtask subtask) {
        int id = 0;
        for (Subtask aSubtask : subtasks.values()) {
            String name = subtask.getName();
            String aName = aSubtask.getName();
            if (name.equals(aName)) {
                id = aSubtask.getId();
                subtask.setId(id);
                subtask.setEpicId(aSubtask.getEpicId());
            }
        }
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        setEpicStatus(epic);
    }

    public void setEpicStatus(Epic epic) {
        ArrayList<Integer> checkForProgress = epic.subtasksIds;
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

