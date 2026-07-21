package com.beatstore.orderservice.model;

import com.beatstore.orderservice.common.enums.Currency;
import com.beatstore.orderservice.common.enums.OrderStatus;
import com.beatstore.orderservice.common.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void createNewOrder_ustawiaWKazdejPozycjiOdwolanieDoZamowienia() {
        OrderItem firstItem = orderItem("offer-1");
        OrderItem secondItem = orderItem("offer-2");
        Set<OrderItem> items = Set.of(firstItem, secondItem);

        Order order = Order.createNewOrder(
                "buyer-hash",
                Currency.USD,
                new BigDecimal("20.00"),
                null,
                null,
                items
        );

        assertThat(order.getOrderItems())
                .allSatisfy(item -> assertThat(item.getOrder()).isSameAs(order));
    }

    @Test
    void createNewOrder_zachowujePrzekazanePozycjeIStatusyPoczatkowe() {
        OrderItem item = orderItem("offer-1");

        Order order = Order.createNewOrder(
                "buyer-hash",
                Currency.USD,
                new BigDecimal("10.00"),
                null,
                null,
                Set.of(item)
        );

        assertThat(order.getOrderItems()).containsExactly(item);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    private OrderItem orderItem(String contentOfferHash) {
        return OrderItem.builder()
                .contentOfferHash(contentOfferHash)
                .sellerHash("seller-hash")
                .productType("LICENSE")
                .contentHash("content-hash")
                .productName("Beat")
                .quantity(1)
                .unitPrice(new BigDecimal("10.00"))
                .totalPrice(new BigDecimal("10.00"))
                .build();
    }
}
