package dev.nano.exceptionhandler.business;

import dev.nano.exceptionhandler.core.BaseException;

public class NotificationException extends BaseException {
    public NotificationException(String message) {
        super(message);
    }
}
