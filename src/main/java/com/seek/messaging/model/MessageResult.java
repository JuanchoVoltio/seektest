package com.seek.messaging;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Builder
@Getter
public final class MessageResult {
    private final boolean success;
    private String message;
    private final Instant timestamp;
    private String error;
}