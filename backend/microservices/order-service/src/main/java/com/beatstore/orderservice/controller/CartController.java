package com.beatstore.orderservice.controller;

import com.beatstore.orderservice.dto.AddItemsToCartCommand;
import com.beatstore.orderservice.model.CartItem;
import com.beatstore.orderservice.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/secured/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //TODO MB: BUYERHASH POWINIEN BYĆ POBIERANY Z REQUEST CONTEXT
    @PostMapping("/{buyerHash}/items")
    public ResponseEntity<Void> addItemsToCart(
            @PathVariable String buyerHash,
            @RequestBody AddItemsToCartCommand command
    ) {
        cartService.createOrUpdateCart(buyerHash, command);
        return ResponseEntity.ok().build();
    }
}
