package com.seek.messaging.sender.sms;

import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;
import com.seek.messaging.sender.sms.implexample.TwilioSmsProvider;
import com.seek.messaging.util.HttpRequestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
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
    HttpRequestExecutor httpRequestExecutorMock;
    SmsSender testSubject;

    @BeforeEach
    public void startUp() throws IOException {
        TwilioSmsProvider provider1 = TwilioSmsProvider.builder()
                .name("Twilio").endpoint("https://twilio.endpoint/").apiKey("encrypted-key").build();
        TwilioSmsProvider provider2 = TwilioSmsProvider.builder()
                .name("Twilio").endpoint("https://twilio2.endpoint/").apiKey("encrypted-key2").build();
        testSubject = new SmsSender(List.of(provider1, provider2), httpRequestExecutorMock);
    }
    @Test
    void send_shouldSendMessagesForAllProviders() throws ExecutionException, InterruptedException {
        //GIVEN
        Map<String, String> payload = Map.of("from", "54911121",
                "to", "57115212",
                "subject", "Aloha",
                "message", "Hello%20There");

        Message message = Message.builder()
                .id("123")
                .payload(payload)
                .headers(Map.of())
                .build();

        CompletableFuture<MessageResult> completedOk = CompletableFuture.completedFuture(MessageResult.builder().success(true).build());
        CompletableFuture<MessageResult> completedFail = CompletableFuture.completedFuture(MessageResult.builder().success(false).build());

        when(httpRequestExecutorMock.execute(any())).thenReturn(completedOk).thenReturn(completedFail);

        //WHEN
        List<CompletableFuture<MessageResult>> result = testSubject.send(message);

        //THEN
        assertEquals(2, result.size());
        assertTrue(result.get(0).get().isSuccess());
        assertFalse(result.get(1).get().isSuccess());
    }
}