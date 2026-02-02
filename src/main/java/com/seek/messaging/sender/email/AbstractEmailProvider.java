package com.seek.messaging.sender.email;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
public abstract class AbstractEmailProvider {
    protected String name;
    protected String endpoint;
    protected String contentType;

    public abstract String getForm(EmailPayload emailData);
}
