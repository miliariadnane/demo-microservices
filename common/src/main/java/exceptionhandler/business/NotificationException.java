package exceptionhandler.business;

import exceptionhandler.core.BaseException;

public class NotificationException extends BaseException {
    public NotificationException(String message) {
        super(message);
    }
}
