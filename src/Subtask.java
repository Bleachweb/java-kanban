import java.util.Objects;

public class Subtask extends Task {
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    private int  epicId;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }
}



