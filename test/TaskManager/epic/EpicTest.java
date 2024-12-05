package TaskManager.epic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void beforeEach() {
        epic1 = new Epic("стать программистом", "чтобы зарабатывать много денег", 3);
        epic2 = new Epic("стать программистом", "чтобы зарабатывать много денег", 3);
    }

    @Test
    void shouldReturnTrueIfIdsAreEqual() {
        assertEquals(epic1, epic2);
    }


    @Test
    void shouldNotAddEpicAsSubtaskToItself() {
        int subtasksSize = epic1.getSubtasksIds().size();
        epic1.addSubtaskId(3);
        assertEquals(subtasksSize, epic1.getSubtasksIds().size());
    }
}