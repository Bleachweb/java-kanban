package tasks;

import statuses.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldReturnTrueIfIdsAreEqual() {
        Task task1 = new Task("посуда", "помыть тарелки", Status.NEW, 1);
        Task task2 = new Task("посуда", "помыть тарелки", Status.NEW, 1);

        assertEquals(task1, task2);
    }

}