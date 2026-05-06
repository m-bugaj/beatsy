package com.beatstore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ValidateContentOffersPricesCommand {
    Map<String, BigDecimal> contentOfferHashToPriceInCart;
}
