package server;

import com.sun.net.httpserver.HttpExchange;

import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                sendText(exchange, gson.toJson(prioritizedTasks), 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}