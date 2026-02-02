package com.seek.messaging.sender.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;
import com.seek.messaging.sender.Channel;
import com.seek.messaging.sender.IMessageSender;
import com.seek.messaging.sender.email.EmailPayload;
import com.seek.messaging.util.HttpRequestExecutor;
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

@Builder
@Getter
public class SmsSender implements IMessageSender {
    private List<AbstractSmsProvider> providers;
    private final HttpRequestExecutor httpRequestExecutor;

    @Override
    public Channel supportedChannel() {
        return Channel.SMS;
    }

    @Override
    public List<CompletableFuture<MessageResult>> send(Message message) {
        List<CompletableFuture<MessageResult>> results = new ArrayList<>();

        SmsPayload data = toSmsPayload(message.getPayload());

        providers.forEach(provider -> {
            String form = provider.getForm(data);
            String url = provider.getEndpoint() + "?" + form;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            CompletableFuture<MessageResult> current = httpRequestExecutor.execute(request);
            results.add(current);
        });

        return results;
    }

    private SmsPayload toSmsPayload(Map<String, String> payload) {
        return SmsPayload.builder()
                .from(payload.get("from").toString())
                .to(payload.get("to").toString())
                .text(payload.get("message"))
                .build();
    }
}
