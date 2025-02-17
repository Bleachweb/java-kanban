package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import statuses.Status;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static statuses.Status.NEW;

class TasksHandlerTest extends BaseHttpHandlerTest {

    @Test
    void testTaskAddAndTaskHasIntersection() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1", NEW, 30, LocalDateTime.now());
        Task taskIntersection = new Task("Task 2", "Description 2", NEW, 30, LocalDateTime.now().plusMinutes(5));
        String taskJson = gson.toJson(task);
        String taskIntersectionJson = gson.toJson(taskIntersection);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskIntersectionJson))
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response1.statusCode());
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1");
        taskManager.add(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(1, tasks.size());
    }


    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1");
        taskManager.add(task);
        task.setStatus(Status.IN_PROGRESS);
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(Status.IN_PROGRESS, taskManager.taskSearch(task.getId()).getStatus());
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1");
        taskManager.add(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }
}