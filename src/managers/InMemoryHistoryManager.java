package managers;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {



    private HashMap<Integer, Node> taskMap; // HashMap для хранения ID задач и узлов
    private Node head;

    public InMemoryHistoryManager() {
        taskMap = new HashMap<>();
        head = null;
    }

    @Override
    public HashMap<Integer, Node> getTaskMap() {
        return taskMap;
    }


    public void updateTask(Task updatedTask) {
        int id = updatedTask.getId();
        if (taskMap.containsKey(id)) {
            Node nodeToUpdate = taskMap.get(id);
            nodeToUpdate.task = updatedTask;
            taskMap.put(id,nodeToUpdate);
        }
    }

    @Override
    public void addTask(Task task) {
        Node newNode = new Node(task);
        int taskId = task.getId();
        if (taskMap.containsKey(taskId)) {
            remove(taskId);
        }
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        taskMap.put(taskId, newNode);
    }

    @Override
    public void remove(int taskId) {
        Node nodeToRemove = taskMap.get(taskId);

        if (nodeToRemove == null) {
            return;
        }

        if (head == nodeToRemove) {
            head = head.next;
        } else {
            Node current = head;
            while (current != null && current.next != nodeToRemove) {
                current = current.next;
            }

            if (current != null) {
                current.next = nodeToRemove.next;
            }
        }

        taskMap.remove(taskId);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;

        while (current != null) {
            history.add(current.task);
            current = current.next;
        }

        return history;
    }

}