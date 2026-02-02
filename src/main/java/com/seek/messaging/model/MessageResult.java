package com.seek.messaging.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Builder
@Getter
public final class MessageResult {
    private final boolean success;
    private String message;
    private String error;
    private final Instant timestamp;
}