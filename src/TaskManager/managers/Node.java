package TaskManager.managers;

import TaskManager.tasks.Task;

public class Node {
    public Task task;
    Node next;

    public Node(Task task) {
        this.task = task;
        this.next = null;
    }
}
