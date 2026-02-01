package com.seek.messaging.sender.email;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SendGridEmailProvider extends AbstractEmailProvider {

    @Override
    public String getForm(EmailPayload emailData) {
        StringBuilder form = new StringBuilder();
        form.append("from=").append(emailData.getFrom());
        form.append("&to=").append(emailData.getTo());
        form.append("&subject=").append(emailData.getSubject());

        return form.toString();
    }
}
