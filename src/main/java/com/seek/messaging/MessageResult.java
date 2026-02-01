package com.seek.messaging;

import java.time.Instant;
import java.util.Map;

public final class MessageResult {
    private final boolean success;
    private final String providerMessageId;
    private final String error;
    private final Instant timestamp;
    private final Map<String, String> metadata;

    public MessageResult(boolean success, String providerMessageId, String error, Instant timestamp, Map<String,String> metadata) {
        this.success = success;
        this.providerMessageId = providerMessageId;
        this.error = error;
        this.timestamp = timestamp;
        this.metadata = Map.copyOf(metadata);
    }

    public boolean isSuccess() { return success; }
    public String getProviderMessageId() { return providerMessageId; }
    public String getError() { return error; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String,String> getMetadata() { return metadata; }
}