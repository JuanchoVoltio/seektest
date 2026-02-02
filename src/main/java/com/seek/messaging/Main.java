package com.seek.messaging;

import com.seek.messaging.sender.SenderRegistry;
import com.seek.messaging.sender.email.EmailSender;
import com.seek.messaging.sender.email.implexample.SendGridEmailProvider;
import com.seek.messaging.util.HttpRequestExecutor;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SenderRegistry registry = new SenderRegistry();
        // register a simple email sender
        var emailSender = EmailSender.builder()
                .httpRequestExecutor(new HttpRequestExecutor(HttpClient.newHttpClient()))
                .providers(List.of(SendGridEmailProvider.builder()
                                .name("sendgrid")
                                .contentType("application/json")
                                .endpoint("https://sendgrid.com/v2/")
                                .build()))
                .build();

        registry.register("email-default", emailSender, true);

    }
}
