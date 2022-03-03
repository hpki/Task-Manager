public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Manager manager = new Manager();

        Task task1 = new Task("Купить книгу", "Заказть книгу в интернете");
        Task task2 = new Task("Заняться спортом", "Бегать по утрам");

        Epic epic1 = new Epic("Ремонт", "Сделать ремонт");

        Subtask subtask1 = new Subtask("Поклеить обои", "Купить и поклеить", 1);
        Subtask subtask2 = new Subtask("Залить пол", "Купить смесь и залить", 1);

        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

         System.out.println(manager.epicList);
         System.out.println(manager.subtaskList);
        System.out.println(epic1.idSubtaskList);

        System.out.println(epic1.status);
        manager.updateSubtaskInProgress(subtask1);
        System.out.println(epic1.status);
        manager.updateSubtaskDone(subtask1);
        manager.updateSubtaskDone(subtask2);
        System.out.println(epic1.status);
    }
}
