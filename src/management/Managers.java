package management;
import servers.HTTPTaskManager;
import java.net.URI;
import java.net.URISyntaxException;


    public class Managers {

        public static TaskManager getDefault() throws URISyntaxException {
            return new HTTPTaskManager(new URI("http://localhost:8078/"));
        }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }

}
