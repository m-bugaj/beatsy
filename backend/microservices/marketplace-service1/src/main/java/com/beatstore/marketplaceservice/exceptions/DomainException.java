package com.beatstore.marketplaceservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DomainException extends RuntimeException {

    private final ErrorCode errorCode;
    private final int httpStatus;

    protected DomainException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus.value();
    }

    protected DomainException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus.value();
    }

}
