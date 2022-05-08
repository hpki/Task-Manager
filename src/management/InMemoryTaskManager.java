package management;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Long, Task> taskList = new HashMap<>();
    private HashMap<Long, Subtask> subtaskList = new HashMap<>();
    private HashMap<Long, Epic> epicList = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();   // объект для хранения последних 10-ти задач

    private static long counter = 0;

    public long generateId() {
        counter = counter + 1;
        return counter;
    }

    private Comparator<Task> userComparator = new Comparator<>() { //компаратор для TreeSet
        @Override
        public int compare(Task task1, Task task2) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    };
    private Map<Long, Task> taskTree = new TreeMap<Long, Task>((Map<? extends Long, ? extends Task>) userComparator);

    public void putToTree(Task task) {
        taskTree.put(task.getId(), task);
    }

    private Map<Long, Task> getPrioritizedTasks() {
        return taskTree;
    }

    // метод-валидатор по пересечению во времени
    public Boolean timeValidate(Task task) {
        for (Map.Entry<Long, Task> entry : taskList.entrySet()) {
            if (task.getStartTime().isAfter(entry.getValue().getStartTime()) && task.getEndTime().isBefore(entry.getValue().getEndTime())) {
                for (Map.Entry<Long, Subtask> entrySub : subtaskList.entrySet()) {
                    if (task.getStartTime().isAfter(entrySub.getValue().getStartTime()) && task.getEndTime().isBefore(entrySub.getValue().getEndTime())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 1-Методы для Task
    @Override
    public void createTask(Task task) { // создание задачи
        if (timeValidate(task)) {
            task.setId(generateId());
            taskList.put(task.getId(), task);
            putToTree(task);
        } else {
            IOException ex = new IOException();
        }
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
        taskList.put(task.getId(), task);
    }

    // 2-Методы для Epic

    @Override
    public void createEpic(Epic epic) {         // создание задачи (эпика)
        epic.setId(generateId());
        epicList.put(epic.getId(), epic);
        setEpicStartTime(epic);
        setEpicEndTime(epic);
        setEpicDuration(epic);
    }

    public void setEpicStartTime(Epic epic) {  //----расчёт startTime
        List<Long> listSubtaskId = epic.getIdSubtaskList();
        Subtask earlierSubtask = null;
        LocalDateTime earlierStartTime = LocalDateTime.of(22000, 1, 1, 0, 0, 0, 0);
        ;
        for (Long id : listSubtaskId) {
            if (subtaskList.get(id).getStartTime().isBefore(earlierStartTime)) {
                earlierSubtask = subtaskList.get(id);
            }
        }
        epic.setStartTime(earlierSubtask.getStartTime());
    }

    public void setEpicEndTime(Epic epic) {    //--расчёт endTime
        List<Long> listSubtaskId = epic.getIdSubtaskList();
        Subtask laterSubtask = null;
        LocalDateTime laterEndTime = LocalDateTime.of(1, 1, 1, 0, 0, 0, 0);
        ;
        for (Long id : listSubtaskId) {
            if (subtaskList.get(id).getEndTime().isAfter(laterEndTime)) {
                laterSubtask = subtaskList.get(id);
            }
        }
        epic.setEndTime(laterSubtask.getEndTime());
    }

    public void setEpicDuration(Epic epic) {   //- расчёт duration
        List<Long> listSubtaskId = epic.getIdSubtaskList();
        Duration epicDuration = null;
        for (Long id : listSubtaskId) {
            epicDuration = epicDuration.plus(subtaskList.get(id).getDuration());
        }
        epic.setDuration(epicDuration);
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
        historyManager.add(epicList.get(id)); // добавление задачи в список последних 10-ти вызываемых задач
        return epicList.get(id);
    }

    @Override
    public ArrayList<Long> getListOfSubtask(Epic epic) {
        return epic.getSubtaskList();
    }

    // 3 - Методы для Subtask

    @Override
    public void createSubtask(Subtask subtask) {
        if (timeValidate(subtask)) {
            subtask.setId(generateId());
            subtaskList.put(subtask.getId(), subtask);
            putToTree(subtask);
            long id = subtask.getEpicId();                   // вытаскиваю ID эпика из сабтаска
            if (epicList.containsKey(id)) {
                epicList.get(id).setIdSubtaskList(subtask.getId());
            }
        } else {
            IOException ex = new IOException();
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
        historyManager.add(subtaskList.get(id)); // добавление задачи в список последних 10-ти вызываемых задач
        return subtaskList.get(id);
    }

    @Override // НОВЫЙ МЕТОД
    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        calculateEpicStatus(subtask);
    }

    private void calculateEpicStatus(Subtask subtask) {  // метод расчёта статуса Эпика
        long epicId = subtask.getEpicId();
        ArrayList<Subtask> subList = new ArrayList<>();
        for (Subtask task : subtaskList.values()) {
            if (task.getEpicId() == epicId) {
                subList.add(task);
            }
        }
        int countDONE = 0;
        int countNEW = 0;
        for (Subtask subtaskOfsubList : subList) {
            if ((subtaskOfsubList.getStatus()).equals(Task.Status.DONE)) {
                countDONE++;
            } else if ((subtaskOfsubList.getStatus()).equals(Task.Status.NEW)) {
                countNEW++;
            }
        }
        if (countDONE >= subList.size()) {
            epicList.get(subtask.getEpicId()).setStatus(Subtask.Status.DONE);
        } else if (countNEW >= subList.size()) {
            epicList.get(subtask.getEpicId()).setStatus(Subtask.Status.NEW);    // добавил условие для NEW
        } else {
            epicList.get(subtask.getEpicId()).setStatus(Subtask.Status.IN_PROGRESS);
        }
    }

    public void remove(long id) {   // удаление task, subtask или epic по идентификатору
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            //long epicId = subtaskList.get(id).getEpicId();
            Subtask subtask = subtaskList.get(id);
            subtaskList.remove(id);                         // удаляет сабтаск....
            calculateEpicStatus(subtask);
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
    public List<Task> history() { // возвращает список последних 10-ти тасков
        return historyManager.getHistory();
    }

    @Override
    public int getSize() {
        return historyManager.getSize();
    }

    @Override
    public void removeInHistory(long id) {
        historyManager.removeInHistory(id);
    }

    public HashMap<Long, Task> getMapTaskList() {
        return taskList;
    }

    public HashMap<Long, Subtask> getMapSubtaskList() {
        return subtaskList;
    }

    public HashMap<Long, Epic> getMapEpicList() {
        return epicList;
    }

    class FirstComparator implements Comparator<Task> {
        @Override
        public int compare(Task e1, Task e2) {
            return (e1.getStartTime()).compareTo(e2.getStartTime());
        }
    }

}
