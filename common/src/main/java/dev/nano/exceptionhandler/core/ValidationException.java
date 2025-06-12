package dev.nano.exceptionhandler.core;

import dev.nano.exceptionhandler.payload.ValidationError;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends BaseException {
    private final List<ValidationError> errors;

    public ValidationException(String message, List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }
}

