package com.beatstore.orderservice.service;

import com.beatstore.orderservice.dto.AddItemsToCartCommand;
import com.beatstore.orderservice.model.Cart;
import com.beatstore.orderservice.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void createOrUpdateCart(String buyerHash, AddItemsToCartCommand command) {
        // Na razie system przewiduje tylko jednosztukowe zakupy
        Map<String, Integer> contentOfferHashToQuantity = command.getContentOfferHashes().stream()
                .collect(Collectors.toMap(
                        hash -> hash,
                        _ -> 1
                ));
        Cart cart = cartRepository.findFirstByBuyerHash(buyerHash)
                .orElseGet(() -> createNewCart(buyerHash));
        cart.addItemsToCart(contentOfferHashToQuantity);
    }

    private Cart createNewCart(String buyerHash) {
        return new Cart(buyerHash);
    }
}
