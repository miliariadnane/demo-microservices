package dev.nano.exceptionhandler.business;

import dev.nano.exceptionhandler.core.BaseException;

public class CustomerException extends BaseException {
    public CustomerException(String message) {
        super(message);
    }
}

