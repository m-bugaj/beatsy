package com.beatstore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CartItemDTO {
    private String contentOfferHash;
    @Setter
    private BigDecimal price;
    private int quantity;
}
