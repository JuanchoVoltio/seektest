package com.seek.messaging.util;

import com.seek.messaging.sender.email.EmailSender;
import com.seek.messaging.sender.email.MailgunEmailProvider;
import com.seek.messaging.sender.email.SendGridEmailProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpRequestExecutorTest {

    @Mock
    HttpClient httpClientMock = HttpClient.newHttpClient();
    @Mock
    HttpResponse<String> httpResponseMock;
    HttpRequestExecutor testSubject;

    @BeforeEach
    public void startUp(){
        testSubject  = new HttpRequestExecutor(httpClientMock);
    }

    @Test
    void testExecuteForPost() throws ExecutionException, InterruptedException {
        //GIVEN
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://endpoint/"))
                .header("Content-Type", "json")
                .POST(HttpRequest.BodyPublishers.ofString("form"))
                .build();

        CompletableFuture<HttpResponse<String>> completed = CompletableFuture.completedFuture(httpResponseMock);

        when(httpResponseMock.statusCode()).thenReturn(200).thenReturn(400);
        when(httpResponseMock.body()).thenReturn("{\"message\":\"General Kenobi!\"}").thenReturn("{\"message\":\"Queued.\"}");
        when(httpClientMock.sendAsync(any(HttpRequest.class), Mockito.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(completed);

        //WHEN
        var result = testSubject.execute(request);

        //THEN
        assertNotNull(result);
        assertEquals("General Kenobi!", result.get().getMessage());
    }

}