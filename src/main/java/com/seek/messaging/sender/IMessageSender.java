package com.seek.messaging.sender.http;

import com.seek.messaging.Channel;
import com.seek.messaging.Message;
import com.seek.messaging.MessageResult;

import java.util.concurrent.CompletableFuture;

public interface MessageSender {
    /**
     * Channel that this sender supports (can be more specific via config).
     */
    Channel supportedChannel();

    /**
     * Send a message asynchronously. Non-blocking, returns a CompletableFuture.
     */
    CompletableFuture<MessageResult> send(Message message);
}