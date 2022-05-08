import management.HistoryManager;
import management.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest extends InMemoryHistoryManager {
    private HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void addTest() {
        Task task = new Task("Test addTask", "Test addTask description");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        Task testTask = historyManager.getHistory().get(0);
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void removeInHistory() {
        Task task = new Task("Test addTask", "Test addTask description");
        add(task);
        long id = task.getId();
        historyManager.removeInHistory(id);
        List<Task> list = historyManager.getHistory();
        assertNotNull(list, "Задачи на возвращаются.");
    }
}
