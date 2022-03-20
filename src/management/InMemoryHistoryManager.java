package management;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public List<Task> browsingHistory = new ArrayList<>(10); // массив для хранения последних 10-ти задач

    @Override
    public void add(Task task) {
        if (browsingHistory.size() < 10) {
            browsingHistory.add(task);
        } else {
            browsingHistory.remove(0);
            browsingHistory.add(task);
             }
        }

        @Override
        public List<Task> getHistory() {
            return browsingHistory;
        }
    }

