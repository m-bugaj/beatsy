package com.beatstore.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @SequenceGenerator(
            name = "order_items_id_seq",
            sequenceName = "order_items_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_items_id_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

    @Column(nullable = false)
    private String contentOfferHash;

    @Column(nullable = false)
    private String sellerHash;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}