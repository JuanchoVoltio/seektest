package com.seek.messaging.sender;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SenderRegistry {
    private final Map<String, IMessageSender> senders = new ConcurrentHashMap<>();

    public void register(String name, IMessageSender sender) {
        senders.put(name, sender);
    }

    public Optional<IMessageSender> get(String name) {
        return Optional.ofNullable(senders.get(name));
    }

    public Optional<IMessageSender> getByChannel(Channel channel) {
        return senders.values().stream()
            .filter(s -> s.supportedChannel() == channel)
            .findFirst();
    }
}