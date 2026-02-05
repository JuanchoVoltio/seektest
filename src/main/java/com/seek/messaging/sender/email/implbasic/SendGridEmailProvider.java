package com.seek.messaging.sender.email.implbasic;

import com.seek.messaging.sender.email.AbstractEmailProvider;
import com.seek.messaging.sender.email.EmailPayload;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SendGridEmailProvider extends AbstractEmailProvider {

    @Override
    public String getRequestParams(EmailPayload emailData) {
        StringBuilder form = new StringBuilder();
        form.append("from=").append(emailData.getFrom());
        form.append("&to=").append(emailData.getTo());
        form.append("&subject=").append(emailData.getSubject());
        form.append("&text=").append(emailData.getText());

        return form.toString();
    }
}
