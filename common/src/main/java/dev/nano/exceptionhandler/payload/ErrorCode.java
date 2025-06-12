package dev.nano.exceptionhandler.payload;

public enum ErrorCode {
    RESOURCE_NOT_FOUND("ERR_001"),
    VALIDATION_FAILED("ERR_002"),
    DUPLICATE_RESOURCE("ERR_003"),
    BAD_REQUEST("ERR_004"),
    INTERNAL_ERROR("ERR_005");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

