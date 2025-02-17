package server;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        List<Task> tasks = taskManager.getTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                    } else if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        Task task = taskManager.taskSearch(id);
                        if (task != null) {
                            sendText(exchange, gson.toJson(task), 200);
                        } else {
                            sendText(exchange, "Задача не найдена", 404);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    int taskAddCheck = taskManager.getTasks().size();
                    if (task.getId() == 0) {
                        taskManager.add(task);
                        if (taskManager.getTasks().size() != taskAddCheck) {
                            sendText(exchange, "Задача добавлена", 201);
                        } else {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        taskManager.update(task);
                        if (task.equals(taskManager.taskSearch(task.getId()))) {
                            sendText(exchange, "Задача обновлена", 201);
                        } else {
                            sendHasInteractions(exchange);
                        }
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.taskRemove(id);
                        sendText(exchange, "Задача удалена", 201);
                    } else {
                        taskManager.clearTasks();
                        sendText(exchange, "Все задачи удалены", 201);
                    }
                    break;
                default:
                    sendNotFound(exchange);
                    break;
            }
        } catch (Exception e) {
            sendText(exchange, "Ошибка сервера: " + e.getMessage(), 500);
        }
    }
}