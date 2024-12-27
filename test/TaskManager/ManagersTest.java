package TaskManager;

import org.junit.jupiter.api.Test;
import TaskManager.managers.InMemoryHistoryManager;
import TaskManager.managers.Managers;
import TaskManager.managers.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager manager1 = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();

        assertNotNull(manager1);
        assertNotNull(manager2);
        assertNotEquals(manager1, manager2);
    }

    @Test
    void getDefaultHistory() {
        InMemoryHistoryManager manager1 = Managers.getDefaultHistory();
        InMemoryHistoryManager manager2 = Managers.getDefaultHistory();

        assertNotNull(manager1);
        assertNotNull(manager2);
        assertNotEquals(manager1, manager2);
    }
}