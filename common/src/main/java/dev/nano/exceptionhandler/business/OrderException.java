package dev.nano.exceptionhandler.business;

import dev.nano.exceptionhandler.core.BaseException;

public class OrderException extends BaseException {
    public OrderException(String message) {
        super(message);
    }
}

