package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import statuses.Status;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(Status.class, new StatusAdapter())
            .create();

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "Not Found";
        sendText(exchange, response, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Task has time intersections";
        sendText(exchange, response, 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        String response = "Internal Server Error";
        sendText(exchange, response, 500);
    }
}