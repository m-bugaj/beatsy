package com.beatstore.orderservice.client;

import com.beatstore.orderservice.dto.FetchContentOffersPricesCommand;
import com.beatstore.orderservice.dto.FetchContentOffersPricesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "marketplace-service")
public interface MarketplaceClient {

    @PostMapping("/prices/fetch")
    ResponseEntity<FetchContentOffersPricesResponse> getPricesForContentOffers(
            @RequestBody FetchContentOffersPricesCommand command
    );
}
