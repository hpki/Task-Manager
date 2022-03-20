package management;
import java.util.List;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    // 1 - Методы для Task
    void createTask(Task task);
    ArrayList<String> getTaskList();
    void clearTaskList();
    Task getTask(long id);
    void updateTaskInProgress(Task task);
    void updateTaskDone(Task task);

    // // 2 - Методы для Epic
    void createEpic(Epic epic);
    ArrayList<String> getEpicList();
    void clearEpicList();
    Epic getEpic(long id);
    ArrayList<Long> getListOfSubtask(Epic epic);

    // 3 - Методы для Subtask
    void createSubtask(Subtask subtask);
    ArrayList<String> getSubtaskList();
    void clearSubtaskList();
    Subtask getSubtask(long id);
    void updateSubtaskInProgress(Subtask subtask);
    void updateSubtaskDone(Subtask subtask);
    public void addIdToEpic(Subtask subtask);

    // метод из ТЗ №3
    List<Task> history();
    }





