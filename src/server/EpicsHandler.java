package server;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
                        List<Epic> epics = taskManager.getEpics();
                        sendText(exchange, gson.toJson(epics), 200);
                    } else if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.epicSearch(id);
                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic), 200);
                        } else {
                            sendText(exchange, "Задача не найдена", 404);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == 0) {
                        taskManager.add(epic);
                        sendText(exchange, "Эпик добавлен", 201);
                    } else {
                        taskManager.update(epic);
                        sendText(exchange, "Эпик обновлен", 201);
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.epicRemove(id);
                        sendText(exchange, "Эпик удален", 201);
                    } else {
                        taskManager.clearEpic();
                        sendText(exchange, "Все эпики удалены", 201);
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