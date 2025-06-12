package dev.nano.exceptionhandler.core;

public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
}
