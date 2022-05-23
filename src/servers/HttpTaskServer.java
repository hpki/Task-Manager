package servers;

import com.sun.net.httpserver.HttpServer;
import management.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    static private final int PORT = 8080;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new Handler(manager));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }
}
