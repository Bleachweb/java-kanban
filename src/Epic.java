import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtaskIds(int subtaskId) {
        subtasksIds.add(subtaskId);
    }

    public void revomeSubtask(Integer subtaskId) {
        subtasksIds.remove(subtaskId);
    }
}
