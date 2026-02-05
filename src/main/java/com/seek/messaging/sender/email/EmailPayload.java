package com.seek.messaging.sender.email;

import com.seek.messaging.model.IPayload;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class EmailPayload implements IPayload {
    String from;
    String to;
    String subject;
    String text;
}
