import management.FileBackedTasksManager;
import management.InMemoryTaskManager;
import management.Managers;
import management.TaskManager;
import tasks.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Managers manager1 = new Managers();

        TaskManager manager = manager1.getDefault();

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("\\file.csv");

        //TaskManager manager = new InMemoryTaskManager();

        // Task task1 = new Task("Купить книгу", "Заказть книгу в интернете");
       //  Task task2 = new Task("Заняться спортом", "Бегать по утрам");

       // Epic epic1 = new Epic("Ремонт", "Сделать ремонт");
      //  Epic epic2 = new Epic("Отпуск", "Организовать отдых");
      //  Subtask subtask1 = new Subtask("Поклеить обои", "Купить и поклеить", 1);
       // Subtask subtask2 = new Subtask("Залить пол", "Купить смесь и залить", 1);
       // Subtask subtask3 = new Subtask("Купить билеты", "Пойти в кассы", 1);

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

        //метод removeInHistory использует removeNode, но с ошибкой.
        // при этом метод removeNode отрабатывает корректно внутри метода add

    }
}
