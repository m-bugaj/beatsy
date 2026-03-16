package com.beatstore.orderservice.repository;


import com.beatstore.orderservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findFirstByBuyerHash(String buyerHash);
}
