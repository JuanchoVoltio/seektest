package com.seek.messaging.sender.email;

import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;
import com.seek.messaging.sender.Channel;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

    @Mock
    SendGridEmailProvider sendGridEmailProviderMock;
    @Mock
    HttpClient httpClient = HttpClient.newHttpClient();
    @Mock
    HttpResponse<String> httpResponse;
    @Mock
    HttpResponse<String> webResponseMock;
    static MockWebServer server;
    EmailSender testSubject;

    @BeforeEach
    public void startUp() throws IOException {
        SendGridEmailProvider provider1 = SendGridEmailProvider.builder()
                .name("Send Grid").contentType("application/json").endpoint("https://sendgrid.endpoint").build();
        testSubject = new EmailSender(List.of(provider1), httpClient);
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void stopServer() throws Exception {
        server.shutdown();
    }


    @Test
    void send_shouldSendMessagesForAllProviders() throws ExecutionException, InterruptedException {
        //GIVEN
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("{\"message\":\"Queued.\"}");

        Map<String, String> payload = Map.of("from", "obi.wan@jedi.org",
                "to", "you@gmail.com",
                "subject", "Hello There",
                "message", "....");

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
        assertEquals(1, result.size());
        assertTrue(result.get(0).get().isSuccess());
    }
//    @Test
//    void send_shouldReturnAnErrorResponseWhenPayloadIsNotForEmail(){
//        //GIVEN
//        Map<String, String> payload = Map.of("from", "", "to", )
//        Message message = Message.builder()
//                .id("123")
//                .payload(Map.of())
//                .build();
//
//
//        //WHEN
//        testSubject.send(message);
//
//        //THEN
//    }
}