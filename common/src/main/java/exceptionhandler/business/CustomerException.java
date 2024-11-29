package exceptionhandler.business;

import exceptionhandler.core.BaseException;

public class CustomerException extends BaseException {
    public CustomerException(String message) {
        super(message);
    }
}

