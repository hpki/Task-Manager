package management;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private String fileName;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    static private FileBackedTasksManager loadFromFile(File fileName) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName.toString());
        FileReader reader = new FileReader(fileName.toString());
        BufferedReader br = new BufferedReader(reader);
        ArrayList<String> list = new ArrayList<>();
        while (br.ready()) {
            list.add(br.readLine());
        }
        for (int i = 1; i < list.size() - 2; i++) {
            fileBackedTasksManager.fromString(list.get(i));
        }
        String listFromMemory = list.get(list.size() - 1);
        List<Long> listMemory = fromStringHistory(listFromMemory);
        String[] splitListFromMemory = listFromMemory.split(",");
        List<Task> taskHistory = new ArrayList<>();
        HashMap<Long, Task> taskList = fileBackedTasksManager.getMapTaskList();
        HashMap<Long, Subtask> subtaskList = fileBackedTasksManager.getMapSubtaskList();
        HashMap<Long, Epic> epicList = fileBackedTasksManager.getMapEpicList();
        for (Long id : listMemory) {
            for (Long idTask : taskList.keySet()) {
                if (id == idTask) {
                    taskHistory.add(taskList.get(idTask));
                }
                for (Long idSubtask : subtaskList.keySet()) {
                    if (id == idSubtask) {
                        taskHistory.add(subtaskList.get(idSubtask));
                    }
                    for (Long idEpic : epicList.keySet()) {
                        if (id == idEpic) {
                            taskHistory.add(epicList.get(idEpic));
                        }
                    }
                }
            }
        }
        return fileBackedTasksManager;
    }

    static String toString(HistoryManager manager) { //метод перевода истории вызовов в строку
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId() + ",");
        }
        return sb.toString();
    }

    static List<Long> fromStringHistory(String value) {
        ArrayList<Long> listTaskHistory = new ArrayList<>();
        String[] split = value.split(",");
        for (String id : split) {
            listTaskHistory.add(Long.parseLong(id));
        }
        return listTaskHistory;
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
        }
        return sb.toString();
    }

    public String toString(Subtask subtask) {   //метод перевода подзадачи в строку
        StringBuilder sb = new StringBuilder();
        sb.append(subtask.getId() + ",").append(subtask.getType()).append(subtask.getName() + ",")
                .append(subtask.getStatus() + ",").append(subtask.getDescription()).append(subtask.getEpicId());
        return sb.toString();
    }


    public Task fromString(String value) {  //метод перевода строки в задачу

        Task taskResult = null;
        String[] split = value.split(",");
        if (split.length == 7) {
            long id = Long.parseLong(split[0]);
            String type = split[1];
            String name = split[2];
            String status = split[3];
            String description = split[4];
            String startTime = split[5];
            String duration = split[6];
            if (type.equals(Task.TypeTask.TASK)) {
                Task task = new Task(name, description, LocalDateTime.parse(startTime), Duration.parse(duration));
                task.setId(id);
                task.setStatus(Task.Status.valueOf(status));
                task.setType(Task.TypeTask.TASK);
                createTask(task);
                taskResult = task;
            } else if (type.equals(Task.TypeTask.EPIC)) {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(Task.Status.valueOf(status));
                epic.setType(Task.TypeTask.EPIC);
                createTask(epic);
                taskResult = epic;
            } else {
                long idSubtask = Long.parseLong(split[0]);
                String typeSubtask = split[1];
                String nameSubtask = split[2];
                String statusSubtask = split[3];
                String descriptionSubtask = split[4];
                String startTimeSubtask = split[5];
                String durationSubtask = split[6];

                long epicId = Long.parseLong(split[5]);
                Subtask subtask = new Subtask(nameSubtask, descriptionSubtask, LocalDateTime.parse(startTime), Duration.parse(duration), epicId);
                subtask.setId(idSubtask);
                subtask.setStatus(Task.Status.valueOf(statusSubtask));
                subtask.setType(Task.TypeTask.SUBTASK);
                createSubtask(subtask);
                taskResult = subtask;
            }
        }
        return taskResult;
    }

    private void save()  throws ManagerSaveException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getMapTaskList().values()) {
                String string = toString(task);
                fileWriter.write(string + "\n");
            }
            for (Task task : getMapEpicList().values()) {
                String string = toString(task);
                fileWriter.write(string + "\n");
            }
            for (Subtask subtask : getMapSubtaskList().values()) {
                String string = toString(subtask);
                fileWriter.write(string + "\n");
            }
            fileWriter.write(",\n");

            fileWriter.write(toString((HistoryManager) history()));

        } catch (IOException e) {
            throw new ManagerSaveException();
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
    public void clearEpicList() {  // очистка списка задач
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
    public void clearSubtaskList() {  // очистка списка задач
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

    private class ManagerSaveException extends RuntimeException {
        public ManagerSaveException() {
        }
    }
}






