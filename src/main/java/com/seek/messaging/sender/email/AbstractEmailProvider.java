package com.seek.messaging.sender.email;

import com.seek.messaging.model.IProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
public abstract class AbstractEmailProvider implements IProvider<String, EmailPayload> {
    protected String name;
    protected String endpoint;
    protected String contentType;

}
