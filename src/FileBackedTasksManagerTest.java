import management.FileBackedTasksManager;
import management.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

public class FileBackedTasksManagerTest extends TaskManagerTest {
    public FileBackedTasksManagerTest(FileBackedTasksManager fileBackedTasksManager) {
        super(new FileBackedTasksManager("file.scv"));
    }

}
