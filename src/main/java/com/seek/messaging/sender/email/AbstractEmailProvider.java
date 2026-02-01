package com.seek.messaging.sender.email;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public abstract class AbstractEmailProvider {
    private String name;
    private String endpoint;
    private String contentType;

    public abstract String getForm(EmailPayload emailData);
}
