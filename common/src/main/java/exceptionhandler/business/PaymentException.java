package exceptionhandler.business;

import exceptionhandler.core.BaseException;

public class PaymentException extends BaseException {
    public PaymentException(String message) {
        super(message);
    }
}

