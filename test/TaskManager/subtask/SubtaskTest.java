package TaskManager.subtask;

import TaskManager.Status;
import TaskManager.TaskManager;
import TaskManager.Managers;
import TaskManager.epic.Epic;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    TaskManager taskManager = Managers.getDefault();
    @Test
    void shouldReturnTrueIfIdsAreEqual() {
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,3,4);
        Subtask subtask2 = new Subtask("практикум", "пройти обучение", Status.NEW,3,4);
        assertEquals(subtask1, subtask2);
    }

    @Test
    void shouldNotAddSubtaskAsEpicToItself() {
        Epic epic = new Epic("стать программистом", "чтобы зарабатывать много денег");
        taskManager.add(epic);
        List<Subtask> check1 = taskManager.getSubtasks();
        Subtask subtask1 = new Subtask("практикум", "пройти обучение", Status.NEW,2);
        taskManager.add(subtask1);
        List<Subtask> check2 = taskManager.getSubtasks();
        assertEquals(check1.size(), check2.size());
    }
}