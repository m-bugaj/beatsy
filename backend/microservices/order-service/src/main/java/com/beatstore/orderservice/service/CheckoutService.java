package com.beatstore.orderservice.service;

import com.beatstore.orderservice.repository.OrderItemRepository;
import com.beatstore.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CheckoutService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void checkout(String buyerHash) {

    }
}
