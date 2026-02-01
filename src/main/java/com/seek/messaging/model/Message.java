package com.seek.messaging;

import java.util.Map;
import java.util.Optional;

public final class Message {
    private final String id; // optional id for idempotency
    private final Channel channel;
    private final Map<String, String> headers;
    private final Object payload; // can be a DTO, String, Map, etc.

    public Message(String id, Channel channel, Map<String, String> headers, Object payload) {
        this.id = id;
        this.channel = channel;
        this.headers = Map.copyOf(headers);
        this.payload = payload;
    }

    public Optional<String> getId() { return Optional.ofNullable(id); }
    public Channel getChannel() { return channel; }
    public Map<String,String> getHeaders() { return headers; }
    public Object getPayload() { return payload; }
}