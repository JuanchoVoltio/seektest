package com.seek.messaging.sender.sms;

import com.seek.messaging.model.IPayload;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class SmsPayload implements IPayload {
    private String from;
    private String to;
    private String text;
}
