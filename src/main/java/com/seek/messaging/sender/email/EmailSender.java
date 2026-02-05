package com.seek.messaging.sender.email;

import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;
import com.seek.messaging.sender.Channel;
import com.seek.messaging.sender.IMessageSender;
import com.seek.messaging.util.HttpRequestExecutor;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Builder
@Getter
public class EmailSender  implements IMessageSender {

    private List<AbstractEmailProvider> providers;
    private final HttpRequestExecutor httpRequestExecutor;

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
            String form = provider.getRequestParams(emailData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", provider.getContentType())
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            CompletableFuture<MessageResult> current = httpRequestExecutor.execute(request);
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
