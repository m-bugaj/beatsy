package com.beatstore.orderservice.controller;

import com.beatstore.orderservice.context.RequestContext;
import com.beatstore.orderservice.service.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/secured/checkout")
public class CheckoutController {
    private final RequestContext requestContext;
    private final CheckoutService checkoutService;

    public CheckoutController(RequestContext requestContext, CheckoutService checkoutService) {
        this.requestContext = requestContext;
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<?> checkout() {
        String buyerHash = requestContext.getUserHash();
        checkoutService.checkout(buyerHash);
        return ResponseEntity.ok().build();
    }
}
