package com.seek.messaging.sender;

import com.seek.messaging.model.Message;
import com.seek.messaging.model.MessageResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IMessageSender {
    /**
     * Channel that this sender supports (can be more specific via config).
     */
    Channel supportedChannel();

    /**
     * Send a message asynchronously. Non-blocking, returns a CompletableFuture.
     */
    List<CompletableFuture<MessageResult>> send(Message message);
}