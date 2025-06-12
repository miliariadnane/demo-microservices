package dev.nano.exceptionhandler.business;

import dev.nano.exceptionhandler.core.BaseException;

public class PaymentException extends BaseException {
    public PaymentException(String message) {
        super(message);
    }
}

