package com.beatstore.marketplaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentOffersPricesValidationResult {
    private boolean allValid;
    private Set<String> unavailableContentOffersHashes;
    private Set<PriceChange> priceChanges;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceChange {
        private String contentOfferHash;
        private BigDecimal oldPrice;
        private BigDecimal newPrice;
    }
}
