package tasks;

import statuses.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, Status status, int epicId, int id) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId, int id, int duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId, int duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d",
                this.getId(),
                TaskType.SUBTASK,
                this.getName(),
                this.getStatus(),
                this.getDescription(),
                this.getStartTime() != null ? this.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")) : EMPTY_TIME,
                this.getDuration() != null ? this.getDuration().toMinutes() : EMPTY_TIME,
                this.epicId);
    }
}


