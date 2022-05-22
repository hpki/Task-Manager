package management;

import java.util.HashMap;
import java.util.List;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Map;

public interface TaskManager {
    // 1 - Методы для Task
    void createTask(Task task);

    ArrayList<String> getTaskList();

    void clearTaskList();

    Task getTask(long id);

    void updateTask(Task task);

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

    void updateSubtask(Subtask subtask);

    void remove(long id); // удаление задачи по идентификатору

    // метод из ТЗ №3
    List<Task> history();

    int getSize();

    void removeInHistory(long id);

    //для ТЗ №7
    ArrayList<Task> getAllTasks();

    void deliteAllTasks();

    HashMap<Long, Task> getMapTaskList();

    HashMap<Long, Subtask> getMapSubtaskList();

    HashMap<Long, Epic> getMapEpicList();
}





