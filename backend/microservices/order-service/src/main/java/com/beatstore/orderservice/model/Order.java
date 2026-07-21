package com.beatstore.orderservice.model;

import com.beatstore.orderservice.common.enums.Currency;
import com.beatstore.orderservice.common.enums.OrderStatus;
import com.beatstore.orderservice.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @SequenceGenerator(
            name = "orders_id_seq",
            sequenceName = "orders_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orders_id_seq"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String buyerHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private String paymentProvider;

    private String paymentReference;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderStatusHistory> statusHistory = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Order createNewOrder(String buyerHash, Currency currency, BigDecimal totalAmount,
                                       String paymentProvider, String paymentReference, Set<OrderItem> orderItems) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .buyerHash(buyerHash)
                .currency(currency)
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentProvider(paymentProvider)
                .paymentReference(paymentReference)
                .orderItems(orderItems)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        return order;
    }
}