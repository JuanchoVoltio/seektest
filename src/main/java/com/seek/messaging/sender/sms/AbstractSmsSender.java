package com.seek.messaging.sender.sms;

import com.seek.messaging.sender.email.AbstractEmailProvider;

import java.util.List;

public abstract class AbstractSmsSender {
    private List<AbstractSmsProvider> providers;

}
