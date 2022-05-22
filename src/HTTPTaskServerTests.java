import com.google.gson.Gson;
import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import servers.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskServerTests {
    static KVServer kvServer;
    HttpTaskServer httpTaskServer;
    static TaskManager manager;
    HttpClient client = HttpClient.newHttpClient();

    static Task task;
    static Epic epic1;
    static Subtask subtask1;
    static Subtask subtask2;
    static Epic epic2;
    static Subtask subtask3;

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }


    @AfterEach
    void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void getTasksWhenEmptyList() {
        String body = sendGetRquest("tasks/task/").body();
        assertEquals(body, "[]");
    }

    @Test
    void getTasksWhenNotEmptyList() {
        String taskJson = "[{\"name\":\"name\",\"description\":\"description\",\"status\":\"NEW\"," +
                "\"duration\":{\"seconds\":5000,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2025,\"month\":5," +
                "\"day\":13},\"time\":{\"hour\":10,\"minute\":0,\"second\":0,\"nano\":0}}}]";

        sendPostRequest("task", task);
        assertEquals(sendGetRquest("tasks/task").body(), taskJson);
    }

    @Test
    void getSubtasksWhenEmptyList() {
        assertEquals(sendGetRquest("tasks/subtask/").body(), "[]");
    }

    @Test
    void getSubtasksWhenNotEmptyList() {
        Gson gson = new Gson();
        sendPostRequest("subtask", subtask1);
        String subtaskJson = "[" + gson.toJson(manager.getSubtask(1)) + "]";
        assertEquals(sendGetRquest("tasks/subtask/").body(), subtaskJson);
    }

    @Test
    void getEpicsWhenEmptyList() {
        assertEquals(sendGetRquest("tasks/subtask/").body(), "[]");
    }

    @Test
    void getEpicsTaskWhenNotEmptyList() {
        Gson gson = new Gson();
        sendPostRequest("epic", epic1);
        String subtaskJson = "[" + gson.toJson(manager.getEpic(1)) + "]";
        assertEquals(sendGetRquest("tasks/epic/").body(), subtaskJson);
    }

    @Test
    void getTasksByIdWhenEmptyList() {
        assertEquals(sendGetRquest("tasks/task/?id=1").body(), "");
    }

    @Test
    void getTasksByIdWhenNotEmptyList() {
        sendPostRequest("task", task);
        String string = sendGetRquest("tasks/task/?id=1").body();
        String testString = "{\"name\":\"name\",\"description\":\"description\",\"status\":\"NEW\"," +
                "\"duration\":{\"seconds\":15000,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2026,\"month\":5," +
                "\"day\":13},\"time\":{\"hour\":10,\"minute\":0,\"second\":0,\"nano\":0}}}";
        assertEquals(string, testString);

    }

    @Test
    void getSubtasksByIdWhenEmptyList() {
        assertEquals(sendGetRquest("tasks/subtask/?id=1").body(), "");
    }

    @Test
    void getSubtasksByIdWhenNotEmptyList() {
        sendPostRequest("subtask", subtask1);
        String string = sendGetRquest("tasks/subtask/?id=1").body();
        String testString = "{\"epicId\":0,\"name\":\"subtask1Name\",\"description\":\"subtask1Description\" " +
                "\"status\":\"NEW\",\"duration\":{\"seconds\":3600,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2025,\"month\":5,\"day\":13},\"time\":{\"hour\":10," +
                "\"minute\":0,\"second\":0,\"nano\":0}}}";
        assertEquals(string, testString);
    }

    @Test
    void getEpicsByIdWhenEmptyList() {
        assertEquals(sendGetRquest("tasks/epic/?id=1").body(), "");
    }

    @Test
    void getEpicsByIdWhenNotEmptyList() {
        sendPostRequest("epic", epic1);
        String string = sendGetRquest("tasks/epic/?id=1").body();
        String testString = "{\"subTasks\":[],\"name\":\"epic1Name\",\"description\":\"epic1Description\"," +
                "\"status\":\"NEW\"}";
        assertEquals(string, testString);
    }

    @Test
    void getHistoryWhenEmpty() {
        assertEquals(sendGetRquest("tasks/history").body(), "[]");
    }

    @Test
    void getHistory() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        sendGetRquest("tasks/task/?id=1");
        sendGetRquest("tasks/epic/?id=2");
        String jsonOfAllTasks = "[{\"name\":\"name\",\"description\":\"description\"," +
                "\"status\":\"NEW\",\"duration\":{\"seconds\":3000,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2025,\"month\":5,\"day\":13},\"time\":{\"hour\":10,\"minute\":0," +
                "\"second\":0,\"nano\":0}}},{\"subTasks\":[],\"name\":\"epic1Name\"," +
                "\"description\":\"epic1Description\",\"status\":\"NEW\"}]";
        assertEquals(sendGetRquest("tasks/history").body(), jsonOfAllTasks);
    }

    @Test
    void getTasksPriority() {
        sendPostRequest("task", task);
        String testString = "[{\"name\":\"name\",\"description\":\"description\", \"status\":\"NEW\"," +
                "\"duration\":{\"seconds\":3000,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2025,\"month\":5," +
                "\"day\":13},\"time\":{\"hour\":9,\"minute\":0,\"second\":0,\"nano\":0}}}]";
        assertEquals(sendGetRquest("tasks").body(), testString);
    }

    @Test
    void getTasksPriorityWhenEmpty() {
        assertEquals(sendGetRquest("tasks").body(), "[]");
    }

    @Test
    void postTask() {
        Task newTask = cloneTask(task);
        sendPostRequest("task", task);
        assertEquals(manager.getTask(1), newTask);
    }

    @Test
    void postSubtask() {
        Task newTask = cloneSubtask(subtask1);
        sendPostRequest("subtask", subtask1);
        assertEquals(manager.getSubtask(1), newTask);
    }

    @Test
    void postEpic() {
        Task newTask = cloneEpic(epic1);
        newTask.setStatus(Task.Status.NEW);
        sendPostRequest("epic", epic1);
        assertEquals(manager.getEpic(1), newTask);
    }

    @Test
    void deleteTasks() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        deleteRequest("tasks/task");
        assertEquals(sendGetRquest("tasks/task").body(), "[]");
    }


    void deleteSubtasks() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        assertEquals(sendGetRquest("tasks/subtask").body(), "[]");
    }

    void deleteEpics() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        assertEquals(sendGetRquest("tasks/epic").body(), "[]");
    }

    void deleteTask() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        assertEquals(sendGetRquest("tasks/task/?id=1").body(), "[]");
    }

    void deleteSubtask() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        assertEquals(sendGetRquest("tasks/subtasktask/?id=3").body(), "[]");
    }

    void deleteEpic() {
        sendPostRequest("task", task);
        sendPostRequest("epic", epic1);
        sendPostRequest("subtask", subtask1);
        assertEquals(sendGetRquest("tasks/epic/?id=2").body(), "[]");
    }

    void sendPostRequest(String path, Task newTask) {
        HttpResponse<String> response = null;
        try {
            URI url = URI.create("http://localhost:8080/tasks/" + path);
            Gson gson = new Gson();

            String json = gson.toJson(newTask);

            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

            response = HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(url)
                    .POST(body).build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("");
        }

    }

    HttpResponse<String> sendGetRquest(String path) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    void deleteRequest(String path) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void fillServer() {
        task = new Task("Task name", "Task description",
                LocalDateTime.of(2025, 5, 13, 10, 0), Duration.ofMinutes(60));

        epic1 = new Epic("Epic1 name", "Epic1 description");

        subtask1 = new Subtask("subtask1 name", "subtask1 description",
                LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60), epic1.getId());

        subtask2 = new Subtask("subtask2 name", "subtask2 description",
                LocalDateTime.of(2022, 4, 16, 11, 0), Duration.ofMinutes(60), epic1.getId());

        epic2 = new Epic("Epic2 name", "Epic2 description");

        subtask3 = new Subtask("subtask3 name", "subtask3 description",
                LocalDateTime.of(2022, 4, 16, 13, 0), Duration.ofMinutes(60), epic2.getId());
    }

    //    Вспомогательные методы для тестов

    public Task cloneTask(Task taskToClone) {
        Task newTask = new Task(taskToClone.getName(), taskToClone.getDescription(), taskToClone.getStartTime(), taskToClone.getDuration());
        newTask.setStatus(taskToClone.getStatus());
        return newTask;
    }

    public Subtask cloneSubtask(Subtask subtask) {
        Subtask newSubtask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getStartTime(), subtask.getDuration(), subtask.getEpicId());
        newSubtask.setStatus(subtask.getStatus());
        return newSubtask;
    }

    public Epic cloneEpic(Epic epicToClone) {
        Epic newEpic = new Epic(epicToClone.getName(), epicToClone.getDescription());
        return newEpic;
    }
}
