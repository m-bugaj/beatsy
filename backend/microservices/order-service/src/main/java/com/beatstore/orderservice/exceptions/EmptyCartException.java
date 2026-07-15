package com.beatstore.orderservice.exceptions;

import org.springframework.http.HttpStatus;

public class EmptyCartException extends DomainException {
    private static final ErrorCode errorCode = ErrorCode.CART_IS_EMPTY;
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public EmptyCartException(String userHash) {

        super(
                errorCode,
                httpStatus,
                String.format("Cart for user [%s] is empty!", userHash)
        );
    }
}
