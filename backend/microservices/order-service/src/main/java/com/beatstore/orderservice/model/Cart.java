package com.beatstore.orderservice.model;

import com.beatstore.orderservice.common.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "carts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @SequenceGenerator(
            name = "carts_id_seq",
            sequenceName = "carts_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "carts_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String buyerHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public void addItemsToCart(Map<String, Integer> contentOfferHashToQuantity) {
        Set<CartItem> cartItems = contentOfferHashToQuantity.entrySet()
                .stream()
                .map(entry -> new CartItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
        this.cartItems.addAll(cartItems);
    }

    public Cart(String buyerHash) {
        this.buyerHash = buyerHash;
        this.currency = Currency.USD;
    }
}