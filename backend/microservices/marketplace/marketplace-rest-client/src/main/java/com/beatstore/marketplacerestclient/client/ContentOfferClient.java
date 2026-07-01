package com.beatstore.marketplacerestclient.client;

import com.beatstore.marketplacerestclient.common.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
        name = "marketplace-service",
        contextId = "contentOfferClient",
        url = "http://localhost:8083"
)
public interface ContentOfferClient {

    @PostMapping("/internal/content-offer/details/checkout")
    ResponseEntity<FetchCheckoutDetailsResponse> getContentOffersDetailsForCheckout(
            @RequestBody Set<String> contentOfferHashes
    );

    @PostMapping("/internal/content-offer/prices/fetch")
    ResponseEntity<FetchContentOffersPricesResponse> getPricesForContentOffers(
            @RequestBody FetchContentOffersPricesCommand command
    );

    @PostMapping("/internal/content-offer/prices/validate")
    ResponseEntity<ContentOffersPricesValidationResult> validatePricesForContentOffers(
            @RequestBody ValidateContentOffersPricesCommand command
    );
}
