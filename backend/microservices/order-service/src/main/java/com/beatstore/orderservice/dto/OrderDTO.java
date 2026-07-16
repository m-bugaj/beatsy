package com.beatstore.orderservice.dto;

import com.beatstore.orderservice.common.enums.OrderStatus;
import com.beatstore.orderservice.common.enums.PaymentStatus;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
public class OrderDTO {
    private String orderNumber;
    private String buyerHash;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private String currency;
    private BigDecimal totalPrice;

    private String paymentProvider;
    private String paymentReference;

    private Set<OrderItemDTO> orderItems;

    private LocalDateTime createdAt;
}
