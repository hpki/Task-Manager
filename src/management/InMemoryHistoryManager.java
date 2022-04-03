package management;

import tasks.Epic;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    public List<Task> browsingHistory = new LinkedList(); // массив для хранения последних 10-ти задач

    HashMap<Long, Node> hashMapHistory = new HashMap<>();

    public class Node {
        public Task task;
        public Node next;
        public Node prev;

        public Node(Node prev, Task task, Node next) {   // // Конструктор класса Node
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node head; // указатель на первый элемент списка (голова)
    private Node tail; // указатель на последний объект списка (хвост)
    private int size;

    public void addFirst(Task task) {
        final Node oldHead = head;
        final Node newNode = new Node(null, task, oldHead);
        head = newNode;
        if (oldHead == null)
            tail = newNode;
        else
            oldHead.prev = newNode;
        size++;
    }

    public void linkLast(Task task) {  // добавляет элемент в конец списка
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            tail = newNode;
        else
            oldTail.prev = newNode;
        size++;
    }


    public List<Task> getTasks() {
        List<Task> taskHistory = new ArrayList<>();
        Node node = head;
        while (node != null) {
            taskHistory.add((Task) node.task);
            node = node.next;
        }
        return taskHistory;
    }

    @Override
    public void add(Task task) {
        if (size == 0) {
            addFirst(task);
            hashMapHistory.put(task.getId(), head);
        } else if (hashMapHistory.containsKey(task.getId())) {
            removeNode(hashMapHistory.get(task.getId()));
            linkLast(task);
            hashMapHistory.put(task.getId(), tail);
            size--;
        } else linkLast(task);
        hashMapHistory.put(task.getId(), tail);
    }

    public void removeNode(Node node) {
        if (node.next == null) {      //если node является tail
            tail = node.prev;
            node.prev.next = null;
            node = null;
        } else if (node.prev == null) {    //если node является head
            node.next.prev = null;
            head = node.next;
            node = null;
        } else {
            Node nodePrev = node.prev;       //если node находится в середине
            Node nodeNext = node.next;
            node.prev.next = nodeNext;
            // node.next.prev = nodePrev;
            node = null;
        }
    }

    @Override
    public void removeInHistory(long id) {   // название изменил (не как в ТЗ
        if (hashMapHistory.get(id).task instanceof Epic) {
            hashMapHistory.remove(id);
            removeNode(hashMapHistory.get(id));
            List<Long> idSubtaskList = new ArrayList<>();
            idSubtaskList = ((Epic) hashMapHistory.get(id).task).getSubtaskList();
            for (Long key : idSubtaskList) {
                if (key == id) {
                    hashMapHistory.remove(id);
                    removeNode(hashMapHistory.get(id));
                }
            }
        } else hashMapHistory.remove(id);
                removeNode(hashMapHistory.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override   // для проверки
    public int getSize() {
        return hashMapHistory.size();
    }

}

