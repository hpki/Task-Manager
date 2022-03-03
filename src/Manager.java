import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Manager {
   HashMap<Integer, Task> taskList = new HashMap<>();
   HashMap<Integer, Subtask> subtaskList = new HashMap<>();
   HashMap<Integer, Epic> epicList = new HashMap<>();

    private static int counter = 1;
                                                                // 1-Методы для Task
    public void createTask(Task task) { // создание задачи
        task.id = counter;
        counter++;
        taskList.put(task.id, task);
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
        task.status = "IN_PROGRESS";
        taskList.put(task.id, task);
    }

    public void updateTaskDone(Task task) { // обновление задачи на DONE
        task.status = "DONE";
        taskList.put(task.id, task);
    }

                                                        /// 2-Методы для Epic
    public void createEpic(Epic epic) {         // создание задачи
        epic.id = counter;
        counter++;
        epicList.put(epic.id, epic);
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

    public ArrayList<Integer> getListOfSubtask(Epic epic) {
        return epic.getSubtaskList();
    }
                                                         // 3 - Методы для Subtask

    public void createSubtask(Subtask subtask) {         // создание задачи
        subtask.id = counter;
        counter++;
        subtaskList.put(subtask.id, subtask);
        int id = subtask.getEpicId();                   // вытаскиваю ID эпика из сабтаска
        if (epicList.containsKey(id)) {
            epicList.get(id).setIdSubtaskList(subtask.id);
        }
    }

    public ArrayList<String> getSubtaskList() {     // возвращение списка задач
        ArrayList<String> values = new ArrayList(subtaskList.values());
        return values;
    }

    public void clearSubtaskList() {  // очитска списка задач
        subtaskList.clear();
    }

    public Subtask getSubtask(int id) {  // получение задачи по id
        return subtaskList.get(id);
    }

    public void updateSubtaskInProgress(Subtask subtask) { // обновление задачи на IN_PROGRESS
        subtask.status = "IN_PROGRESS";
        subtaskList.put(subtask.id, subtask);
        epicList.get(subtask.getEpicId()).status = "IN_PROGRESS";
    }

    public void updateSubtaskDone(Subtask subtask) { // обновление задачи на DONE
        subtask.status = "DONE";
        subtaskList.put(subtask.id, subtask);
        int epicId = subtask.getEpicId();
        ArrayList<Subtask> subList = new ArrayList<>();
        for(Subtask task : subtaskList.values()) {
            if (task.getEpicId() == epicId) {
                subList.add(task);
            }
            int count = 0;
            for (Subtask subtaskOfsubList  : subList) {
                if ((subtaskOfsubList.status).equals("DONE")) {
                    count++;
                }
                if (count >= subList.size()) {
                    epicList.get(subtask.getEpicId()).status = "DONE";
                } else {
                    epicList.get(subtask.getEpicId()).status = "IN_PROGRESS";
                }
            }

        }
    }

    public void addIdToEpic(Subtask subtask) {

    }

}
