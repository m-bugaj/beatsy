package com.beatstore.contentservice.exceptions;

import org.springframework.http.HttpStatus;

public class BeatNotFoundException extends DomainException {
    private static final ErrorCode errorCode = ErrorCode.BEAT_NOT_FOUND;
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public BeatNotFoundException(String userHash, String beatHash) {

        super(
                errorCode,
                httpStatus,
                String.format("Beat [%s] for user [%s] not found!", beatHash, userHash)
        );
    }
}
