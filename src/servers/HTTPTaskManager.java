package servers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import management.FileBackedTasksManager;
import management.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;

    public HTTPTaskManager(URI url) {
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        gson = new Gson();
        List<Long> listOfHistory = new ArrayList<>();
        for (Task task : history()) {
            listOfHistory.add(task.getId());
        }

        String tasks = gson.toJson(getTaskList());
        String subtasks = gson.toJson(getSubtaskList());
        String epics = gson.toJson(getEpicList());
        String history = gson.toJson(history());

        try {
            client.put("tasksKey", tasks);
            client.put("subTasksKey", subtasks);
            client.put("epicsKey", epics);
            client.put("historyKey", history);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createTaskWhenLoad(Task task) {
        getMapTaskList().put(task.getId(), task);
        putToTree(task);
    }

    private void createSubtaskWhenLoad(Subtask subtask) {
        getMapSubtaskList().put(subtask.getId(), subtask);
        putToTree(subtask);
    }

    private void createEpicWhenLoad(Epic epic) {
        getMapEpicList();
    }

    private static HTTPTaskManager loadFromClient(KVTaskClient client) {

        HTTPTaskManager loader = new HTTPTaskManager(client.getUrl());

        Type TaskListType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = new Gson().fromJson(client.load("tasksKey"), TaskListType);

        Type EpicListType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = new Gson().fromJson(client.load("epicsKey"), EpicListType);

        Type SubtaskListType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subTasks = new Gson().fromJson(client.load("subtasksKey"), SubtaskListType);

        Type historylistType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> history = new Gson().fromJson(client.load("historyKey"), historylistType);

        for (Task task : tasks) {
            loader.createTaskWhenLoad(task);
        }
        for (Epic epic : epics) {
            loader.createEpicWhenLoad(epic);
        }
        for (Subtask subtask : subTasks) {
            loader.createSubtaskWhenLoad(subtask);
        }
        // loader.setUin(loadedManager.getManagerEpicsMap().size() + loadedManager.getManagerTasksMap().size()
        //        + loadedManager.getManagerSubTasksMap().size());
        for (int i = 0; i < history.size(); i++) {
            loader.history().add(loader.getTask(history.get(i)));
        }
        return loader;
    }

    public static void main(String[] args) {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTTPTaskManager manager = (HTTPTaskManager) Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Описание эпика 1");
        Epic epic2 = new Epic("Epic 1", "Описание эпика 1");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask 1", "Описание сабтаска 1",
                LocalDateTime.of(2022, 5, 13, 5, 0), Duration.ofMinutes(60), epic1.getId());

        Subtask subtask2 = new Subtask("Subtask 2", "Описание сабтаска 2",
                LocalDateTime.of(2022, 5, 13, 5, 0), Duration.ofMinutes(60), epic1.getId());

        Subtask subtask3 = new Subtask("Subtask 3", "Описание сабтаска 3",
                LocalDateTime.of(2022, 5, 13, 5, 0), Duration.ofMinutes(60), epic2.getId());

        Subtask subtask4 = new Subtask("Subtask 4", "Описание сабтаска 4",
                LocalDateTime.of(2022, 5, 13, 5, 0), Duration.ofMinutes(60), epic2.getId());


        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);

        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.getSubtask(subtask3.getId());
        manager.getSubtask(subtask4.getId());

        Task task = new Task("Task 1", "Описание задачи 1");
        task.setStartTime(LocalDateTime.of(2029, 5, 13, 5, 0));
        task.setStatus(Task.Status.DONE);
        task.setDuration(Duration.ofMinutes(60));
        manager.createTask(task);
        manager.updateTask(task);
        manager.getTask(5);


        Task task2 = new Task("Task 2", "Описание задачи 2");
        task2.setStartTime(LocalDateTime.of(2030, 5, 13, 5, 0));
        task2.setStatus(Task.Status.IN_PROGRESS);
        task2.setDuration(Duration.ofMinutes(60));
        manager.createTask(task2);
        manager.updateTask(task2);
        manager.getTask(6);

        System.out.println(manager.client.load("tasksKey"));
        System.out.println(manager.client.load("subTasksKey"));
        System.out.println(manager.client.load("epicsKey"));
        System.out.println(manager.client.load("historyKey"));

        HTTPTaskManager newManager = loadFromClient(manager.client);
        String firstManager = manager.history().toString();
        String secondManager = manager.history().toString();
        System.out.println(firstManager.equals(secondManager));
        System.out.println(firstManager);
        System.out.println(secondManager);
    }
}
