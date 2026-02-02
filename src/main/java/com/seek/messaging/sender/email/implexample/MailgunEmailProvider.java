package com.seek.messaging.sender.email.implexample;

import com.seek.messaging.sender.email.AbstractEmailProvider;
import com.seek.messaging.sender.email.EmailPayload;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MailgunEmailProvider extends AbstractEmailProvider {

    @Override
    public String getForm(EmailPayload emailData) {
        StringBuilder form = new StringBuilder();
        form.append("from=").append(emailData.getFrom());
        form.append("&to=").append(emailData.getTo());
        form.append("&subject=").append(emailData.getSubject());
        form.append("&message-content=").append(emailData.getText());

        return form.toString();
    }
}
