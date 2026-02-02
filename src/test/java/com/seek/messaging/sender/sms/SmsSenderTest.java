package com.seek.messaging.sender.sms;

import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsSenderTest {
    @Mock
    HttpClient httpClient = HttpClient.newHttpClient();
    @Mock
    HttpResponse<String> httpResponse;
    SmsSender testSubject;

    @BeforeEach
    public void startUp() throws IOException {
        TwilioSmsProvider provider1 = TwilioSmsProvider.builder()
                .name("Twilio").endpoint("https://twilio.endpoint/").apiKey("encrypted-key").build();
        TwilioSmsProvider provider2 = TwilioSmsProvider.builder()
                .name("Twilio").endpoint("https://twilio2.endpoint/").apiKey("encrypted-key2").build();
        testSubject = new SmsSender(List.of(provider1, provider2), httpClient);
    }

    @Test
    void send_shouldSendMessagesForAllProviders() throws ExecutionException, InterruptedException {
        //GIVEN
        when(httpResponse.statusCode()).thenReturn(200).thenReturn(400);
        when(httpResponse.body()).thenReturn("{\"message\":\"General Kenobi!\"}").thenReturn("{\"message\":\"Queued.\"}");

        Map<String, String> payload = Map.of("from", "54911121",
                "to", "57115212",
                "subject", "Aloha",
                "message", "Hello%20There");

        Message message = Message.builder()
                .id("123")
                .payload(payload)
                .headers(Map.of())
                .build();

        CompletableFuture<HttpResponse<String>> completed = CompletableFuture.completedFuture(httpResponse);

        when(httpClient.sendAsync(any(HttpRequest.class), Mockito.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(completed);


        //WHEN
        List<CompletableFuture<MessageResult>> result = testSubject.send(message);

        //THEN
        assertEquals(2, result.size());
        assertTrue(result.get(0).get().isSuccess());
        assertFalse(result.get(1).get().isSuccess());
    }
}