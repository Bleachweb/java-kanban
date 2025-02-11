package managers;

import statuses.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);

    }

    public boolean checkTime(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
            for (Task taskSave : tasks) {
                if (task.getId() != taskSave.getId()) {
                    if (task.getStartTime().isBefore(taskSave.getEndTime()) && task.getEndTime().isAfter(taskSave.getStartTime())) {
                        return true;
                    }
                }
            }
        return false;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public void add(Task task) {

        try {
            if (task.getStartTime() != null) {
                if (timeValidation(task)) {
                    task.setId(nextId++);
                    int id = task.getId();
                    tasks.put(id, task);
                    addNewPrioritizedTask(task);
                }
            } else {
                task.setId(nextId++);
                int id = task.getId();
                tasks.put(id, task);
            }
        } catch (ManagerValidateException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId++);
        int id = epic.getId();
        epics.put(id, epic);
        epic.setStatus(Status.NEW);
        updateEpicTime(epic);
    }

    @Override
    public void add(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return;
        }
        subtask.setId(nextId++);
        try {
            if (subtask.getStartTime() != null) {
                if (timeValidation(subtask)) {
                    int id = subtask.getId();
                    subtasks.put(id, subtask);
                    int epicId = subtask.getEpicId();
                    Epic epic = epics.get(epicId);
                    epic.addSubtaskId(id);
                    setEpicStatus(epic);
                    updateEpicTime(epic);
                    addNewPrioritizedTask(subtask);
                } else {
                    subtask.setId(0);
                    nextId--;
                }
            } else {
                int id = subtask.getId();
                subtasks.put(id, subtask);
                int epicId = subtask.getEpicId();
                Epic epic = epics.get(epicId);
                epic.addSubtaskId(id);
                setEpicStatus(epic);
                updateEpicTime(epic);
            }
        } catch (ManagerValidateException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            updateEpicTime(epic);
            setEpicStatus(epic);
        }
    }

    @Override
    public void clearEpic() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task taskSearch(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask subtaskSearch(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic epicSearch(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void taskRemove(int id) {
        prioritizedTasks.removeIf(task -> task.getId() == id);
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void subtaskRemove(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        prioritizedTasks.remove(subtask);
        historyManager.remove(subtask.getId());
        Epic epic = epics.get(epicId);
        epic.removeSubtask(id);
        subtasks.remove(id);
        updateEpicTime(epic);
        setEpicStatus(epic);
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
        Epic epic = epics.get(id);
        epic.getSubtasksIds().forEach(subtaskId -> {
            prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subtaskId));
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        });
        epic.removeSubtasks();
        epics.remove(id);
        historyManager.remove(epic.getId());
    }

    @Override
    public void update(Task task) {
        int id = task.getId();
        Task updatedTask = tasks.get(id);
        try {
            if (task.getStartTime() != null && !timeValidation(task)) {
                return;
            }
            if (updatedTask.getStartTime() != null && updatedTask.getStartTime() != task.getStartTime()) {
                prioritizedTasks.remove(updatedTask);
            }
            updatedTask.setName(task.getName());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setStatus(task.getStatus());
            tasks.put(id, updatedTask);
            if (updatedTask.getStartTime() != null) {
                prioritizedTasks.add(updatedTask);
            }
        } catch (ManagerValidateException e) {
            System.out.println("Ошибка валидации: " + e.getMessage() + ". Изменения не внесены");
        }
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
        try {
            if (subtask.getStartTime() != null && !timeValidation(subtask)) {
                return;
            }
            if (updatedSubtask.getStartTime() != null && updatedSubtask.getStartTime() != subtask.getStartTime()) {
                prioritizedTasks.remove(updatedSubtask);
            }
            updatedSubtask.setName(subtask.getName());
            updatedSubtask.setDescription(subtask.getDescription());
            updatedSubtask.setStatus(subtask.getStatus());
            updatedSubtask.setStartTime(subtask.getStartTime());
            updatedSubtask.setDuration(subtask.getDuration());
            subtasks.put(id, updatedSubtask);
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            setEpicStatus(epic);
            updateEpicTime(epic);
            if (updatedSubtask.getStartTime() != null) {
                prioritizedTasks.add(updatedSubtask);
            }
        } catch (ManagerValidateException e) {
            System.out.println("Ошибка валидации: " + e.getMessage() + ". Изменения не внесены");
        }

    }

    public boolean timeValidation(Task task) {
        boolean taskHasIntersections = checkTime(task);
        if (!taskHasIntersections) {
            return true;
        } else {
            throw new ManagerValidateException(
                    "Задача " + task.getName() + " пересекается с ранее добавленной");
        }
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

    private void updateEpicTime(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtasksIds();
        Duration totalDuration = null;
        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                Duration subtaskDuration = subtask.getDuration();
                LocalDateTime subtaskStartTime = subtask.getStartTime();
                LocalDateTime subtaskEndTime = subtask.getEndTime();

                if (subtaskDuration != null) {
                    if (totalDuration == null) {
                        totalDuration = Duration.ZERO;
                    }
                    totalDuration = totalDuration.plus(subtaskDuration);
                }
                if (subtaskStartTime != null) {
                    if (earliestStartTime == null || subtaskStartTime.isBefore(earliestStartTime)) {
                        earliestStartTime = subtaskStartTime;
                    }
                }
                if (subtaskEndTime != null) {
                    if (latestEndTime == null || subtaskEndTime.isAfter(latestEndTime)) {
                        latestEndTime = subtaskEndTime;
                    }
                }
            }
        }
        epic.setDuration(totalDuration);
        epic.setStartTime(earliestStartTime);
        epic.setEndTime(latestEndTime);
    }

}