package dev.nano.notification.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!eks")
@Slf4j
public class DefaultEmailService implements EmailService {
    @Override
    public void send(String to, String content, String subject) {
        log.info("Simulated email sent to: {}, subject: {}, content: {}", to, subject, content);
    }
}
