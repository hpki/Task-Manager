package servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import management.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class Handler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    HttpResponse<String> response;
    HttpExchange exchange;
    String[] splittedPath;
    TaskManager manager;
    String path;
    String body;
    Gson gson;

    public Handler(final TaskManager manager) {
        this.manager = manager;
    }

    private static boolean isPositiveNumber(String str) throws NumberFormatException {
        try {
            return Integer.parseInt(str) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void handle(HttpExchange exchange) {
        gson = new Gson();
        this.exchange = exchange;
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        this.path = requestURI.toString().toLowerCase();
        splittedPath = path.split("/");
        InputStream input = exchange.getRequestBody();
        try {
            body = new String(input.readAllBytes(), DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            switch (method) {
                case "GET":
                    getHandler();
                    break;

                case "POST":
                    postHandler();
                    break;

                case "DELETE":
                    deleteHandler();
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        } catch (IOException e) {
            try {
                exchange.sendResponseHeaders(405, 0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void getHandler() throws IOException {
        if (path.equals("/tasks")) {
            exchange.sendResponseHeaders(200, 0);
            gson = new GsonBuilder().create();
            String allTasks = gson.toJson(manager.getAllTasks());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(allTasks.getBytes());
            }
        } else {
            switch (splittedPath[2].toLowerCase()) {

                case "task":
                    taskGetHandle();
                    break;

                case "subtask":
                    subtaskGetHandle();
                    break;

                case "epic":
                    epicGetHandle();
                    break;

                case "history":
                    exchange.sendResponseHeaders(200, 0);
                    List<Task> history = manager.history();
                    gson = new GsonBuilder().create();
                    String historyJson = gson.toJson(history);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(historyJson.getBytes());
                        exchange.close();
                    }
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
        }
    }

    void deleteHandler() throws IOException {
        switch (splittedPath[2]) {

            case "task":
                taskDeleteHandle();
                break;

            case "subtask":
                subtaskDeleteHandle();
                break;

            case "epic":
                epicDeleteHandle();
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
        }
    }

    void postHandler() throws IOException {
        switch (splittedPath[2].toLowerCase()) {
            case "task":
                taskPostHandler();
                break;

            case "subtask":
                subtaskPostHandle();
                break;

            case "epic":
                epicPostHandle();
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
        }
    }

    void taskGetHandle() throws IOException {
        GsonBuilder gbuilder = new GsonBuilder();
        gson = gbuilder.create();
        if (splittedPath.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            String jsonStr = gson.toJson(manager.getTaskList());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonStr.getBytes());
                exchange.close();
            }
        } else if (splittedPath.length == 4 && splittedPath[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(splittedPath[3]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                long id = Long.parseLong(sb.toString());
                if (manager.getMapTaskList().containsKey(id)) {
                    exchange.sendResponseHeaders(200, 0);
                    Task task = manager.getTask(id);
                    String taskSerialized = gson.toJson(task);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(taskSerialized.getBytes());
                        exchange.close();
                    }
                } else exchange.sendResponseHeaders(405, 0);
            } else exchange.sendResponseHeaders(405, 0);
        } else exchange.sendResponseHeaders(400, 0);
    }

    void taskPostHandler() throws IOException {
        exchange.sendResponseHeaders(200, 0);
        Task task = gson.fromJson(body, Task.class);
        manager.createTask(task);
        manager.updateTask(task);
    }

    void taskDeleteHandle() throws IOException {
        if (splittedPath.length == 4 && splittedPath[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(splittedPath[3]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                long id = Long.parseLong(sb.toString());
                manager.remove(id);
            } else exchange.sendResponseHeaders(400, 0);
        } else if (splittedPath.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            manager.clearTaskList();
        } else
            exchange.sendResponseHeaders(400, 0);
    }

    void subtaskPostHandle() throws IOException {
        exchange.sendResponseHeaders(200, 0);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        manager.createSubtask(subtask);
        manager.updateSubtask(subtask);
    }

    void epicPostHandle() throws IOException {
        exchange.sendResponseHeaders(200, 0);
        Epic epic = gson.fromJson(body, Epic.class);
        manager.createEpic(epic);
        manager.updateTask(epic);
    }

    void subtaskGetHandle() throws IOException {
        gson = new GsonBuilder().create();
        if (splittedPath.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            String jsonStr = gson.toJson(manager.getSubtaskList());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonStr.getBytes());
                exchange.close();
            }

        } else if (splittedPath.length == 4 && splittedPath[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(splittedPath[3]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                long id = Long.parseLong(sb.toString());
                if (manager.getMapSubtaskList().containsKey(id)) {
                    exchange.sendResponseHeaders(200, 0);
                    Task task = manager.getSubtask(id);
                    String taskSerialized = gson.toJson(task);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(taskSerialized.getBytes());
                        exchange.close();
                    }
                } else exchange.sendResponseHeaders(405, 0);
            } else exchange.sendResponseHeaders(405, 0);
        } else if (splittedPath.length == 5 && splittedPath[4].startsWith("?id=") && splittedPath[3].equals("epic")) {
            StringBuilder sb = new StringBuilder(splittedPath[4]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                String jsonStr = gson.toJson(manager.getListOfSubtask(manager.getEpic(Long.parseLong(sb.toString()))));
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(jsonStr.getBytes());
                    exchange.close();
                }
            } else {
                exchange.sendResponseHeaders(400, 0);
            }
        }
    }

    void subtaskDeleteHandle() throws IOException {
        if (splittedPath.length == 4 && splittedPath[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(splittedPath[3]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                long id = Long.parseLong(sb.toString());
                manager.remove(id);

            } else exchange.sendResponseHeaders(400, 0);
        } else if (splittedPath.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            manager.clearSubtaskList();
        } else
            exchange.sendResponseHeaders(400, 0);
    }

    void epicGetHandle() throws IOException {
        gson = new GsonBuilder().create();
        if (splittedPath.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            String jsonStr = gson.toJson(manager.getEpicList());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonStr.getBytes());
                exchange.close();
            }
        } else if (splittedPath.length == 4 && splittedPath[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(splittedPath[3]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                long id = Long.parseLong(sb.toString());
                if (manager.getMapEpicList().containsKey(id)) {
                    exchange.sendResponseHeaders(200, 0);
                    Task task = manager.getEpic(id);
                    String taskSerialized = gson.toJson(task);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(taskSerialized.getBytes());
                        exchange.close();
                    }
                } else exchange.sendResponseHeaders(405, 0);
            } else exchange.sendResponseHeaders(405, 0);
        }
    }

    void epicDeleteHandle() throws IOException {
        if (splittedPath.length == 4 && splittedPath[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(splittedPath[3]);
            sb.delete(0, 4);
            if (isPositiveNumber(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                long id = Long.parseLong(sb.toString());
                manager.remove(id);

            } else exchange.sendResponseHeaders(400, 0);
        } else if (splittedPath.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            manager.clearEpicList();
        } else
            exchange.sendResponseHeaders(400, 0);
    }

    long extractId(String thirdElementOfPath) {
        String[] thirdElement = thirdElementOfPath.split("\\?id=");
        if (thirdElement[0].equals("") && isPositiveNumber(thirdElement[2])) {
            return Long.parseLong(thirdElement[2]);
        } else return -1;
    }
}

