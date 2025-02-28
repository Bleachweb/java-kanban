package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import statuses.Status;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest extends BaseHttpHandlerTest {

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.add(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, epic.getId());
        String subtaskJson = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.add(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, epic.getId());
        taskManager.add(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());

        assertEquals(1, subtasks.size());
    }
}