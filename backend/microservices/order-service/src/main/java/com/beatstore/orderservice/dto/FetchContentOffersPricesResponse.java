package com.beatstore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Getter
public class FetchContentOffersPricesResponse {
    Map<String, BigDecimal> contentOfferHashToPrice;
}
