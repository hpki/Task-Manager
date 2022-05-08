import management.FileBackedTasksManager;
import management.InMemoryTaskManager;
import management.Managers;
import management.TaskManager;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Managers manager1 = new Managers();

        TaskManager manager = manager1.getDefault();

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("\\file.csv");

         Task task1 = new Task("Купить книгу", "Заказть книгу в интернете",
                 LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0),
                 Duration.ofHours(6));
         Task task2 = new Task("Заняться спортом", "Бегать по утрам",
                 LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0),
                 Duration.ofHours(6));

        Epic epic1 = new Epic("Ремонт", "Сделать ремонт");
        Epic epic2 = new Epic("Отпуск", "Организовать отдых");
        Subtask subtask1 = new Subtask("Поклеить обои", "Купить и поклеить",
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofHours(6), 1);
        Subtask subtask2 = new Subtask("Залить пол", "Купить смесь и залить",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                Duration.ofHours(6), 1);
        Subtask subtask3 = new Subtask("Купить билеты", "Пойти в кассы",
                LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0),
                Duration.ofHours(6), 1);

        manager.createTask(task1);
        fileBackedTasksManager.createTask(task1);
        manager.createTask(task2);
        fileBackedTasksManager.createTask(task2);
        manager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic1);
        manager.createEpic(epic2);
        fileBackedTasksManager.createEpic(epic2);
        manager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        fileBackedTasksManager.createSubtask(subtask3);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println("////////////////////////////////////");
        manager.getTask(1);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        manager.getEpic(3);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        manager.getSubtask(5);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        manager.getSubtask(6);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        manager.getEpic(4);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        manager.getEpic(3);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        manager.getTask(1);
        System.out.println(manager.getSize());
        System.out.println(manager.history());
        System.out.println("////////////////////////////////////");
        //manager.removeInHistory(3);
        System.out.println(manager.getSize());
        System.out.println(manager.history());


    }
}
