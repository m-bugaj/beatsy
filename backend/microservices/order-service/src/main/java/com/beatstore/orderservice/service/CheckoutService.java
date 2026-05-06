package com.beatstore.orderservice.service;

import com.beatstore.orderservice.client.MarketplaceClient;
import com.beatstore.orderservice.dto.CartItemDTO;
import com.beatstore.orderservice.dto.ContentOffersPricesValidationResult;
import com.beatstore.orderservice.dto.ValidateContentOffersPricesCommand;
import com.beatstore.orderservice.repository.OrderItemRepository;
import com.beatstore.orderservice.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final MarketplaceClient marketplaceClient;

    public CheckoutService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, MarketplaceClient marketplaceClient) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.marketplaceClient = marketplaceClient;
    }

    public void checkout(String buyerHash) {
            // 1. Fetch the buyer's cart (order with status "CART")
            // 2. Validate the cart items (check if content offers are still available, prices are correct, etc.)
            // 3. Create a new order with status "PENDING_PAYMENT"
            // 4. Clear the buyer's cart
            // 5. Return the order details for payment processing

        Set<CartItemDTO> cartItems = cartService.getCartForUser(buyerHash).getCartItems();
        Set<String> contentOfferHashes = cartItems.stream()
                .map(CartItemDTO::getContentOfferHash)
                .collect(Collectors.toSet());
        validateAndUpdateCart(cartItems);
    }

    private void validateAndUpdateCart(Set<CartItemDTO> cartItems) {
        Map<String, BigDecimal> contentOfferHashToPriceInCart = cartItems.stream()
                .collect(Collectors.toMap(
                        CartItemDTO::getContentOfferHash,
                        CartItemDTO::getPrice
                ));
        ContentOffersPricesValidationResult validationResult =
                marketplaceClient.validatePricesForContentOffers(
                        new ValidateContentOffersPricesCommand(contentOfferHashToPriceInCart)
                ).getBody();

        if (validationResult != null && !validationResult.isAllValid()) {
            if (!validationResult.getUnavailableContentOffersHashes().isEmpty()) {
                Set<String> unavailableContentOffersHashes = validationResult.getUnavailableContentOffersHashes();
                cartItems.stream()
                        .filter(cartItem -> unavailableContentOffersHashes.contains(cartItem.getContentOfferHash()))
                        .forEach(cartItems::remove);
            }

            if (!validationResult.getPriceChanges().isEmpty()) {
                Set<ContentOffersPricesValidationResult.PriceChange> priceChanges = validationResult.getPriceChanges();
                priceChanges.forEach(priceChange -> cartItems.stream()
                        .filter(cartItem -> cartItem.getContentOfferHash().equals(priceChange.getContentOfferHash()) && cartItem.getPrice().compareTo(priceChange.getOldPrice()) == 0)
                        .forEach(cartItem -> cartItem.setPrice(priceChange.getNewPrice())));
            }
        }
    }
}
