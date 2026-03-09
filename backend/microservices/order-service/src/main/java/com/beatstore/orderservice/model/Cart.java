package com.beatstore.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private UUID buyerId;

    @Column(nullable = false)
    private String currency;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}