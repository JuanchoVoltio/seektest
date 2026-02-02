package com.seek.messaging.sender.sms.implexample;

import com.seek.messaging.sender.sms.AbstractSmsProvider;
import com.seek.messaging.sender.sms.SmsPayload;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TwilioSmsProvider extends AbstractSmsProvider {
    @Override
    public String getForm(SmsPayload smsData) {
        StringBuilder form = new StringBuilder();
        form.append("apikey=").append(this.apiKey);
        form.append("from=").append(smsData.getFrom());
        form.append("&to=").append(smsData.getTo());
        form.append("&text=").append(smsData.getText());

        return form.toString();
    }
}
