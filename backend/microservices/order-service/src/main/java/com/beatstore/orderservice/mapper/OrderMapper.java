package com.beatstore.orderservice.mapper;

import com.beatstore.orderservice.dto.OrderDTO;
import com.beatstore.orderservice.dto.OrderItemDTO;
import com.beatstore.orderservice.model.Order;
import com.beatstore.orderservice.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDto(Order order) {

        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();

        dto.setOrderNumber(order.getOrderNumber());
        dto.setBuyerHash(order.getBuyerHash());
        dto.setStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setCurrency(order.getCurrency().toString());
        //TODO MB: zmienić na total price w order
        dto.setTotalPrice(order.getTotalAmount());
        dto.setPaymentProvider(order.getPaymentProvider());
        dto.setPaymentReference(order.getPaymentReference());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setOrderItems(
                order.getOrderItems()
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toSet())
        );

        return dto;
    }

    private OrderItemDTO toDto(OrderItem orderItem) {

        OrderItemDTO dto = new OrderItemDTO();

        dto.setContentOfferHash(orderItem.getContentOfferHash());
        dto.setContentHash(orderItem.getContentHash());
        dto.setTitle(orderItem.getProductName());
        dto.setPrice(orderItem.getUnitPrice());
        dto.setQuantity(orderItem.getQuantity());

        return dto;
    }
}
