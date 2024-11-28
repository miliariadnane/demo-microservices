package dev.nano.notification.email;

public interface EmailService {
    void send(String to, String content, String subject);
}
