package management;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void removeInHistory(long id);

    List<Task> getHistory();

    int getSize();

}
