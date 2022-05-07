import management.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @Test
    void createTaskTest() {
        Task task = new Task("Test addTask", "Test addTask description");
        taskManager.createTask(task);

        final long idTask = task.getId();
        final Task newTask = taskManager.getTask(idTask);

        assertNotNull(newTask, "Задача не найдена.");
        assertEquals(task, newTask, "Задачи не совпадают.");

        final List<String> taskList = taskManager.getTaskList();

        assertNotNull(taskList, "Задачи на возвращаются.");
        assertEquals(1, taskList.size(), "Неверное количество задач.");
        assertEquals(task, taskList.get(0), "Задачи не совпадают.");
    }

    @Test
    void clearTaskListTest() {
        Task task = new Task("Test addTask", "Test addTask description");
        taskManager.createTask(task);

        final List<String> taskList = taskManager.getTaskList();

        taskManager.clearTaskList();
        int listSize = taskList.size();
        assertEquals(1, taskList.size(), "Список не пустой.");
    }

    @Test
    void updateTaskTest() {
        Task task = new Task("Test taskName", "Test addTask description");
        taskManager.createTask(task);
        List<String> taskList = taskManager.getTaskList();
        String nameTask = taskList.get(0);

        Task task1 = new Task("Test anotherTaskName", "Test addTask description");
        taskManager.updateTask(task1);
        String nameTask1 = taskList.get(0);
        assertEquals(nameTask, nameTask1, "Задачи одинаковые.");
    }

    @Test
    void getTaskTest() {
        Task task = new Task("Test addTask", "Test addTask description");
        taskManager.createTask(task);
        long id = task.getId();
        taskManager.getTask(id);
        assertNotNull(taskManager.history(), "Список вызовов задач пуст");
    }

    @Test
    void createEpicTest() {
        Epic epic = new Epic("Test addEpic", "Test addEpic description");
        taskManager.createTask(epic);

        final long idEpic = epic.getId();
        final Epic newEpic = taskManager.getEpic(idEpic);

        assertNotNull(newEpic, "Задача не найдена.");
        assertEquals(epic, newEpic, "Задачи не совпадают.");

        final List<String> epicList = taskManager.getEpicList();

        assertNotNull(epicList, "Задачи на возвращаются.");
        assertEquals(1, epicList.size(), "Неверное количество задач.");
        assertEquals(epic, epicList.get(0), "Задачи не совпадают.");
    }

    @Test
    void clearEpicListTest() {
        Epic epic = new Epic("Test addEpic", "Test addEpic description");
        taskManager.createEpic(epic);

        final List<String> epicList = taskManager.getEpicList();

        taskManager.clearEpicList();
        int listSize = epicList.size();
        assertEquals(1, epicList.size(), "Список не пустой.");
    }

    @Test
    void getEpicTest() {
        Epic epic = new Epic("Test addEpic", "Test addEpic description");
        taskManager.createEpic(epic);
        long id = epic.getId();
        taskManager.getEpic(id);
        assertNotNull(taskManager.history(), "Список вызовов задач пуст");
    }

    @Test
    void getListOfSubtaskTest() {
        Epic epic = new Epic("Test addEpic", "Test addEpic description");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test addSubtask", "Test addSubtask description", epic.getId());
        taskManager.createEpic(epic);
        List<Long> idSubtaskList = taskManager.getListOfSubtask(epic);
        long id = idSubtaskList.get(0);
        assertEquals(id, subtask.getId(), "Номера не совпадают.");
    }

    @Test
    void createSubtaskTest() {
        Subtask subtask = new Subtask("Test addSubtask", "Test addSubtask description", 1);
        taskManager.createSubtask(subtask);

        final long idSubtask = subtask.getId();
        final Subtask newSubtask = taskManager.getSubtask(idSubtask);

        assertNotNull(newSubtask, "Задача не найдена.");
        assertEquals(subtask, newSubtask, "Задачи не совпадают.");

        final List<String> subtaskList = taskManager.getSubtaskList();

        assertNotNull(subtaskList, "Задачи на возвращаются.");
        assertEquals(1, subtaskList.size(), "Неверное количество задач.");
        assertEquals(subtask, subtaskList.get(0), "Задачи не совпадают.");
    }

    @Test
    void clearSubtaskListTest() {
        Subtask subtask = new Subtask("Test addSubtask", "Test addSubtask description", 1);
        taskManager.createSubtask(subtask);

        final List<String> subtaskList = taskManager.getSubtaskList();

        taskManager.clearSubtaskList();
        int listSize = subtaskList.size();
        assertEquals(1, subtaskList.size(), "Список не пустой.");
    }

    @Test
    void getSubtaskTest() {
        Subtask subtask = new Subtask("Test addSubtask", "Test addSubtask description", 1);
        taskManager.createSubtask(subtask);
        long id = subtask.getId();
        taskManager.getSubtask(id);
        assertNotNull(taskManager.history(), "Список вызовов задач пуст");
    }

    @Test
    void updateSubtaskTest() {
        Subtask subtask = new Subtask("Test SubtaskName", "Test SubtaskName description", 1);
        taskManager.createSubtask(subtask);
        List<String> subtaskList = taskManager.getSubtaskList();
        String nameSubtask = subtaskList.get(0);

        Subtask subtask1 = new Subtask("Test anotherSubtaskName", "Test addTask description", 1);
        taskManager.updateTask(subtask1);
        String nameSubtask1 = subtaskList.get(0);
        assertEquals(nameSubtask, nameSubtask1, "Задачи одинаковые.");
    }

    @Test
    void calculateEpicStatusTest() {
        Epic epic = new Epic("Test addEpic", "Test addEpic description");
        taskManager.createEpic(epic);
        assertEquals(Task.Status.NEW, epic.getStatus(), "Статусы не совпадают."); // нет подзадач

        Subtask subtask1 = new Subtask("Test SubtaskName1", "Test SubtaskName1 description", 1);
        Subtask subtask2 = new Subtask("Test SubtaskName2", "Test SubtaskName2 description", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        assertEquals(Task.Status.NEW, epic.getStatus(), "Статусы не совпадают."); //все подзадачи NEW

        subtask1.setStatus(Task.Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Статусы не совпадают."); //одна подзадача IN_PROGRESS

        subtask1.setStatus(Task.Status.DONE);
        taskManager.updateSubtask(subtask1);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Статусы не совпадают."); //подзадачи NEW и DONE

        subtask1.setStatus(Task.Status.DONE);
        subtask2.setStatus(Task.Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(Task.Status.DONE, epic.getStatus(), "Статусы не совпадают."); //все подзадачи DONE
    }

    @Test
    void removeTest() {
        Task task = new Task("Test addTask", "Test addTask description");
        taskManager.createTask(task);
        taskManager.remove(task.getId());
        assertNotNull(taskManager.getTaskList(), "Список задач пуст");   // при удалении Task

        Epic epic = new Epic("Test addEpic", "Test addEpic description");
        taskManager.createTask(epic);
        taskManager.remove(epic.getId());
        assertNotNull(taskManager.getEpicList(), "Список эпиков пуст");   // при удалении Epic

        Subtask subtask = new Subtask("Test addSubtask", "Test addSubtask description", 1);
        taskManager.createSubtask(subtask);
        taskManager.remove(subtask.getId());
        assertNotNull(taskManager.getSubtaskList(), "Список подзадач пуст");   // при удалении Subtask

    }

}
