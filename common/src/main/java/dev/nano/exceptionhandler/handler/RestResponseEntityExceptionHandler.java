package dev.nano.exceptionhandler.handler;

import dev.nano.exceptionhandler.business.CustomerException;
import dev.nano.exceptionhandler.business.NotificationException;
import dev.nano.exceptionhandler.business.OrderException;
import dev.nano.exceptionhandler.business.PaymentException;
import dev.nano.exceptionhandler.business.ProductException;
import dev.nano.exceptionhandler.core.BaseException;
import dev.nano.exceptionhandler.core.DuplicateResourceException;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import dev.nano.exceptionhandler.core.ValidationException;
import dev.nano.exceptionhandler.payload.ErrorCode;
import dev.nano.exceptionhandler.payload.ErrorDetails;
import dev.nano.exceptionhandler.payload.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(),
                ErrorCode.RESOURCE_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleValidationException(
            ValidationException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(),
                ErrorCode.VALIDATION_FAILED,
                HttpStatus.BAD_REQUEST,
                request,
                ex.getErrors());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDetails> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(),
                ErrorCode.DUPLICATE_RESOURCE,
                HttpStatus.CONFLICT,
                request);
    }

    @ExceptionHandler({
            CustomerException.class,
            OrderException.class,
            PaymentException.class,
            ProductException.class,
            NotificationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleBusinessExceptions(
            BaseException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(),
                ErrorCode.BAD_REQUEST,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(),
                ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    private ResponseEntity<ErrorDetails> buildErrorResponse(
            String message,
            ErrorCode errorCode,
            HttpStatus status,
            WebRequest request) {
        return buildErrorResponse(message, errorCode, status, request, null);
    }

    private ResponseEntity<ErrorDetails> buildErrorResponse(
            String message,
            ErrorCode errorCode,
            HttpStatus status,
            WebRequest request,
            List<ValidationError> errors) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(message)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorDetails, status);
    }
}

