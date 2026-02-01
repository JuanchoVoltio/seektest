package com.seek.messaging.http;

import java.net.http.*;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seek.messaging.Channel;
import com.seek.messaging.Message;
import com.seek.messaging.MessageResult;
import com.seek.messaging.MessageSender;

public class HttpWebhookSender implements MessageSender {
    private final URI endpoint;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpWebhookSender(URI endpoint, HttpClient client) {
        this.endpoint = endpoint;
        this.client = client;
    }

    @Override
    public Channel supportedChannel() { return Channel.WEBHOOK; }

    @Override
    public CompletableFuture<MessageResult> send(Message message) {
        try {
            String body = mapper.writeValueAsString(message.getPayload());
            HttpRequest req = HttpRequest.newBuilder()
                .uri(endpoint)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

            return client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    boolean ok = r.statusCode() >= 200 && r.statusCode() < 300;
                    return new MessageResult(ok,
                        // provider id absent; use response header or body
                        r.headers().firstValue("X-Request-Id").orElse(null),
                        ok ? null : "HTTP " + r.statusCode() + ": " + r.body(),
                        Instant.now(),
                        Map.of("statusCode", String.valueOf(r.statusCode())));
                });

        } catch (Exception e) {
            CompletableFuture<MessageResult> cf = new CompletableFuture<>();
            cf.completeExceptionally(e);
            return cf;
        }
    }
}