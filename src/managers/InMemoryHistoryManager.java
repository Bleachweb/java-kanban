package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int taskId) {
        if (historyList.taskMap.containsKey(taskId)) {
            historyList.removeNode(historyList.taskMap.get(taskId));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    private class CustomLinkedList {
        private HashMap<Integer, Node> taskMap = new HashMap<>();
        private Node head;
        private Node tail;

        public void linkLast(Task task) {
            int taskId = task.getId();
            if (taskMap.containsKey(taskId)) {
                remove(taskId);
            }
            Node newNode = new Node(task);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
            }
            taskMap.put(taskId, newNode);
        }

        public void removeNode(Node node) {
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            } else {
                head = node.getNext();
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            } else {
                tail = node.getPrev();
            }
            taskMap.remove(node.getTask().getId());
        }

        public List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node current = head;
            while (current != null) {
                history.add(current.getTask());
                current = current.getNext();
            }
            return history;
        }

    }
}