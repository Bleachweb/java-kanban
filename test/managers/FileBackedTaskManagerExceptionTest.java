package managers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerExceptionTest {

    @Test
    public void testFileNotFoundException() {
        File invalidFile = new File("invalid/path/to/file.csv");

        assertThrows(RuntimeException.class, () -> FileBackedTaskManager.loadFromFile(invalidFile));
    }

}