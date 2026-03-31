package com.beatstore.orderservice.dto;

import com.beatstore.orderservice.common.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class CartDTO {
    private String buyerHash;
    private Set<CartItemDTO> cartItems;
    private Currency currency;
}
