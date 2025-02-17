package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends BaseHttpHandlerTest {

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1");
        taskManager.add(task);
        taskManager.taskSearch(task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(1, history.size());
    }
}