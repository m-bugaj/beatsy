package com.beatstore.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @SequenceGenerator(
            name = "cart_items_id_seq",
            sequenceName = "cart_items_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cart_items_id_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "cart_id",
            nullable = false
    )
    private Cart cart;

    @Column(nullable = false)
    private String contentOfferHash;

    @Column(nullable = false)
    private Integer quantity;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CartItem(String contentOfferHash, Integer quantity) {
        this.contentOfferHash = contentOfferHash;
        this.quantity = quantity;
    }
}