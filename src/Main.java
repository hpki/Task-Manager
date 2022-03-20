import management.InMemoryTaskManager;
import management.Managers;
import management.TaskManager;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Managers manager1 = new Managers();

        TaskManager manager = manager1.getDefault();

        //TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Купить книгу", "Заказть книгу в интернете");
        Task task2 = new Task("Заняться спортом", "Бегать по утрам");

        Epic epic1 = new Epic("Ремонт", "Сделать ремонт");

        Subtask subtask1 = new Subtask("Поклеить обои", "Купить и поклеить", 1);
        Subtask subtask2 = new Subtask("Залить пол", "Купить смесь и залить", 1);

        manager.createTask(task1);
        manager.createTask(task2);
        System.out.println(manager.getTaskList());
        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(2));
        System.out.println("Последние 10 задач");
        System.out.println(manager.history());
        System.out.println("Всего задач: ");
        System.out.println(manager.history().size());

        manager.createEpic(epic1);
        System.out.println(manager.getEpicList());
        System.out.println(manager.getEpic(3));
        System.out.println("Последние 10 задач");
        System.out.println(manager.history());
        System.out.println("Всего задач: ");
        System.out.println(manager.history().size());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        System.out.println(manager.getSubtaskList());
        System.out.println(manager.getSubtask(4));
        System.out.println(manager.getSubtask(5));
        System.out.println("Последние 10 задач");
        System.out.println(manager.history());
        System.out.println("Всего задач: ");
        System.out.println(manager.history().size());

        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(2));
        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(2));
        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(2));
        System.out.println("Последние 10 задач");
        System.out.println(manager.history());
        System.out.println("Всего задач: ");
        System.out.println(manager.history().size());
    }
}
