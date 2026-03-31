package com.beatstore.orderservice.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BEAT_NOT_FOUND(1, "Beat not found!"),
    INTERNAL_SERVER_ERROR(999, "Unexpected server error."),

//    ORDER-SERVICE
    CART_NOT_FOUND(1000, "Cart not found!");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

}
