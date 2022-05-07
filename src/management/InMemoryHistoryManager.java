package management;

import tasks.Epic;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Long, Node> hashMapHistory = new HashMap<>();

    public class Node {
        public Task task = null;
        public Node next = null;
        public Node prev = null;

        public Node(Node prev, Task task, Node next) {   // // Конструктор класса Node
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

        public Node() {
        }
        public Task getTask() {
            return task;
        }
        public Node getNext() {
            return next;
        }
        public void setNext(Node next) {
            this.next = next;
        }
        public Node getPrev() {
            return prev;
        }
        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }

    private Node head; // указатель на первый элемент списка (голова)
    private Node tail; // указатель на последний объект списка (хвост)
    private int size;

    public void linkLast(Task task) {
        final Node oldLastNode = tail;
        final Node newNode = new Node(oldLastNode, task, null);
        tail = newNode;
        if (oldLastNode == null)
            head = newNode;
        else
            oldLastNode.setNext(newNode);
        hashMapHistory.put(task.getId(), newNode); //обновление мапы
    }

    public void removeNode(Node node) {
        if (head == null && tail == null) {
            return;
        } else if (head.getNext() == null && tail.getPrev() == null) {
            head = tail = null;
        } else {
            if (node.getPrev() == null) {
                head = head.getNext();
                head.setPrev(null);
            } else if (node.getNext() == null) {
                tail = tail.getPrev();
                tail.setNext(null);
            } else {
                Node prevTaskNode = node.getPrev();
                Node nextTaskNode = node.getNext();
                prevTaskNode.setNext(nextTaskNode);
                nextTaskNode.setPrev(prevTaskNode);
            }
        }
        hashMapHistory.remove(node.getTask().getId());
    }

    public List<Task> getTasks() {
        List<Task> taskHistory = new ArrayList<>();
        for (Map.Entry<Long, Node> entry: hashMapHistory.entrySet()) {
            Node node = entry.getValue();
            Task task = node.task;
            taskHistory.add(task);
        }
        return taskHistory;
    }

    @Override
    public void add(Task task) {
        if (size == 0) {
            //addFirst(task);
            linkLast(task);
            hashMapHistory.put(task.getId(), head);
        } else if (hashMapHistory.containsKey(task.getId())) {
            removeNode(hashMapHistory.get(task.getId()));
            linkLast(task);
            hashMapHistory.put(task.getId(), tail);
            size--;
        } else linkLast(task);
        hashMapHistory.put(task.getId(), tail);
    }

    @Override
    public void removeInHistory(long id) {   // название изменил (не как в ТЗ)
        if (hashMapHistory.get(id).task instanceof Epic) {
            List<Long> idSubtaskList = ((Epic) hashMapHistory.get(id).task).getSubtaskList();
            removeNode(hashMapHistory.get(id));
            hashMapHistory.remove(id);
            for (Long key : idSubtaskList) {
                    removeNode(hashMapHistory.get(key));
                    hashMapHistory.remove(key);
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

