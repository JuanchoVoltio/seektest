package com.seek.messaging.sender.email;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class EmailPayload {
    String from;
    String to;
    String subject;
    String text;
}
