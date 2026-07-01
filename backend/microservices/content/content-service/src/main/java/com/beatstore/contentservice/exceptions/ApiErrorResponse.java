package com.beatstore.contentservice.exceptions;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final int code;
    private final String error;
    private final String message;

    public ApiErrorResponse(int status, ErrorCode errorCode, String message) {
        this.status = status;
        this.code = errorCode.getCode();
        this.error = errorCode.name();
        this.message = message;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public int getCode() { return code; }
    public String getError() { return error; }
    public String getMessage() { return message; }
}
