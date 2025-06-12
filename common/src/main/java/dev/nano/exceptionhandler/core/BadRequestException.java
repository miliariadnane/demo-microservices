package dev.nano.exceptionhandler.core;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message);
    }
}
