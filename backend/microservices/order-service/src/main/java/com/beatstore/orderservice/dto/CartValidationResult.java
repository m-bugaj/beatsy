package com.beatstore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class CartValidationResult {
    private boolean valid;
    private CartDTO updatedCart;

    public boolean isNotValid() {
        return !valid;
    }
}
