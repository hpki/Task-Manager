package management;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// все необходимые методы и классы создал, но не уверен, что получилось их собрать вместе(
// надеюсь по Вашим замечаниям приведу в нормальный вид

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String fileName;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName; //"C:\\Костя\\по Java\\dev\\java-sprint2-hw\\src\\management\\file.csv";
    }

    static void main(String[] args) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("C:\\Костя\\по Java\\dev" +
                "\\java-sprint2-hw\\src\\management\\file.csv");
        Task task1 = new Task("Купить книгу", "Заказть книгу в интернете");
        Task task2 = new Task("Заняться спортом", "Бегать по утрам");
        Epic epic1 = new Epic("Ремонт", "Сделать ремонт");
        Epic epic2 = new Epic("Отпуск", "Организовать отдых");
        Subtask subtask1 = new Subtask("Поклеить обои", "Купить и поклеить", 1);
        Subtask subtask2 = new Subtask("Залить пол", "Купить смесь и залить", 1);
        Subtask subtask3 = new Subtask("Купить билеты", "Пойти в кассы", 1);
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);
        fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic2);

        Files.readString(Path.of(fileBackedTasksManager.fileName));
    }

    private FileBackedTasksManager loadFromFile(File fileName) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName.toString());
        FileReader reader = new FileReader(fileName);
        BufferedReader br = new BufferedReader(reader);
        ArrayList<String> list = new ArrayList<>();
        while (br.ready()) {
                list.add(br.readLine());
        }
        for (int i = 1; i < list.size()-1; i++) {
            fromString(list.get(i));
        }
        fromStringHistory(list.get(list.size() - 1));
        return fileBackedTasksManager;
    }

    static String toString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId() + ",");
        }
        return sb.toString();
    }

    static List<Integer> fromStringHistory(String value) {
        ArrayList<Integer> listTaskHistory = new ArrayList<>();
        String[] split = value.split(",");
        for (String id : split) {
            listTaskHistory.add(Integer.parseInt(id));
        } return listTaskHistory;
    }


    public String toString(Task task) { //метод перевода задачи и эпика в строку
        Task.TypeTask type = task.getType();
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case TASK:
                sb.append(task.getId() + ",").append(type + ",").append(task.getName() + ",")
                        .append(task.getStatus() + ",").append(task.getDescription());
                break;
            case EPIC:
                sb.append(task.getId() + ",").append(type + ",").append(task.getName() + ",")
                        .append(task.getStatus() + ",").append(task.getDescription());
                break;
        } return sb.toString();
    }

    public String toString(Subtask subtask) {   //метод перевода подзадачи в строку
        StringBuilder sb = new StringBuilder();
        sb.append(subtask.getId() + ",").append(subtask.getType()).append(subtask.getName() + ",")
                .append(subtask.getStatus() + ",").append(subtask.getDescription()).append(subtask.getEpicId());
        return sb.toString();
    }


    public Task fromString(String value) {  //метод перевода строки в задачу
        String[] split = value.split(",");
    long id = Integer.parseInt(split[0]);
    String type = split[1];
    String name = split[2];
    String status = split[3];
    String description = split[4];

    Task task = new Task(name, description);
    task.setId(id);
    task.setStatus(Task.Status.valueOf(status));
    if (type.equals(Task.TypeTask.TASK)) {
        task.setType(Task.TypeTask.TASK);
    } else if (type.equals(Task.TypeTask.SUBTASK)) {
    task.setType(Task.TypeTask.SUBTASK);
    } else if (type.equals(Task.TypeTask.EPIC)) {
        task.setType(Task.TypeTask.SUBTASK);
    } return task;
    }

    private void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (String task : getTaskList()) {
                fileWriter.write(task + "\n");
            }
            for (String task : getSubtaskList()) {
                fileWriter.write(task + "\n");
            }
            for (String task : getEpicList()) {
                fileWriter.write(task + "\n");
            }
            fileWriter.write(",\n");

            fileWriter.write(toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTask(Task task) { // создание задачи
        super.createTask(task);
        save();
    }

    @Override
    public void clearTaskList() {  // очистка списка задач
        super.clearTaskList();
        save();
    }

    @Override
    public Task getTask(long id) {  // получение задачи по id
        final Task result = super.getTask(id);
        save();
        return result;
    }

    @Override // НОВЫЙ МЕТОД - обновление статуса (универсальный метод)
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {         // создание задачи (эпика)
        super.createEpic(epic);
        save();
    }

    @Override
    public void clearEpicList() {  // очитска списка задач
        super.clearEpicList();
        save();
    }

    @Override
    public Epic getEpic(long id) {  // получение задачи по id
        final Epic result = super.getEpic(id);
        save();
        return result;
    }

    @Override
    public void clearSubtaskList() {  // очитска списка задач
        super.clearSubtaskList();
        save();
    }

    @Override
    public Subtask getSubtask(long id) {  // получение подзадачи по id
        final Subtask result = super.getSubtask(id);
        save();
        return result;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void remove(long id) {
        super.remove(id);
        save();
    }

    @Override
    public void removeInHistory(long id) {
        super.removeInHistory(id);
        save();
    }
}


