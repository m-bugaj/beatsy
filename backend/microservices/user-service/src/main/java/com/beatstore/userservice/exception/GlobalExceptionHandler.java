package com.beatstore.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserSessionExpired.class)
    public ResponseEntity<Object> handleSessionExpired(UserSessionExpired ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "SESSION_EXPIRED",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(UserSessionNotFound.class)
    public ResponseEntity<Object> handleSessionNotFound(UserSessionNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "SESSION_NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }
}
