package dev.nano.notification.aws;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import dev.nano.notification.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile("eks")
public class AWSEmailService implements EmailService {

    private final AmazonSimpleEmailService emailService;

    public void send(String to, String content, String subject) {
        try {
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset(AWSConstant.UTF_ENCODING).withData(content)))
                            .withSubject(new Content()
                                    .withCharset(AWSConstant.UTF_ENCODING).withData(subject)))
                    .withSource(AWSConstant.SENDER);
            emailService.sendEmail(request);
        } catch (AmazonSimpleEmailServiceException e) {
            throw new IllegalStateException(AWSConstant.SEND_EMAIL_FAILED_EXCEPTION, e);
        }
    }
}
