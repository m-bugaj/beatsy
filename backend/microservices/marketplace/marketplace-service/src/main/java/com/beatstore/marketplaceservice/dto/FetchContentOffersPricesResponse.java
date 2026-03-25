package com.beatstore.marketplaceservice.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
public class FetchContentOffersPricesResponse {
    Map<String, BigDecimal> contentOfferHashToPrice;
}
