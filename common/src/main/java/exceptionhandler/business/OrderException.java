package exceptionhandler.business;

import exceptionhandler.core.BaseException;

public class OrderException extends BaseException {
    public OrderException(String message) {
        super(message);
    }
}

