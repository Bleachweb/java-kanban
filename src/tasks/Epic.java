package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id, int duration, LocalDateTime startTime) {
        super(name, description, id, duration, startTime);
    }

    public List<Integer> getSubtasksIds() {
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

    public void removeSubtasks() {
        subtasksIds.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                this.getId(),
                TaskType.EPIC,
                this.getName(),
                this.getStatus(),
                this.getDescription(),
                this.getStartTime() != null ? this.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")) : EMPTY_TIME,
                this.getDuration() == null ? EMPTY_TIME : this.getDuration().toMinutes());
    }
}
