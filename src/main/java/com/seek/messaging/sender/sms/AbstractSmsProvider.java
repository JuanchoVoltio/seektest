package com.seek.messaging.sender.sms;

import com.seek.messaging.model.IProvider;
import com.seek.messaging.sender.email.EmailPayload;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class AbstractSmsProvider implements IProvider<String, SmsPayload>{

    protected String name;
    protected String endpoint;
    protected String apiKey;

}
