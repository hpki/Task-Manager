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

        // Task task1 = new Task("Купить книгу", "Заказть книгу в интернете");
        // Task task2 = new Task("Заняться спортом", "Бегать по утрам");

        Epic epic1 = new Epic("Ремонт", "Сделать ремонт");
        Epic epic2 = new Epic("Отпуск", "Организовать отдых");
        Subtask subtask1 = new Subtask("Поклеить обои", "Купить и поклеить", 1);
        Subtask subtask2 = new Subtask("Залить пол", "Купить смесь и залить", 1);
        Subtask subtask3 = new Subtask("Купить билеты", "Пойти в кассы", 1);

        manager.createEpic(epic1);
        System.out.println(manager.getEpic(1));
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        System.out.println(manager.getSubtaskList());
        System.out.println(manager.getEpic(1));
        manager.updateSubtask(subtask3);
        System.out.println(manager.getSubtaskList());
        System.out.println(manager.getEpic(1));
        manager.remove(4);
        System.out.println("///////////////////////////////////////////////////");
        System.out.println(manager.getSubtaskList());
        System.out.println(manager.getEpic(1));
        //System.out.println(manager.getSubtaskList());
        //System.out.println(manager.getEpic(1));
        // System.out.println(manager.getEpic(1));
        //System.out.println(manager.getSubtaskList());
    }
}
