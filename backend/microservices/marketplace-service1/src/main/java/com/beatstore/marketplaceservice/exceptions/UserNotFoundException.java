package com.beatstore.marketplaceservice.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {
    private static final ErrorCode errorCode = ErrorCode.BEAT_NOT_FOUND;
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public UserNotFoundException(String userHash) {

        super(
                errorCode,
                httpStatus,
                String.format("User [%s] not found!", userHash)
        );
    }
}
