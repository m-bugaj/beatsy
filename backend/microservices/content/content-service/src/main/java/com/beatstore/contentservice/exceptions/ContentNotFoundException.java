package com.beatstore.contentservice.exceptions;

import org.springframework.http.HttpStatus;

public class ContentNotFoundException extends DomainException {
    private static final ErrorCode errorCode = ErrorCode.CONTENT_NOT_FOUND;
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ContentNotFoundException(String contentHash) {

        super(
                errorCode,
                httpStatus,
                String.format("Content with hash [%s] not found!", contentHash)
        );
    }
}
