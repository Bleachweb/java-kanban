package server;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = Managers.getDefault();
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.httpServer.createContext("/tasks", new TasksHandler(taskManager));
        this.httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        this.httpServer.createContext("/epics", new EpicsHandler(taskManager));
        this.httpServer.createContext("/history", new HistoryHandler(taskManager));
        this.httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault(); // Здесь должен быть ваш TaskManager
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}