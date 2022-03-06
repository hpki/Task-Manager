package management;

import java.util.ArrayList;
import java.util.HashMap;

import tasks.*;

public class Manager {
    public HashMap<Long, Task> taskList = new HashMap<>();
    public HashMap<Long, Subtask> subtaskList = new HashMap<>();
    public HashMap<Long, Epic> epicList = new HashMap<>();

    private static long counter = 1;

    // 1-Методы для Task
    public void createTask(Task task) { // создание задачи
        task.setId(counter);
        //task.id = counter;
        counter++;
        taskList.put(task.getId(), task);
    }

    public ArrayList<String> getTaskList() {  // возвращение списка задач
        ArrayList<String> values = new ArrayList(taskList.values());
        return values;
    }

    public void clearTaskList() {  // очитска списка задач
        taskList.clear();
    }

    public Task getTask(int id) {  // получение задачи по id
        return taskList.get(id);
    }

    public void updateTaskInProgress(Task task) { // обновление задачи на IN_PROGRESS
        task.setStatus(String.valueOf(Task.Status.IN_PROGRESS));
        taskList.put(task.getId(), task);
    }

    public void updateTaskDone(Task task) { // обновление задачи на DONE
        task.setStatus(String.valueOf(Task.Status.DONE));
        taskList.put(task.getId(), task);
    }

    /// 2-Методы для Epic
    public void createEpic(Epic epic) {         // создание задачи
        epic.setId(counter);
        //epic.id = counter;
        counter++;
        epicList.put(epic.getId(), epic);
    }

    public ArrayList<String> getEpicList() {     // возвращение списка задач (эпиков)
        ArrayList<String> values = new ArrayList(epicList.values());
        return values;
    }

    public void clearEpicList() {  // очитска списка задач
        epicList.clear();
    }

    public Epic getEpic(int id) {  // получение задачи по id
        return epicList.get(id);
    }

    public ArrayList<Long> getListOfSubtask(Epic epic) {
        return epic.getSubtaskList();
    }
    // 3 - Методы для Subtask

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

    public ArrayList<String> getSubtaskList() {     // возвращение списка задач
        ArrayList<String> values = new ArrayList(subtaskList.values());
        return values;
    }

    public void clearSubtaskList() {  // очитска списка задач
        subtaskList.clear();
    }

    public Subtask getSubtask(int id) {  // получение подзадачи по id
        return subtaskList.get(id);
    }

    public void updateSubtaskInProgress(Subtask subtask) { // обновление под задачи на IN_PROGRESS
        String status = Task.Status.IN_PROGRESS.toString();
        subtask.setStatus(status);
        //subtask.status = "IN_PROGRESS";
        subtaskList.put(subtask.getId(), subtask);
        //taskList.put(subtask.getId(), subtask);
    }

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

    public void addIdToEpic(Subtask subtask) {

    }

}
