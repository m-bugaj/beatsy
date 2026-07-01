package com.beatstore.orderservice.service;

import com.beatstore.marketplacerestclient.client.ContentOfferClient;
import com.beatstore.marketplacerestclient.common.dto.FetchContentOffersPricesCommand;
import com.beatstore.marketplacerestclient.common.dto.FetchContentOffersPricesResponse;
import com.beatstore.orderservice.dto.*;
import com.beatstore.orderservice.exceptions.CartNotFoundException;
import com.beatstore.orderservice.model.Cart;
import com.beatstore.orderservice.model.CartItem;
import com.beatstore.orderservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ContentOfferClient contentOfferClient;

    public CartService(CartRepository cartRepository, ContentOfferClient contentOfferClient) {
        this.cartRepository = cartRepository;
        this.contentOfferClient = contentOfferClient;
    }

    @Transactional
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
        cartRepository.save(cart);
    }

    private Cart createNewCart(String buyerHash) {
        return new Cart(buyerHash);
    }

    public CartDTO getCartForUser(String buyerHash) {
        Cart cart = cartRepository.findFirstByBuyerHash(buyerHash)
                .orElseThrow(() -> new CartNotFoundException(buyerHash));
        Set<CartItem> cartItems = cart.getCartItems();
        Set<String> contentOfferHashes = cartItems.stream()
                .map(CartItem::getContentOfferHash)
                .collect(Collectors.toSet());
        Map<String, BigDecimal> contentOfferHashToPrice =
                getPricesForCartItems(contentOfferHashes).getContentOfferHashToPrice();
        Set<CartItemDTO> cartItemsDto = cartItems.stream()
                .map(cartItem -> new CartItemDTO(cartItem.getContentOfferHash(), contentOfferHashToPrice.get(cartItem.getContentOfferHash()), cartItem.getQuantity()))
                .collect(Collectors.toSet());
        return new CartDTO(cart.getBuyerHash(), cartItemsDto, cart.getCurrency());
    }

    private FetchContentOffersPricesResponse getPricesForCartItems(Set<String> contentOfferHashes) {
        return contentOfferClient
                .getPricesForContentOffers(new FetchContentOffersPricesCommand(contentOfferHashes))
                .getBody();
    }
}
