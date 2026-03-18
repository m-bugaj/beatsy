package com.beatstore.orderservice.controller;

import com.beatstore.orderservice.context.RequestContext;
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
    private final RequestContext requestContext;

    public CartController(CartService cartService, RequestContext requestContext) {
        this.cartService = cartService;
        this.requestContext = requestContext;
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItemsToCart(
            @RequestBody AddItemsToCartCommand command
    ) {
        String buyerHash = requestContext.getUserHash();
        cartService.createOrUpdateCart(buyerHash, command);
        return ResponseEntity.ok().build();
    }
}
