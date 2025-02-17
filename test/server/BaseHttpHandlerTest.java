package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import statuses.Status;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandlerTest {
    protected HttpTaskServer server;
    protected TaskManager taskManager;
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(Status.class, new StatusAdapter())
            .create();
    protected final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }
}