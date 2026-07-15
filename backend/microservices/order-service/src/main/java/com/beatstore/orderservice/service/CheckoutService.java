package com.beatstore.orderservice.service;

import com.beatstore.marketplacerestclient.client.ContentOfferClient;
import com.beatstore.marketplacerestclient.common.dto.ContentOfferCheckoutDetails;
import com.beatstore.marketplacerestclient.common.dto.ContentOffersPricesValidationResult;
import com.beatstore.marketplacerestclient.common.dto.ValidateContentOffersPricesCommand;
import com.beatstore.orderservice.common.enums.Currency;
import com.beatstore.orderservice.dto.*;
import com.beatstore.orderservice.model.Order;
import com.beatstore.orderservice.model.OrderItem;
import com.beatstore.orderservice.repository.OrderItemRepository;
import com.beatstore.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ContentOfferClient contentOfferClient;

    public CheckoutService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ContentOfferClient contentOfferClient) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.contentOfferClient = contentOfferClient;
    }

    @Transactional
    public CartValidationResult checkout(String buyerHash) {
            // 1. Fetch the buyer's cart (order with status "CART")
//         DONE   // 2. Validate the cart items (check if content offers are still available, prices are correct, etc.)
//         DONE  // 3. Create a new order with status "PENDING_PAYMENT"
            // 4. Clear the buyer's cart
            // 5. Return the order details for payment processing
        CartDTO cart = cartService.getCartForUser(buyerHash);
        Set<CartItemDTO> cartItems = cart.getCartItems();


        CartValidationResult cartValidationResult = validateAndUpdateCart(cart);
        if (cartValidationResult.isNotValid()) {
            return cartValidationResult;
        }

        Order order = createOrder(buyerHash, cart.getCurrency(), calculateTotalCartPrice(cart), null,
                null, cartItems);
        orderRepository.save(order);

        return null;
    }

    private BigDecimal calculateTotalCartPrice(CartDTO cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrder(String buyerHash, Currency currency, BigDecimal totalAmount, String paymentProvider,
                              String paymentReference, Set<CartItemDTO> cartItems) {
        Map<String, CartItemDTO> contentOfferHashToCartItemDto = cartItems.stream()
                .collect(Collectors.toMap(
                        CartItemDTO::getContentOfferHash,
                        cartItem -> cartItem
                ));

        Set<ContentOfferCheckoutDetails> contentOffersDetails = Objects.requireNonNull(
                        contentOfferClient
                                .getContentOffersDetailsForCheckout(contentOfferHashToCartItemDto.keySet())
                                .getBody()
                )
                .getCheckoutDetails();

        Set<OrderItem> orderItems = contentOffersDetails.stream()
                .map(details -> {
                    CartItemDTO cartItemDTO = contentOfferHashToCartItemDto.get(details.getContentOfferHash());
                    return createOrderItem(details, cartItemDTO.getPrice(), cartItemDTO.getQuantity());
                })
                .collect(Collectors.toSet());

        return Order.createNewOrder(
                buyerHash,
                currency,
                totalAmount,
                paymentProvider,
                paymentReference,
                orderItems
        );
    }

    private OrderItem createOrderItem(
            ContentOfferCheckoutDetails details,
            BigDecimal unitPrice,
            Integer quantity
    ) {
        return OrderItem.builder()
                .contentOfferHash(details.getContentOfferHash())
                .sellerHash(details.getSellerHash())
                .productType(details.getContentType().name())
                .contentHash(details.getContentHash())
                .productName(details.getContentName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)))
                .build();
    }

    private CartValidationResult validateAndUpdateCart(CartDTO cart) {
        Set<CartItemDTO> cartItems = cart.getCartItems();
        Map<String, BigDecimal> contentOfferHashToPriceInCart = extractCartPrices(cartItems);
        ContentOffersPricesValidationResult validationResult = fetchValidationResult(contentOfferHashToPriceInCart);

        if (validationResult != null && !validationResult.isAllValid()) {
            removeUnavailableItems(cartItems, validationResult.getUnavailableContentOffersHashes());
            updateItemPrices(cartItems, validationResult.getPriceChanges());
            return new CartValidationResult(false, new CartDTO(cart.getBuyerHash(), cartItems, cart.getCurrency()));
        }
        return new CartValidationResult(true, cart);
    }

    private Map<String, BigDecimal> extractCartPrices(Set<CartItemDTO> cartItems) {
        return cartItems.stream()
                .collect(Collectors.toMap(
                        CartItemDTO::getContentOfferHash,
                        CartItemDTO::getPrice
                ));
    }

    private ContentOffersPricesValidationResult fetchValidationResult(Map<String, BigDecimal> contentOfferHashToPriceInCart) {
        return contentOfferClient.validatePricesForContentOffers(
                new ValidateContentOffersPricesCommand(contentOfferHashToPriceInCart)
        ).getBody();
    }

    private void removeUnavailableItems(Set<CartItemDTO> cartItems, Set<String> unavailableHashes) {
        if (unavailableHashes != null && !unavailableHashes.isEmpty()) {
            cartItems.removeIf(cartItem -> unavailableHashes.contains(cartItem.getContentOfferHash()));
        }
    }

    private void updateItemPrices(Set<CartItemDTO> cartItems, Set<ContentOffersPricesValidationResult.PriceChange> priceChanges) {
        if (priceChanges != null && !priceChanges.isEmpty()) {
            priceChanges.forEach(priceChange -> cartItems.stream()
                    .filter(cartItem -> cartItem.getContentOfferHash().equals(priceChange.getContentOfferHash())
                            && cartItem.getPrice().compareTo(priceChange.getOldPrice()) == 0)
                    .forEach(cartItem -> cartItem.setPrice(priceChange.getNewPrice())));
        }
    }
}
