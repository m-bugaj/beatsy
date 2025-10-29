package com.beatstore.marketplaceservice.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BEAT_NOT_FOUND(1, "Beat not found!"),
    INTERNAL_SERVER_ERROR(999, "Unexpected server error.");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

}
