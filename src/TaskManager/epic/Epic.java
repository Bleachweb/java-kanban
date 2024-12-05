package TaskManager.epic;
import TaskManager.task.Task;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public void addSubtaskId(int subtaskId) {
        if (subtaskId != this.getId()) {
            subtasksIds.add(subtaskId);
        }
    }

    public void removeSubtask(Integer subtaskId) {
        subtasksIds.remove(subtaskId);
    }

    public void removeSubtasks () { subtasksIds.clear();}

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksIds=" + subtasksIds +
                "} " + super.toString();
    }
}
