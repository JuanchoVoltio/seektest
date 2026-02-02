package com.seek.messaging.sender.sms;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class SmsPayload {
    private String from;
    private String to;
    private String text;
}
