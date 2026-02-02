package com.seek.messaging.sender.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seek.messaging.sender.Channel;
import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;
import com.seek.messaging.sender.IMessageSender;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

@Builder
@Getter
public class EmailSender implements IMessageSender {

    private List<AbstractEmailProvider> providers;
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Channel supportedChannel() {
        return Channel.EMAIL;
    }

    @Override
    public List<CompletableFuture<MessageResult>> send(Message message) {
        List<CompletableFuture<MessageResult>> results = new ArrayList<>();

        EmailPayload emailData = toEmailPayload(message.getPayload());

        providers.forEach(provider -> {
            String endpoint = provider.getEndpoint();
            String form = provider.getForm(emailData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", provider.getContentType())
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            var current = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        String body = response.body();
                        String responseMessage = "";
                        String error = "";

                        if(body != null && !body.isBlank()){
                            try {
                                Map<String, String> mappedBody = mapper.readValue(body, Map.class);
                                if(mappedBody.containsKey("message")){
                                    responseMessage = mappedBody.get("message");
                                }
                                if(mappedBody.containsKey("error")){
                                    error = mappedBody.get("error");
                                }
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        return MessageResult.builder()
                                .success(response.statusCode() == 200)
                                .message(responseMessage)
                                .error(error)
                                .timestamp(Instant.now())
                                .build();
                    }).exceptionally(ex -> {
                        return MessageResult.builder()
                                .success(false)
                                .message(null)
                                .message("Internal Error: " + ex.getMessage())
                                .timestamp(Instant.now())
                                .build();
                    });

            results.add(current);
        });

        return results;
    }

    private EmailPayload toEmailPayload(Map<String, String> payload) {
        return EmailPayload.builder()
                .from(payload.get("from").toString())
                .to(payload.get("to").toString())
                .subject(payload.get("subject"))
                .text(payload.get("message"))
                .build();
    }
}
