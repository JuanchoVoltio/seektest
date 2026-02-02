package com.seek.messaging.sender;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SenderRegistry {
    private final Map<String, IMessageSender> senders = new ConcurrentHashMap<>();
    private final Set<Channel> defaultChannels = new HashSet();

    public void register(String name, IMessageSender sender, boolean isDefault)
    {
        senders.put(name, sender);
        if(isDefault){
            defaultChannels.add(sender.supportedChannel());
        }
    }

    public Optional<IMessageSender> get(String name) {
        return Optional.ofNullable(senders.get(name));
    }

    public Optional<IMessageSender> getByChannel(Channel channel) {
        return senders.values().stream()
            .filter(s -> s.supportedChannel() == channel)
            .findFirst();
    }

    public Optional<List<IMessageSender>> getByDefaultChannels(){
        return Optional.of(senders.values().stream()
                .filter(s -> defaultChannels.contains(s.supportedChannel())).toList());
    }
}