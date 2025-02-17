package server;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
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
                        List<Subtask> subtasks = taskManager.getSubtasks();
                        sendText(exchange, gson.toJson(subtasks), 200);
                    } else if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        Subtask subtask = taskManager.subtaskSearch(id);
                        if (subtask != null) {
                            sendText(exchange, gson.toJson(subtask), 200);
                        } else {
                            sendText(exchange, "Задача не найдена", 404);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    int subtaskAddCheck = taskManager.getSubtasks().size();
                    if (subtask.getId() == 0) {
                        taskManager.add(subtask);
                        if (taskManager.getSubtasks().size() != subtaskAddCheck) {
                            sendText(exchange, "Подзадача добавлена", 201);
                        } else {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        taskManager.update(subtask);
                        if (subtask.equals(taskManager.subtaskSearch(subtask.getId()))) {
                            sendText(exchange, "Подзадача обновлена", 201);
                        } else {
                            sendHasInteractions(exchange);
                        }
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.subtaskRemove(id);
                        sendText(exchange, "Подзадача удалена", 200);
                    } else {
                        taskManager.clearSubtasks();
                        sendText(exchange, "Все подзадачи удалены", 200);
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