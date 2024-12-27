package taskManager.managers;

import taskManager.tasks.Task;

public class Node {
    public Task task;
    Node next;

    public Node(Task task) {
        this.task = task;
        this.next = null;
    }
}
