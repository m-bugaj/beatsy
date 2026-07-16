package com.beatstore.orderservice.dto;

import lombok.Getter;

@Getter
public class CheckoutResult {
    private boolean success;
    private OrderDTO order;
    private CartValidationResult validationResult;

    public static CheckoutResult success(OrderDTO order) {
        CheckoutResult result = new CheckoutResult();
        result.success = true;
        result.order = order;
        return result;
    }

    public static CheckoutResult validationFailed(CartValidationResult validationResult) {
        CheckoutResult result = new CheckoutResult();
        result.success = false;
        result.validationResult = validationResult;
        return result;
    }

    public boolean isValidationFailed() {
        return !success;
    }
}
