package com.seek.messaging;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Map;

public class ExampleUsage {
    public static void main(String[] args) throws Exception {
        SenderRegistry registry = new SenderRegistry();
        // register a simple webhook sender
        var webhook = new com.seek.messaging.http.HttpWebhookSender(URI.create("https://example.com/webhook"), HttpClient.newHttpClient());
        registry.register("webhook-default", webhook);

        Message msg = new Message("id-123", Channel.WEBHOOK, Map.of(), Map.of("text", "hello"));
        var sender = registry.get("webhook-default").orElseThrow();
        sender.send(msg)
            .thenAccept(result -> {
                if (result.isSuccess()) System.out.println("Sent ok");
                else System.err.println("Failed: " + result.getError());
            }).join();
    }
}