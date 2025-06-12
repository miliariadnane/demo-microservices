package dev.nano.exceptionhandler.payload;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String code;
    private String message;
    private String path;
    private List<ValidationError> errors;
}

