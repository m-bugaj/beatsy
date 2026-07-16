package com.beatstore.orderservice.dto;

import lombok.Setter;

import java.math.BigDecimal;

@Setter
public class OrderItemDTO {
    private String contentOfferHash;
    private String contentHash;

    private String title;

    private BigDecimal price;

    private Integer quantity;

}
