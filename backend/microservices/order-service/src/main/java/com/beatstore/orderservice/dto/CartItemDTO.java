package com.beatstore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CartItemDTO {
    private String contentOfferHash;
    private BigDecimal price;
    private int quantity;
}
