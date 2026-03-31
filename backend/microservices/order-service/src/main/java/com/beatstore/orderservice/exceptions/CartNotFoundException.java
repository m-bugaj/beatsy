package com.beatstore.orderservice.exceptions;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends DomainException {
    private static final ErrorCode errorCode = ErrorCode.CART_NOT_FOUND;
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public CartNotFoundException(String userHash) {

        super(
                errorCode,
                httpStatus,
                String.format("Cart for user [%s] not found!", userHash)
        );
    }
}
