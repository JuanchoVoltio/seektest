package com.seek.messaging.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seek.messaging.model.MessageResult;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpRequestExecutor {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpRequestExecutor(HttpClient client) {
        this.client = client;
    }

    public CompletableFuture<MessageResult> execute(HttpRequest request) {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::mapResponse)
                .exceptionally(ex -> MessageResult.builder()
                        .success(false)
                        .message(null)
                        .error("Internal Error: " + ex.getMessage())
                        .timestamp(Instant.now())
                        .build());
    }

    private MessageResult mapResponse(HttpResponse<String> response) {
        String body = response.body();
        String responseMessage = "";
        String error = "";

        if (body != null && !body.isBlank()) {
            try {
                Map<String, String> mapped = mapper.readValue(body, Map.class);
                responseMessage = mapped.getOrDefault("message", "");
                error = mapped.getOrDefault("error", "");
            } catch (JsonProcessingException ex) {
                responseMessage = "Error: " + ex.getMessage() + ". " + body;
            }
        }

        boolean success = response.statusCode() >= 200 && response.statusCode() < 300;

        return MessageResult.builder()
                .success(success)
                .message(responseMessage.isBlank() ? null : responseMessage)
                .error(error.isBlank() ? null : error)
                .timestamp(Instant.now())
                .build();
    }
}
