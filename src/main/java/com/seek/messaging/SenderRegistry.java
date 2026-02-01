package com.seek.messaging;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SenderRegistry {
    private final Map<String, MessageSender> senders = new ConcurrentHashMap<>();

    public void register(String name, MessageSender sender) {
        senders.put(name, sender);
    }

    public Optional<MessageSender> get(String name) {
        return Optional.ofNullable(senders.get(name));
    }

    public Optional<MessageSender> getByChannel(Channel channel) {
        return senders.values().stream()
            .filter(s -> s.supportedChannel() == channel)
            .findFirst();
    }
}