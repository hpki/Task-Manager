import management.Managers;
import management.TaskManager;
import servers.HttpTaskServer;
import servers.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}