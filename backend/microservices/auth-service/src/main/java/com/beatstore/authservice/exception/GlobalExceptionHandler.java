package com.beatstore.authservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
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

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFound(RoleNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "ROLE_NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "DATA_INTEGRITY_VIOLATION",
                        "message", "Invalid input: " + ex.getMostSpecificCause().getMessage()
                ));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionError(TransactionSystemException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "TRANSACTION_ERROR",
                        "message", ex.getMostSpecificCause().getMessage()
                ));
    }
}
