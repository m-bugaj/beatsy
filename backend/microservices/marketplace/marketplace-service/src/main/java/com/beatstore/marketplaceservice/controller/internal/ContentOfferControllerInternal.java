package com.beatstore.marketplaceservice.controller.internal;

import com.beatstore.marketplaceservice.dto.ContentOffersPricesValidationResult;
import com.beatstore.marketplaceservice.dto.FetchContentOffersPricesCommand;
import com.beatstore.marketplaceservice.dto.FetchContentOffersPricesResponse;
import com.beatstore.marketplaceservice.dto.ValidateContentOffersPricesCommand;
import com.beatstore.marketplaceservice.service.ContentOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/internal/content-offer")
public class ContentOfferControllerInternal {
    private final ContentOfferService contentOfferService;

    public ContentOfferControllerInternal(ContentOfferService contentOfferService) {
        this.contentOfferService = contentOfferService;
    }

    @PostMapping("/prices/fetch")
    public ResponseEntity<FetchContentOffersPricesResponse> getPricesForContentOffers(
            @RequestBody FetchContentOffersPricesCommand command
    ) {
        Map<String, BigDecimal> contentOffersPrices = contentOfferService.getContentOffersPrices(command);
        return ResponseEntity.ok(new FetchContentOffersPricesResponse(contentOffersPrices));
    }

    @PostMapping("/prices/validate")
    public ResponseEntity<ContentOffersPricesValidationResult> validatePricesForContentOffers(@RequestBody ValidateContentOffersPricesCommand command) {
        ContentOffersPricesValidationResult validationResult = contentOfferService.validatePricesForContentOffers(command);
        return ResponseEntity.ok(validationResult);
    }
}
