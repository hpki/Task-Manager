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

    public HistoryManager historyManager = Managers.getDefaultHistory();   // массив для хранения последних 10-ти задач
    //public HistoryManager historyManager = new InMemoryHistoryManager(); // массив для хранения последних 10-ти задач

    private static long counter = 1;

    // 1-Методы для Task
    @Override
    public void createTask(Task task) { // создание задачи
        task.setId(counter);
        //task.id = counter;
        counter++;
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

    @Override
    public void updateTaskInProgress(Task task) { // обновление задачи на IN_PROGRESS
        task.setStatus(String.valueOf(Task.Status.IN_PROGRESS));
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateTaskDone(Task task) { // обновление задачи на DONE
        task.setStatus(String.valueOf(Task.Status.DONE));
        taskList.put(task.getId(), task);
    }

    // 2-Методы для Epic

    @Override
    public void createEpic(Epic epic) {         // создание задачи (эпика)
        epic.setId(counter);
        //epic.id = counter;
        counter++;
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
        subtask.setId(counter);
        //subtask.id = counter;
        counter++;
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

    @Override
    public void updateSubtaskInProgress(Subtask subtask) { // обновление под задачи на IN_PROGRESS
        String status = Task.Status.IN_PROGRESS.toString();
        subtask.setStatus(status);
        //subtask.status = "IN_PROGRESS";
        subtaskList.put(subtask.getId(), subtask);
        //taskList.put(subtask.getId(), subtask);
    }

    @Override
    public void updateSubtaskDone(Subtask subtask) { // обновление задачи на DONE
        String status = Task.Status.DONE.toString();
        subtask.setStatus(status);
        long epicId = subtask.getEpicId();
        ArrayList<Subtask> subList = new ArrayList<>();
        for (Subtask task : subtaskList.values()) {
            if (task.getEpicId() == epicId) {
                subList.add(task);
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
    }

    @Override
    public void addIdToEpic(Subtask subtask) {
    }

    @Override
    public List<Task> history() { // возвращает список последних 10-ти тасков
        return historyManager.getHistory();
    }

    /*public void printHistory() {
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    } */

}
