package com.beatstore.orderservice.client;

import com.beatstore.orderservice.dto.ContentOffersPricesValidationResult;
import com.beatstore.orderservice.dto.FetchContentOffersPricesCommand;
import com.beatstore.orderservice.dto.FetchContentOffersPricesResponse;
import com.beatstore.orderservice.dto.ValidateContentOffersPricesCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "marketplace-service")
public interface MarketplaceClient {

    @PostMapping("/internal/content-offer/prices/fetch")
    ResponseEntity<FetchContentOffersPricesResponse> getPricesForContentOffers(
            @RequestBody FetchContentOffersPricesCommand command
    );

    @PostMapping("/internal/content-offer/prices/validate")
    ResponseEntity<ContentOffersPricesValidationResult> getPricesForContentOffers(
            @RequestBody ValidateContentOffersPricesCommand command
    );
}
