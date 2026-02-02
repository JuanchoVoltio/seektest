package com.seek.messaging.model;

import com.seek.messaging.sender.Channel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Builder
@Getter
public final class Message {
    private final String id; // optional id for idempotency
    private final Channel channel;
    private final Map<String, String> headers;
    private final Map<String, String> payload;

    public Message(String id, Channel channel, Map<String, String> headers, Map<String, String> payload) {
        this.id = id;
        this.channel = channel;
        this.headers = Map.copyOf(headers);
        this.payload = payload;
    }
}