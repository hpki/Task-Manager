package management;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.ls.LSOutput;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    public HashMap<Long, Task> taskList = new HashMap<>();
    public HashMap<Long, Subtask> subtaskList = new HashMap<>();
    public HashMap<Long, Epic> epicList = new HashMap<>();

    public HistoryManager historyManager = Managers.getDefaultHistory();   // объект для хранения последних 10-ти задач

    private static long counter = 0;

    public long generateId() {
        counter = counter + 1;
        return counter;
    }

    // 1-Методы для Task
    @Override
    public void createTask(Task task) { // создание задачи
        task.setId(generateId());
        taskList.put(task.getId(), task);
    }

    @Override
    public ArrayList<String> getTaskList() {  // возвращение списка задач
        ArrayList<String> values = new ArrayList(taskList.values());
        return values;
    }

    @Override
    public void clearTaskList() {  // очистка списка задач
        taskList.clear();
    }

    @Override
    public Task getTask(long id) {  // получение задачи по id
        historyManager.add(taskList.get(id)); // добавление задачи в список последних 10-ти вызываемых задач
        return taskList.get(id);
    }

    @Override // НОВЫЙ МЕТОД - обновление статуса (универсальный метод)
    public void updateTask(Task task) {
        switch (task.getStatus()) {
            case "NEW":
                task.setStatus(String.valueOf(Task.Status.IN_PROGRESS));
                taskList.put(task.getId(), task);
                break;
            case "IN_PROGRESS":
                task.setStatus(String.valueOf(Task.Status.DONE));
                taskList.put(task.getId(), task);
                break;
        }
    }

    // 2-Методы для Epic

    @Override
    public void createEpic(Epic epic) {         // создание задачи (эпика)
        epic.setId(generateId());
        epicList.put(epic.getId(), epic);
    }

    @Override
    public ArrayList<String> getEpicList() {     // возвращение списка задач (эпиков)
        ArrayList<String> values = new ArrayList(epicList.values());
        return values;
    }

    @Override
    public void clearEpicList() {  // очитска списка задач
        epicList.clear();
    }

    @Override
    public Epic getEpic(long id) {  // получение задачи по id
        historyManager.add(epicList.get(id)); // добавление задачи в список последних 10-ти вызыввемых задач
        return epicList.get(id);
    }

    @Override
    public ArrayList<Long> getListOfSubtask(Epic epic) {
        return epic.getSubtaskList();
    }
    // 3 - Методы для Subtask

    @Override
    public void createSubtask(Subtask subtask) {         // создание задачи
        subtask.setId(generateId());
        subtaskList.put(subtask.getId(), subtask);
        long id = subtask.getEpicId();                   // вытаскиваю ID эпика из сабтаска
        if (epicList.containsKey(id)) {
            epicList.get(id).setIdSubtaskList(subtask.getId());
        }
    }

    @Override
    public ArrayList<String> getSubtaskList() {     // возвращение списка задач
        ArrayList<String> values = new ArrayList(subtaskList.values());
        return values;
    }

    @Override
    public void clearSubtaskList() {  // очитска списка задач
        subtaskList.clear();
    }

    @Override
    public Subtask getSubtask(long id) {  // получение подзадачи по id
        historyManager.add(subtaskList.get(id)); // добавление задачи в список последних 10-ти вызыввемых задач
        return subtaskList.get(id);
    }

    @Override // НОВЫЙ МЕТОД
    public void updateSubtask(Subtask subtask) {
        switch (subtask.getStatus()) {
            case "NEW":
                subtask.setStatus(String.valueOf(Task.Status.IN_PROGRESS));
                subtaskList.put(subtask.getId(), subtask);
                epicList.get(subtask.getEpicId()).setStatus(Task.Status.IN_PROGRESS.toString());
                break;
            case "IN_PROGRESS":
                subtask.setStatus(String.valueOf(Task.Status.DONE));
                subtaskList.put(subtask.getId(), subtask);
                calculateEpicStatus(subtask);
        }
    }

    private void calculateEpicStatus(Subtask subtask) {  // метод расчёта статуса Эпика
        long epicId = subtask.getEpicId();
        ArrayList<Subtask> subList = new ArrayList<>();
        for (Subtask task : subtaskList.values()) {
            if (task.getEpicId() == epicId) {
                subList.add(task);
            }
        }
        int count = 0;
        for (Subtask subtaskOfsubList : subList) {
            if ((subtaskOfsubList.getStatus()).equals("DONE")) {
                count++;
            }
            if (count >= subList.size()) {
                epicList.get(subtask.getEpicId()).setStatus(Task.Status.DONE.toString());
            } else {
                epicList.get(subtask.getEpicId()).setStatus(Task.Status.IN_PROGRESS.toString());
            }
        }
    }

    public void remove(long id) {   // удаление task, subtask или epic по идентификатору
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            long epicId = subtaskList.get(id).getEpicId();
            subtaskList.remove(id);                         // удаляет сабтаск....
            long count = 0;
            for (Subtask subtask : subtaskList.values()) {
                if (subtask.getEpicId() == epicId) {
                    count = count + 1;
                }
            }
            if (count == 0) {
                epicList.get(epicId).setStatus("NEW");   // ... исправляет статус Эпика
            }
        } else if (epicList.containsKey(id)) {
            epicList.remove(id);                  // при удалении Epic удаляет все подзадачи этого эпика
            ArrayList<Subtask> subList = new ArrayList<>(subtaskList.values());
            for (Subtask subtask : subList) {
                if (subtask.getEpicId() == id) {
                    subtaskList.remove(subtask.getId());
                }
            }
        }
    }

    @Override
    public void addIdToEpic(Subtask subtask) {
    }

    @Override
    public List<Task> history() { // возвращает список последних 10-ти тасков
        return historyManager.getHistory();
    }
}
